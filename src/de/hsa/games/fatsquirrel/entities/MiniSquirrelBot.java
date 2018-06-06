package de.hsa.games.fatsquirrel.entities;

import de.hsa.games.fatsquirrel.botapi.BotController;
import de.hsa.games.fatsquirrel.botapi.BotControllerFactory;
import de.hsa.games.fatsquirrel.botapi.ControllerContext;
import de.hsa.games.fatsquirrel.botapi.OutOfViewException;
import de.hsa.games.fatsquirrel.core.EntityContext;
import de.hsa.games.fatsquirrel.core.EntityType;
import de.hsa.games.fatsquirrel.core.XY;
import de.hsa.games.fatsquirrel.util.XYSupport;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.logging.Logger;

public class MiniSquirrelBot extends MiniSquirrel {
    private final static Logger logger = Logger.getLogger("MINI_SQUIRREL_BOT");
    private static final int VIEW_DISTANCE = 21;
    private BotControllerFactory botControllerFactory;
    private BotController botController;

    MiniSquirrelBot(int energy, XY position, MasterSquirrel daddy, BotControllerFactory botControllerFactory) {
        super(energy, position, daddy);
        this.botControllerFactory = botControllerFactory;
        this.botController = botControllerFactory.createMiniBotController();
    }

    @Override
    public XY nextStep(EntityContext context) {
        // Not needed here, otherwise the minisquirrel would move twice (random + bot move)
        //super.nextStep(context);
        // But we still need this little line from the super method
        updateEnergy(-1);

        if (isParalyzed(true))
            return getPosition();

        ControllerContext view = new ControllerContextImpl(context);

        InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                logger.finest("MiniBot(ID: " + getId() + ") invoked: " + method.getName() + "(" + Arrays.toString(args) + ")");
                return method.invoke(view, args);
            }
        };

        ControllerContext proxyInstance = (ControllerContext) Proxy.newProxyInstance(
                ControllerContext.class.getClassLoader(),
                new Class[]{ControllerContext.class},
                handler);

        botController.nextStep(proxyInstance);
        return getPosition();
    }

    @Override
    public String toString() {
        return "MINI_SQUIRREL_BOT{ " + super.toString() + " }";
    }

    private boolean entityFriendly(Entity entity, Entity entitytoCheck) {
        if (entitytoCheck == null || entity == null)
            return true;
        if (EntityType.fromEntity(entity) == EntityType.MASTER_SQUIRREL_BOT) {
            MasterSquirrel masterSquirrelOfMiniSquirrel = ((MiniSquirrel) entity).getPatron();

            switch (EntityType.fromEntity(entitytoCheck)) {
                case MASTER_SQUIRREL:
                    return masterSquirrelOfMiniSquirrel.equals(entitytoCheck);
                case MINI_SQUIRREL:
                    return masterSquirrelOfMiniSquirrel.equals(((MiniSquirrel) entitytoCheck).getPatron());
                case WALL:
                default:
                    return false;
            }
        }
        return true;
    }

    private int collectedEnergyOfEntity(double energyLoss, Entity entity) {
        int energyCollected;
        EntityType entityType = EntityType.fromEntity(entity);

        switch (entityType) {
            case BAD_BEAST:
            case BAD_PLANT:
                entity.updateEnergy((int) energyLoss);
            case WALL:
                energyCollected = 0;
                break;
            case MASTER_SQUIRREL:
            case MASTER_SQUIRREL_BOT:
                energyCollected = (int) energyLoss;
                entity.updateEnergy(-(int) energyLoss);
                break;
            default:
                energyCollected = (int) energyLoss > entity.getEnergy() ? entity.getEnergy() : (int) energyLoss;
                entity.updateEnergy(-(int) energyLoss);
        }
        return energyCollected;
    }

    public class ControllerContextImpl implements ControllerContext {
        private EntityContext context;

        ControllerContextImpl(EntityContext context) {
            this.context = context;
        }

        @Override
        public XY getViewLowerLeft() {
            int x = getPosition().getX() - (VIEW_DISTANCE - 1) / 2;
            int y = getPosition().getY() + (VIEW_DISTANCE - 1) / 2;

            if (x < 0)
                x = 0;

            if (y > context.getSize().getY())
                y = context.getSize().getY();

            return new XY(x, y);
        }

        @Override
        public XY getViewUpperRight() {
            int x = getPosition().getX() + (VIEW_DISTANCE - 1) / 2;
            int y = getPosition().getY() - (VIEW_DISTANCE - 1) / 2;

            if (x > context.getSize().getX())
                x = context.getSize().getX();

            if (y < 0)
                y = 0;

            return new XY(x, y);
        }

        @Override
        public XY locate() {
            return getPosition();
        }

        @Override
        public EntityType getEntityAt(XY target) throws OutOfViewException {
            if (!XYSupport.isInRange(locate(), target, VIEW_DISTANCE)) {
                logger.finer("No Entity in the searchVector");
                throw new OutOfViewException("No Entity in the searchVector");
            }
            return context.getEntityType(target);
        }

        @Override
        public boolean isMine(XY target) throws OutOfViewException {
            if (!XYSupport.isInRange(locate(), target, VIEW_DISTANCE)) {
                logger.finer("Daddy not reachable");
                throw new OutOfViewException("Daddy not reachable");
            }
            try {
                return context.getEntityType(target).equals(getPatron());
            } catch (Exception e) {
                return false;
            }
        }

        @Override
        public void move(XY direction) {
            context.tryMove(MiniSquirrelBot.this, direction);
        }

        @Override
        public void spawnMiniBot(XY direction, int energy) {
            //kann keine MINI_SQUIRREL_BOT spawnen
        }

        //@Override
        public void implodeQuang(int impactRadius) {
            logger.info("Implode Method called");
            if (!(impactRadius >= 2 && impactRadius <= 10))
                return;

            float impactArea = (float) (Math.pow(impactRadius, 2) * Math.PI);
            int collectedEnergy = 0;
            for (int x = -impactRadius; x < impactRadius; x++) {
                for (int y = -impactRadius; y < impactRadius; y++) {
                    if (x == 0 && y == 0)
                        continue;

                    Entity entitytoCheck = context.getEntity(new XY(getPosition().getX() + x, getPosition().getY() + y));
                    if (entitytoCheck == null)
                        continue;
                    if (entityFriendly(MiniSquirrelBot.this, entitytoCheck))
                        continue;

                    int distance = (int) this.locate().distanceFrom(entitytoCheck.getPosition());
                    double energyLoss = (200 * (MiniSquirrelBot.this.getEnergy() / impactArea) * (1 - distance / impactRadius));
                    energyLoss = energyLoss < 0 ? 0 : energyLoss;
                    collectedEnergy += collectedEnergyOfEntity(energyLoss, entitytoCheck);
                    EntityType entityType = EntityType.fromEntity(entitytoCheck);

                    switch (entityType) {
                        case WALL:
                            break;
                        case BAD_PLANT:
                        case BAD_BEAST:
                        case GOOD_BEAST:
                        case GOOD_PLANT:
                            if (entitytoCheck.getEnergy() == 0) {
                                context.killAndReplace(entitytoCheck);
                            }
                            break;
                        case MINI_SQUIRREL:
                        case MASTER_SQUIRREL_BOT:
                            if (entitytoCheck.getEnergy() == 0) {
                                context.kill(entitytoCheck);
                            }
                    }
                }
            }

            getPatron().updateEnergy(collectedEnergy);
            context.kill(MiniSquirrelBot.this);
        }

        @Override
        public void implode(int impactRadius) {
            logger.info("Implode Method called");
            if (!(impactRadius >= 2 && impactRadius <= 10))
                return;

            int startX = locate().getX() - (impactRadius - 1) / 2;
            int startY = locate().getY() - (impactRadius - 1) / 2;
            int stopX = locate().getX() + (impactRadius - 1) / 2;
            int stopY = locate().getY() + (impactRadius - 1) / 2;

            if (startX < 0)
                startX = 0;
            if (startY < 0)
                startY = 0;
            if (stopX > getViewUpperRight().getX())
                stopX = getViewUpperRight().getX();
            if (stopY > getViewLowerLeft().getY())
                stopY = getViewLowerLeft().getY();

            int impactArea = (int) Math.round(Math.pow(impactRadius, 2) * Math.PI);
            int totalImplosionEnergy = 0;

            for (int x = startX; x < stopX; x++)
                for (int y = startY; y < stopY; y++) {
                    if (context.getEntity(new XY(x, y)) == null)
                        continue;
                    if (x == 0 && y == 0)
                        continue;

                    Entity entity = context.getEntity(new XY(x, y));

                    int distance = (int) this.locate().distanceFrom(entity.getPosition());
                    int energyLoss = (200 * (MiniSquirrelBot.this.getEnergy() / impactArea) * (1 - distance / impactRadius));

                    switch (EntityType.fromEntity(entity)) {
                        case BAD_BEAST:
                        case BAD_PLANT:
                            logger.fine("Imploding on Entity ID: " + entity.getId());
                            entity.updateEnergy(-energyLoss);
                            if (entity.getEnergy() >= 0)
                                context.killAndReplace(entity);
                            break;
                        case GOOD_PLANT:
                        case GOOD_BEAST:
                            logger.fine("Imploding on Entity ID: " + entity.getId());
                            entity.updateEnergy(energyLoss);
                            if (entity.getEnergy() <= 0)
                                context.killAndReplace(entity);
                            break;
                        case MINI_SQUIRREL_BOT:
                        case MINI_SQUIRREL:
                            if (getPatron() == ((MiniSquirrel) entity).getPatron())
                                continue;
                            logger.fine("Imploding on Entity ID: " + entity.getId());
                            entity.updateEnergy(energyLoss);
                            if (entity.getEnergy() <= 0)
                                context.killAndReplace(entity);
                            break;
                        case MASTER_SQUIRREL:
                        case MASTER_SQUIRREL_BOT:
                            MasterSquirrel masterSquirrel = (MasterSquirrel) entity;
                            if (!(masterSquirrel.isPatronOf(MiniSquirrelBot.this)))
                                if (entity.getEnergy() < -energyLoss)
                                    energyLoss = -entity.getEnergy();
                            logger.fine("Imploding on Entity ID: " + entity.getId());
                            entity.updateEnergy(energyLoss);
                            break;
                    }
                    totalImplosionEnergy = totalImplosionEnergy - energyLoss;
                    logger.fine("Imploding: Total implosion energy: " + totalImplosionEnergy);
                }
            getPatron().updateEnergy(totalImplosionEnergy);
        }

        @Override
        public int getEnergy() {
            return MiniSquirrelBot.this.getEnergy();
        }

        @Override
        public XY directionOfMaster() {
            return XYSupport.assignMoveVector(getPatron().getPosition().reduceVector(MiniSquirrelBot.this.getPosition()));
        }

        @Override
        public long getRemainingSteps() {
            return 0;
        }
    }

}


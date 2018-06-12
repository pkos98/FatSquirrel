package de.hsa.games.fatsquirrel.entities;

import de.hsa.games.fatsquirrel.botapi.BotController;
import de.hsa.games.fatsquirrel.botapi.BotControllerFactory;
import de.hsa.games.fatsquirrel.botapi.ControllerContext;
import de.hsa.games.fatsquirrel.botapi.OutOfViewException;
import de.hsa.games.fatsquirrel.core.EntityContext;
import de.hsa.games.fatsquirrel.core.EntityType;
import de.hsa.games.fatsquirrel.core.XY;
import de.hsa.games.fatsquirrel.util.MiniSquirrelInjector;
import de.hsa.games.fatsquirrel.util.XYSupport;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.logging.Logger;

public class MasterSquirrelBot extends MasterSquirrel {


    private static final int VIEW_DISTANCE = 31;
    private final BotController botController;
    private final BotControllerFactory factory;
    private Logger logger = Logger.getLogger(getClass().getName());
    private MiniSquirrelInjector miniSquirrelInjector;

    public MasterSquirrelBot(int id, int startEnergy, XY startPos, BotControllerFactory factory) {
        super(id, startEnergy, startPos);
        this.factory = factory;
        botController = factory.createMasterBotController();
        name = botController.getClass().getName();
    }

    @Override
    public XY nextStep(EntityContext entityContext) {
        miniSquirrelInjector = (MiniSquirrelInjector) entityContext;
        if (isParalyzed(true))
            return getPosition();
        ControllerContext view = new ControllerContextImpl(entityContext);

        InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                logger.finest("MASTER_SQUIRREL_BOT(ID: " + getId() + ") invoked: " + method.getName() + "(" + Arrays.toString(args) + ")");
                return method.invoke(view, args);
            }
        };

        ControllerContext proxyInstance = (ControllerContext) Proxy.newProxyInstance(
                ControllerContext.class.getClassLoader(),
                new Class[]{ControllerContext.class},
                handler);

        botController.nextStep(proxyInstance);
        if (getEnergy() >= 1000)
            proxyInstance.spawnMiniBot(XY.RIGHT, 1000);
        return getPosition();
    }

    public class ControllerContextImpl implements ControllerContext {
        private EntityContext context;

        public ControllerContextImpl(EntityContext context) {
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
        public EntityType getEntityAt(XY target) {
            if (!XYSupport.isInRange(locate(), target, VIEW_DISTANCE)) {
                logger.finer("No Entity in searchVecotr");
                throw new OutOfViewException("Kein Entity in Sichtweite (MasterBot)");
            }
            return context.getEntityType(target);
        }

        @Override
        public boolean isMine(XY target) {
            if (!XYSupport.isInRange(locate(), target, VIEW_DISTANCE)) {
                logger.finer("No Entity in searchVector");
                throw new OutOfViewException("No entity in view distance");
            }
            try {
                //if (isPatronOf() context.getEntity(target)))
                return true;
            } catch (Exception e) {
                return false;
            }
            //return false;
        }

        @Override
        public void move(XY direction) {
            context.tryMove(MasterSquirrelBot.this, direction);
        }

        @Override
        public void spawnMiniBot(XY direction, int energy) {
            if (energy > getEnergy())
                return;
            MiniSquirrelBot miniBot = new MiniSquirrelBot(energy, XYSupport.add(getPosition(), direction),
                    MasterSquirrelBot.this, factory);
            updateEnergy(-energy);
            addMiniSquirrel(miniBot);
            MasterSquirrelBot.this.miniSquirrelInjector.injectMiniSquirrel(miniBot);
        }

        @Override
        public void implode(int impactRadius) {
            // masterSquirrel kann nicht implodieren
        }

        @Override
        public int getEnergy() {
            return MasterSquirrelBot.this.getEnergy();
        }

        @Override
        public XY directionOfMaster() {
            return XY.ZERO_ZERO;
        }

        @Override
        public long getRemainingSteps() {
            return 0;
        }
    }
}

package de.hsa.games.fatsquirrel.core;

import de.hsa.games.fatsquirrel.botapi.ControllerContext;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.logging.Logger;

public class BotInvocationHandler implements InvocationHandler {
    private final ControllerContext target;
    private final Logger logger = java.util.logging.Logger.getLogger(getClass().getName());

    public BotInvocationHandler(ControllerContext target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //logger.info()
        return null;
    }
}

package engine;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class FTPSearchEngineApplication {
    public static ConcurrentMap<String,Boolean> res = new ConcurrentHashMap<>();

    public static void run (AbstractEngineConfig config, Class<? extends AbstractStarter> starter) throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        Method start = starter.getMethod("start", AbstractEngineConfig.class);
        start.invoke(starter.newInstance(), config);
    }
}

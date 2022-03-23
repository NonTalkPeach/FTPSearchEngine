import engine.FTPSearchEngineApplication;
import engine.planB.EngineConfigPlanB;
import engine.planB.StarterPlanB;

import java.lang.reflect.InvocationTargetException;

public class TestPlanB {
    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String startIP = "172.26.1.1";
        String endIP = "172.26.255.255";
        EngineConfigPlanB engineConfig = new EngineConfigPlanB(startIP,endIP,10000);
        FTPSearchEngineApplication.run(engineConfig, StarterPlanB.class);
    }
}

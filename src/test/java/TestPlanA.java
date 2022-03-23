import engine.FTPSearchEngineApplication;
import engine.planA.EngineConfigPlanA;
import engine.planA.StarterPlanA;
import java.lang.reflect.InvocationTargetException;

public class TestPlanA {
    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String startIP = "172.26.1.1";
        String endIP = "172.26.255.255";
        int portNum = 10000;
        EngineConfigPlanA engineConfig = new EngineConfigPlanA(startIP,endIP,portNum);
        FTPSearchEngineApplication.run(engineConfig, StarterPlanA.class);
    }
}

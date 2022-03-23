import engine.FTPSearchEngineApplication;
import engine.planC.EngineConfigPlanC;
import engine.planC.StarterPlanC;
import java.lang.reflect.InvocationTargetException;

public class TestPlanC {
    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String startIP = "172.26.1.1";
        String endIP = "172.26.255.255";
        int n = 10000;
        EngineConfigPlanC engineConfig = new EngineConfigPlanC(startIP,endIP, n);
        FTPSearchEngineApplication.run(engineConfig, StarterPlanC.class);
    }
}

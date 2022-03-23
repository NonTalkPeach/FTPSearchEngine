package engine.planB;

import engine.AbstractEngineConfig;

public class EngineConfigPlanB extends AbstractEngineConfig {
    /**
     * 系统配置 根据电脑配置而定
     */
    public static int HANDLE_IP_Num_PER_THREAD = 2000;
    public static int PORT_NUM = 10000;
    public EngineConfigPlanB(String startIP, String endIP, int portNum) {
        super(startIP, endIP);
        PORT_NUM = portNum;
    }
}

package engine.planA;

import engine.AbstractEngineConfig;

public class EngineConfigPlanA extends AbstractEngineConfig {
    private final int portNum;

    public EngineConfigPlanA(String startIP, String endIP, int portNum) {
        super(startIP, endIP);
        this.portNum = portNum;
    }

    public int getPortNum() {
        return portNum;
    }
}

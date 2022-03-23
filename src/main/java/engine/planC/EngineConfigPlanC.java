package engine.planC;

import engine.AbstractEngineConfig;
import engine.utils.IPUtil;

public class EngineConfigPlanC extends AbstractEngineConfig {
    /** 线程数量最大值为临时端口可用数量 */
    private final int nThread;
    public EngineConfigPlanC(String startIP, String endIP, int nThread) {
        super(startIP, endIP);
        this.nThread = nThread;
        IPUtil.nextIP(getEndIP());
    }

    public int getnThread() {
        return nThread;
    }
}

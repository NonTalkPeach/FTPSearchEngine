package engine;

import lombok.Data;

@Data
public abstract class AbstractEngineConfig {
    /**
     * 搜索参数
     */
    private int[] startIP;
    private int[] endIP;
    public AbstractEngineConfig(String startIP, String endIP) {
        String[] splitIP = startIP.split("\\.");
        this.startIP = new int[]{
                Integer.parseInt(splitIP[0]),
                Integer.parseInt(splitIP[1]),
                Integer.parseInt(splitIP[2]),
                Integer.parseInt(splitIP[3]),
        };
        splitIP = endIP.split("\\.");
        this.endIP = new int[]{
                Integer.parseInt(splitIP[0]),
                Integer.parseInt(splitIP[1]),
                Integer.parseInt(splitIP[2]),
                Integer.parseInt(splitIP[3]),
        };
    }
}

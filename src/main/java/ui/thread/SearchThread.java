package ui.thread;

import engine.AbstractEngineConfig;
import engine.AbstractStarter;
import engine.FTPSearchEngineApplication;
import javax.swing.*;
import java.lang.reflect.Constructor;

public class SearchThread extends Thread{
    private final String plan;
    private final String startIP;
    private final String endIP;
    private final int portNum;

    private final Thread resThread;
    private final JLabel timeLabel;

    public SearchThread(String plan, String startIP, String endIP, int portNum, JLabel timeLabel, Thread resThread) {
        this.plan = plan;
        this.startIP = startIP;
        this.endIP = endIP;
        this.portNum = portNum;
        this.timeLabel = timeLabel;
        this.resThread = resThread;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        try{
            Class<?> starter = Class.forName("engine.plan" + plan + ".StarterPlan" + plan);
            Constructor<?> constructor = Class.forName("engine.plan" + plan + ".EngineConfigPlan" + plan).getConstructor(String.class, String.class, int.class);
            AbstractEngineConfig config = (AbstractEngineConfig) constructor.newInstance(startIP, endIP, portNum);
            FTPSearchEngineApplication.run(config, (Class<? extends AbstractStarter>) starter);
        }catch (Exception e) {
            e.printStackTrace();
        }
        resThread.interrupt();
        timeLabel.setText("Spend Time :  "+ (System.currentTimeMillis() - startTime) / 1000.0 +" s");
    }
}

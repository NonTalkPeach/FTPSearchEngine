package engine.planA;

import engine.AbstractEngineConfig;
import engine.AbstractStarter;
import engine.planA.task.NIOConnectTask;
import engine.planA.task.NIOHandleTask;

import java.io.IOException;
import java.nio.channels.Selector;
import java.util.concurrent.Semaphore;

/**
 * PlanA
 * 开启两个线程，一个请求连接，一个使用NIO处理连接。同步执行
 * 可能存在selector 线程安全问题
 */
public class StarterPlanA extends AbstractStarter {
    @Override
    public void start(AbstractEngineConfig config) throws IOException {
        System.out.println("Start Plan A");
        EngineConfigPlanA engineConfig = (EngineConfigPlanA) config;
        //准备资源
        Semaphore semaphore = new Semaphore(engineConfig.getPortNum());
        Selector selector = Selector.open();

        //两个任务
        NIOConnectTask NIOConnectTask = new NIOConnectTask(
                config.getStartIP(), config.getEndIP(),
                selector, semaphore);
        NIOHandleTask NIOHandleTask = new NIOHandleTask(selector, semaphore);

        //开启两线程
        Thread connectThread = new Thread(NIOConnectTask);
        Thread handleThread = new Thread(NIOHandleTask);
        connectThread.start();
        handleThread.start();

        while (true) {
            if (!connectThread.isAlive() && semaphore.availablePermits() == engineConfig.getPortNum()){
                handleThread.interrupt();
                break;
            }
        }
        System.out.println("[Tao 's Info] : Searching work has already finished!");
    }
}

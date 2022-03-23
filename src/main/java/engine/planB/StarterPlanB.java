package engine.planB;

import engine.AbstractEngineConfig;
import engine.AbstractStarter;
import engine.planB.task.NIOSearchTask;
import engine.utils.IPUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * PlanB
 * 开启N个线程每个线程使用NIO执行额定数量的搜索任务
 */
public class StarterPlanB extends AbstractStarter {
    @Override
    public void start(AbstractEngineConfig config) throws InterruptedException {
        System.out.println("Start Plan B");

        int nThread = EngineConfigPlanB.PORT_NUM / EngineConfigPlanB.HANDLE_IP_Num_PER_THREAD;
        ExecutorService executorService = Executors.newFixedThreadPool(nThread);
        /*
         * 分配任务
         */
        int[] curIP = config.getStartIP();
        while (true) {
            int[] taskIP = new int[4];
            System.arraycopy(curIP, 0, taskIP, 0, 4);
            IPUtil.IPAdder(curIP, EngineConfigPlanB.HANDLE_IP_Num_PER_THREAD);
            if (IPUtil.isOver(curIP, config.getEndIP())) {
                NIOSearchTask NIOSearchTask = new NIOSearchTask(taskIP, IPUtil.IPSub(config.getEndIP(), taskIP) + 1);
                executorService.submit(NIOSearchTask);
                break;
            }else {
                NIOSearchTask NIOSearchTask = new NIOSearchTask(taskIP, EngineConfigPlanB.HANDLE_IP_Num_PER_THREAD);
                executorService.submit(NIOSearchTask);
            }
        }
        executorService.shutdown();
        while (!executorService.isTerminated()) {
            Thread.sleep(300);
        }
    }
}

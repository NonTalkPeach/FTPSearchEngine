package engine.planC;

import engine.AbstractEngineConfig;
import engine.AbstractStarter;
import engine.planC.task.SearchTask;
import engine.utils.IPUtil;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 最普通的想法，使用线程池一个IP一个线程搜索
 */
public class StarterPlanC extends AbstractStarter {
    @Override
    public void start(AbstractEngineConfig config) throws InterruptedException {
        System.out.println("Start Plan C");
        ExecutorService executorService = Executors.newFixedThreadPool(((EngineConfigPlanC)config).getnThread());
        while (!IPUtil.isEqual(config.getStartIP(), config.getEndIP())) {
            String IP = config.getStartIP()[0] + "." + config.getStartIP()[1] + "." + config.getStartIP()[2] + "." + config.getStartIP()[3];
            SearchTask searchTask = new SearchTask(IP);
            executorService.submit(searchTask);
            IPUtil.nextIP(config.getStartIP());
        }
        executorService.shutdown();
        while (!executorService.isTerminated()) { Thread.sleep(300); }
    }
}

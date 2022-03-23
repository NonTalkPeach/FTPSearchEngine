package engine;

import java.io.IOException;

public abstract class AbstractStarter {
    public abstract void start(AbstractEngineConfig config) throws IOException, InterruptedException;
}

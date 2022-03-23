package engine.planC.task;

import engine.FTPSearchEngineApplication;
import org.apache.commons.net.ftp.FTPClient;
import java.io.IOException;

public class SearchTask implements Runnable {
    private final String IP;
    public SearchTask(String IP) {
        this.IP = IP;
    }

    @Override
    public void run() {
        FTPClient ftpClient = new FTPClient();
        ftpClient.setConnectTimeout(1500);
        try {
            ftpClient.connect(IP);
            boolean login = ftpClient.login("ANONYMOUS", "");
            FTPSearchEngineApplication.res.put(IP, login);
        } catch (IOException ignore) {}
    }
}
package ui.thread;

import engine.FTPSearchEngineApplication;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import ui.Main;

import javax.swing.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class OutputResultThread extends Thread{
    @Override
    public void run() {
        while(!(Thread.currentThread().isInterrupted() && FTPSearchEngineApplication.res.isEmpty())) {
            for (String IP : FTPSearchEngineApplication.res.keySet()) {
                Main.IPS.add(IP);
                Main.jcb.setModel(new DefaultComboBoxModel(Main.IPS.toArray(new String[Main.IPS.size()])));
                System.out.println("  " + IP);
                if (FTPSearchEngineApplication.res.get(IP)) {
                    FTPClient ftpClient = new FTPClient();
                    try {
                        ftpClient.connect(IP);
                        ftpClient.login("ANONYMOUS","");
                        FTPFile[] ftpFiles = ftpClient.listFiles();
                        for (FTPFile ftpFile : ftpFiles) {
                            String name = new String(ftpFile.getName().getBytes(StandardCharsets.ISO_8859_1), "GBK");
                            System.out.println("\t" + name + "\t" + ftpFile.getSize()/1000.0 + "Kb");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {
                    System.out.println("\t可连接但是无法匿名登陆！");
                }
                FTPSearchEngineApplication.res.remove(IP);
            }
        }
        System.out.println("Searching work has finished!");
        Main.searchButton.setEnabled(true);
    }
}

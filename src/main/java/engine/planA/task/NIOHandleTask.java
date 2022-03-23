package engine.planA.task;

import engine.FTPSearchEngineApplication;
import engine.nio.FTPNIOClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.Semaphore;

/**
 * 用于对可连接IP的处理，包括 匿名登陆 和 获取信息
 */
public class NIOHandleTask implements Runnable {
    //选择器 注意线程安全
    private final Selector selector;

    //同步器
    private final Semaphore available;

    public NIOHandleTask(Selector selector, Semaphore available) {
        this.selector = selector;
        this.available = available;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                if (selector.selectNow() <= 0) continue; //这个消耗资源很大
                //selector.select();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            for (SelectionKey selection : selectionKeys) {
                SocketChannel socketChannel = (SocketChannel) selection.channel();
                try {
                    socketChannel.finishConnect();
                    selection.cancel();
                    //登录 、 获取信息
                    if (socketChannel.socket().getRemoteSocketAddress() != null) {
                        socketChannel.configureBlocking(true);
                        FTPNIOClient ftpnioClient = new FTPNIOClient(socketChannel.socket());
                        boolean login = ftpnioClient.login("ANONYMOUS", "");
                        String ip = socketChannel.socket().getRemoteSocketAddress().toString();
                        FTPSearchEngineApplication.res.put(ip.substring(1,ip.length() - 3), login);
                    }
                    socketChannel.close();
                    available.release();
                } catch (IOException ignored) {
                    // 释放资源
                    try {
                        socketChannel.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    selection.cancel();
                    available.release();
                }
            }
        }
        System.out.println("[Tao 's Info] : Handle work have already finished!");
    }
}

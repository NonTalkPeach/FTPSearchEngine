package engine.planA.task;

import engine.utils.IPUtil;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Semaphore;

/**
 * 用于遍历所有ip，找出可连接的ip
 */
public class NIOConnectTask implements Runnable{
    //选择器 注意线程安全
    private final Selector selector;
    //同步器
    private final Semaphore available;

    // 开始ip地址
    private final int[] startIP;

    // 结束ip地址
    private final int[] endIP;

    public NIOConnectTask(int[] startIP, int[] endIP, Selector selector, Semaphore available) {
        this.startIP = startIP;
        this.endIP = endIP;
        this.selector = selector;
        this.available = available;
        IPUtil.nextIP(endIP);
    }
    @Override
    public void run() {
        while (startIP[0] != endIP[0] || startIP[1] != endIP[1] || startIP[2] != endIP[2] || startIP[3] != endIP[3]) {
            String ip = startIP[0] + "." + startIP[1] + "." + startIP[2] + "." + startIP[3];
            try {
                // 保证临时端口数量够用
                available.acquire();

                SocketChannel socketChannel = SocketChannel.open();
                socketChannel.configureBlocking(false);
                socketChannel.register(selector, SelectionKey.OP_CONNECT);
                socketChannel.connect(new InetSocketAddress(ip, 21));
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                IPUtil.nextIP(startIP);
            }
        }
        System.out.println("[Tao 's Info] : All ip have already sent!");
    }
}

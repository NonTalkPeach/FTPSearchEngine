package engine.planB.task;

import engine.FTPSearchEngineApplication;
import engine.nio.FTPNIOClient;
import engine.utils.IPUtil;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 寻找可连接FTP IP 并且处理
 */
public class NIOSearchTask implements Runnable {

    private final int[] startIP;
    private final int size;

    public NIOSearchTask(int[] startIP, int size) {
        this.startIP = startIP;
        this.size = size;
    }

    @Override
    public void run() {
        List<SocketChannel> channelSet = new ArrayList<>(); //保存开着的socket

        Selector selector;
        try {
            selector = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("[Tao 's Info] : Selector Open Error!");
            return;
        }

        /*
         * 建立连接
         */
        int curConnectNum = 0;
        while (curConnectNum < size) {
            curConnectNum++;
            String ip = startIP[0] + "." + startIP[1] + "." + startIP[2] + "." + startIP[3];
            try {
                SocketChannel socketChannel = SocketChannel.open();
                socketChannel.configureBlocking(false);
                socketChannel.register(selector, SelectionKey.OP_CONNECT);
                socketChannel.connect(new InetSocketAddress(ip, 21));

                channelSet.add(socketChannel);
            } catch (Exception e) {}finally {
                IPUtil.nextIP(startIP);
            }
        }
        /*
         * 处理连接
         */
        while (curConnectNum > 0) {
            try {
                //if (selector.selectNow() == 0) continue; //这个消耗资源很大
                int select = selector.select(1500);
                if (select == 0) {
                    for (SocketChannel socketChannel : channelSet) {
                        socketChannel.close();
                    }
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("[Tao 's Info] : selector process error!");
            }
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            for (SelectionKey key : selectionKeys) {
                curConnectNum--;
                SocketChannel socketChannel = (SocketChannel) key.channel();
                boolean isFinishConnect = true;
                try {
                    socketChannel.finishConnect();
                } catch (IOException e) {
                    isFinishConnect = false;
                }
                /*
                    处理业务逻辑
                 */
                key.cancel();
                if (isFinishConnect) {
                    try {
                        socketChannel.configureBlocking(true);
                        FTPNIOClient ftpnioClient = new FTPNIOClient(socketChannel.socket());
                        boolean login = ftpnioClient.login("ANONYMOUS", "");
                        String ip = socketChannel.socket().getRemoteSocketAddress().toString();
                        FTPSearchEngineApplication.res.put(ip.substring(1,ip.length() - 3), login);
                    }catch (IOException ignore) {}
                }
                /*
                    关闭资源
                 */
                try {
                    socketChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            selector.close();
        } catch (IOException e) {
            System.out.println("selector 关闭 ERROR!");
        }
    }

}

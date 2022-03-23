package engine.nio;

import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;
import java.net.Socket;

public class FTPNIOClient extends FTPClient {
    /**
     * 把准备好的socket 安装到该类中
     * @param socket
     * @throws IOException
     */
    public FTPNIOClient(Socket socket) throws IOException {
        nioChange(socket);
        this.setConnectTimeout(3000);
    }
    private void nioChange(final Socket socket)
            throws IOException
    {
        _socket_ = socket;
        _connectAction_();
    }
}

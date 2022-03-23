package engine.utils;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.IOException;

public class IPUtil {
    /**
     * @return IP + in
     */
    public static void IPAdder(int[] IP, int in) {
        boolean flag = true;
        for (int i = 3; i >= 0; i--) {
            if(flag) {
                IP[i] += in;
                if (IP[i] > 255) {
                    in = IP[i] / 256;
                    IP[i] = IP[i] % 256;
                }else flag = false;
            }
        }
    }

    /**
     * @return return IP01 - IP02 相差多少个， 差值存到IP01
     */
    public static int IPSub(int[] IP01, int[] IP02) {
        boolean flag = false;
        for (int i = 3; i >= 0; i--) {
            if(flag) IP01[i]--;
            if (IP01[i] < IP02[i]) {
                flag = true;
                IP01[i] += 256;
            }else flag = false;
            IP01[i] = IP01[i] - IP02[i];
        }
        return IP01[0] * 16777216 + IP01[1] * 65536 + IP01[2] * 256 + IP01[3];
    }

    /**
     * @return return true if IP01 > IP02
     */
    public static boolean isOver(int[] IP01, int[] IP02) {
        for (int i = 0; i < 4; i++) {
            if (IP01[i] > IP02[i]) return true;
            else if (IP01[i] < IP02[i]) return false;
        }
        return false;
    }

    /**
     * IP + 1
     */
    public static void nextIP(int[] IP) {
        boolean flag = true;
        for (int i = 3; i >= 0; i--) {
            if(flag) {
                if (++IP[i] > 255) IP[i] = 0;
                else flag = false;
            }
        }
    }

    /**
     *
     * @return return true if IP01 = IP02
     */
    public static boolean isEqual(int[] IP01, int[] IP02) {
        return IP01[0] == IP02[0] && IP01[1] == IP02[1] && IP01[2] == IP02[2] && IP01[3] == IP02[3];
    }

    public static void outputFilesInfo(FTPClient ftpClient, String curPath, int k) throws IOException {
        ftpClient.changeWorkingDirectory(curPath);//找到指定目录
        FTPFile[] ftpFiles = ftpClient.listFiles();
        for (FTPFile file : ftpFiles) {
            if (file.isDirectory()) {
                for (int i = 0; i < k; i++) System.out.print("\t");
                System.out.println(file.getName());
                if (k < 5)
                    outputFilesInfo(ftpClient, curPath + "/" + file.getName(), k + 1);
            }else {
                for (int i = 0; i < k; i++) System.out.print("\t");
                System.out.println(file.getName());
            }
        }
    }
}

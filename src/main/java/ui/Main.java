package ui;

import ui.config.PrintStreamToGUI;
import ui.thread.OutputResultThread;
import ui.thread.SearchThread;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Main {
    public static PrintStreamToGUI consoleToGUI;
    public static ConcurrentLinkedDeque<String> IPS = new ConcurrentLinkedDeque<>(); //用于组合框
    public static JComboBox jcb; //组合框，需要时常更新
    public static JButton searchButton;
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setTitle("FTP Search Engine");    //设置窗口标题
        frame.setSize(800, 700);    //设置窗口大小
        frame.setLocationRelativeTo(null);//设置窗口居中显示
        frame.setResizable(false);//设置不可调整大小
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);//设置窗口关闭时程序结束

        /*
         *  输入组件
         */
        JPanel inputPanel = new JPanel();
        placeInputComponents(inputPanel);

        /*
         * 输出组件
         */
        JPanel outputListPanel = new JPanel();
        placeOutputComponents(outputListPanel);
        JScrollPane scrollPanel = new JScrollPane();
        //设置滚动条面板位置和大小
        scrollPanel.setBounds(5,180,775,450);
        /*将普通面板嵌入带滚动条的面板，所在位置由后者决定*/
        scrollPanel.getViewport().add(outputListPanel);

        frame.add(scrollPanel);
        frame.add(inputPanel);
        frame.setVisible(true);//设置窗口可见
    }
    private static void placeOutputComponents(JPanel panel) {
        panel.setLayout(new BorderLayout());//让JTextArea平铺整个JPanel

        //结果输出
        JTextArea area = new JTextArea();
        area.setEditable(false);
        Font font=new Font("Arial Unicode MS",Font.PLAIN,16);
        area.setFont(font);
        panel.add(area);

        /*
         *  控制台结果转换到swing
         */
        consoleToGUI = new PrintStreamToGUI(System.out, area);
        System.setOut(consoleToGUI);
        System.setErr(consoleToGUI);
    }

    private static void placeInputComponents(JPanel panel) {

        panel.setLayout(null);

        /*
         * 创建文本域用于用户输入 StartIP
         */
        JLabel startIPLabel = new JLabel("StartIP :");
        startIPLabel.setBounds(50,20,80,25);
        panel.add(startIPLabel);
        JTextField inputStartIP = new JTextField(20);
        inputStartIP.setBounds(120,20,120,25);
        panel.add(inputStartIP);

        /*
         * 创建文本域用于用户输入 EndIP
         */
        JLabel endIPLabel = new JLabel("EndIP :");
        endIPLabel.setBounds(50,50,80,25);
        panel.add(endIPLabel);
        JTextField inputEndIP = new JTextField(20);
        inputEndIP.setBounds(120,50,120,25);
        panel.add(inputEndIP);

        /*
         * 创建文本域用于用户输入 EndIP
         */
        JLabel portNumLabel = new JLabel("PortNum :");
        portNumLabel.setBounds(50,80,80,25);
        panel.add(portNumLabel);
        JTextField inputPortNum = new JTextField(20);
        inputPortNum.setBounds(120,80,60,25);
        inputPortNum.setText("10000");
        inputPortNum.setEnabled(true);
        panel.add(inputPortNum);

        /*
         * 创建单选框选择搜索方案
         */
        JRadioButton planA = new JRadioButton("Plan A:NIO+多线程，两个线程同步执行。 (消耗大，不稳定)");
        planA.setBounds(300,15,500,25);
        JRadioButton planB = new JRadioButton("Plan B:NIO+多线程，使用线程池，每个线程使用NIO处理多个连接。2000_per");
        planB.setBounds(300,48,500,25);
        JRadioButton planC = new JRadioButton("Plan C:使用多线程，BIO模式，每个线程处理一个连接。(速度快，内存消耗大)");
        planC.setBounds(300 ,80,500,25);
        planC.setSelected(true);
        ButtonGroup group = new ButtonGroup();
        group.add(planA);group.add(planB);group.add(planC);
        panel.add(planA);panel.add(planB);panel.add(planC);

        // 创建搜索按钮
        searchButton = new JButton("Search");
        searchButton.setBounds(200, 125, 120, 30);
        panel.add(searchButton);

        //显示消耗时间
        JLabel timeLabel = new JLabel("Spend Time :  0.000 s");
        timeLabel.setBounds(50,130,200,25);
        panel.add(timeLabel);

        //创建组合框
        JLabel jcbLabel = new JLabel("可连接IP : ");
        jcbLabel.setBounds(350,130,80,25);
        panel.add(jcbLabel);
        jcb = new JComboBox(IPS.toArray(new String[0]));
        jcb.setBackground(Color.white);
        jcb.setBounds(420,130,150,25);
        panel.add(jcb);
        JButton connectButton = new JButton("Connect");
        connectButton.setBounds(580, 125, 120, 30);
        panel.add(connectButton);

        inputStartIP.setText("172.16.1.1");
        inputEndIP.setText("172.16.255.255");
        /*
         *  Search点击事件
         */
        searchButton.addActionListener((ActionEvent e) -> {
            searchButton.setEnabled(false);
            consoleToGUI.clear();
            String startIP = inputStartIP.getText();
            String endIP = inputEndIP.getText();
            int portNum = Integer.parseInt(inputPortNum.getText().equals("")? "0" : inputPortNum.getText());

            /*
             *  输入校验
             */
            if (startIP.equals("")) {
                JOptionPane.showMessageDialog(null, "请输入起始IP");
                return;
            }
            else if (endIP.equals("")) {
                JOptionPane.showMessageDialog(null, "请输入结束IP");
                return;
            }
            else if (portNum == 0) {
                JOptionPane.showMessageDialog(null, "开设端口数不能为0");
                return;
            }
            if (startIP.split("\\.").length != 4) {
                JOptionPane.showMessageDialog(null, "起始IP输入错误");
                return;
            }
            else if (endIP.split("\\.").length != 4) {
                JOptionPane.showMessageDialog(null, "结束IP输入错误");
                return;
            }

            String plan = null;
            if (planA.isSelected()) plan = "A";
            else if (planB.isSelected()) plan = "B";
            else if (planC.isSelected()) plan = "C";

            /*
                开始搜索
             */
            OutputResultThread outputResultThread = new OutputResultThread();
            outputResultThread.start();
            new SearchThread(plan, startIP, endIP, portNum, timeLabel, outputResultThread).start();
        });
        /*
         *  Connect 点击事件
         */
        connectButton.addActionListener((ActionEvent e)->{
            if (jcb.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(null, "请选择可用IP地址");
                return;
            }
            try {
                Runtime.getRuntime().exec("explorer ftp://" + jcb.getSelectedItem());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
    }
}

package ui.config;

import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.*;

public class PrintStreamToGUI extends PrintStream {

    private JTextArea text;
    private StringBuffer buffer = new StringBuffer();

    public PrintStreamToGUI(OutputStream out, JTextArea text) {
        super(out);
        this.text = text;
    }

    /**
     * 在这里重截,所有的打印方法都要调用的方法
     */
    public void write(byte[] buf, int off, int len) {
        final String message = new String(buf, off, len);
        SwingUtilities.invokeLater(() -> {
            buffer.append(message);
            text.setText(buffer.toString());
        });
    }

    public void changeConsole(JTextArea console) {
        this.text = console;
    }

    public void clear () {
        buffer = new StringBuffer();
        System.gc();
        text.setText("");
    }
}
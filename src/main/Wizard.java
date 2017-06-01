package main;

import javax.swing.*;
import java.io.*;

/**
 * Created by David Szilagyi on 2017. 05. 31..
 */
public class Wizard implements Runnable {
    private String source;
    private String dest;
    private JProgressBar pb;
    long bytes;
    long rounds;
    private InputStream is = null;
    private OutputStream os = null;
    boolean running;

    public Wizard(String source, String dest, JProgressBar pb) {
        this.source = source;
        this.dest = dest;
        this.pb = pb;
    }

    public String getSource() {
        return source;
    }

    public String getDest() {
        return dest;
    }

    public JProgressBar getPb() {
        return pb;
    }

    @Override
    public void run() {
        try {
            running = true;
            is = new FileInputStream(getSource());
            os = new FileOutputStream(getDest());
            bytes = is.available();
            rounds = bytes / 1024;
            byte[] buffer = new byte[1024];
            int length;
            int round = 0;
            while ((length = is.read(buffer)) > 0 && running) {
                if(Thread.currentThread().isInterrupted())  {
                    running = false;
                }
                pb.setValue((int)(Math.ceil(((double)round / rounds) * 100)));
                os.write(buffer, 0, length);
                round++;
            }
            is.close();
            os.close();
            if(!running) {
                new File(getDest()).delete();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

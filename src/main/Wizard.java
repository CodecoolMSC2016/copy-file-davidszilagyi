package main;

import java.io.*;

/**
 * Created by David Szilagyi on 2017. 05. 31..
 */
public abstract class Wizard implements Runnable {
    private String source;
    private String dest;
    long bytes;
    long rounds;
    private InputStream is = null;
    private OutputStream os = null;
    boolean running;

    public Wizard(String source, String dest) {
        this.source = source;
        this.dest = dest;
    }

    public String getSource() {
        return source;
    }

    public String getDest() {
        return dest;
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
                changePb((int)(Math.ceil(((double)round / rounds) * 100)));
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
    public abstract void changePb(int current);
}

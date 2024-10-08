package unibo.filter;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;

public abstract class RunnableFilter extends Filter implements Runnable {

    protected final Thread t;

    public RunnableFilter(BufferedInputStream in, BufferedOutputStream out) {
        super(in, out);
        this.t = new Thread(this);
    }

    public void start() {
        this.t.start();
    }

    public void stop() {
        this.t.interrupt();
    }
    @Override
    public void run() {
        while (true) {
            try {
                forward(this.in, this.out);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}

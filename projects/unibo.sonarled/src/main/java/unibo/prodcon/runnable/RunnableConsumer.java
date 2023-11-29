package unibo.prodcon.runnable;

import java.io.InputStream;

public abstract class RunnableConsumer implements Runnable {

    protected final InputStream in;

    public RunnableConsumer(InputStream in) {
        this.in = in;
        Thread t = new Thread(this);
        t.start();
    }

    public abstract void consume(InputStream in) throws InterruptedException;

    @Override
    public void run() {
        while (true) {
            try {
                consume(this.in);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

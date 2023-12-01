package unibo.prodcon.runnable;

import unibo.prodcon.Consumer;

import java.io.BufferedInputStream;
import java.io.IOException;

public abstract class RunnableConsumer implements Runnable, Consumer {

    protected final BufferedInputStream in;

    protected final Thread t;

    public RunnableConsumer(BufferedInputStream in) {
        this.in = in;
        this.t = new Thread(this);
        this.t.start();
    }

    public abstract void consume(BufferedInputStream in) throws InterruptedException, IOException;

    @Override
    public void run() {
        while (true) {
            try {
                consume(this.in);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

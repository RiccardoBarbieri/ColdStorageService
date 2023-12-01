package unibo.prodcon.runnable;

import unibo.prodcon.Producer;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public abstract class RunnableProducer implements Runnable, Producer {

    protected final BufferedOutputStream out;

    protected final Thread t;

    public RunnableProducer(BufferedOutputStream out) {
        this.out = out;
        this.t = new Thread(this);
        this.t.start();
    }

//    public void startProducer() {
//        t.start();
//    }
//
//    public void stopProducer() {
//        t.interrupt();
//    }

    public abstract void produce(BufferedOutputStream out) throws InterruptedException, IOException;

    @Override
    public void run() {
        while (true) {
            try {
                produce(this.out);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

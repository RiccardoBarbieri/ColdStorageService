package unibo.prodcon.runnable;

import java.io.IOException;
import java.io.OutputStream;

public abstract class RunnableProducer implements Runnable {

    protected final OutputStream out;

    protected final Thread t;

    public RunnableProducer(OutputStream out) {
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

    public abstract void produce(OutputStream out) throws InterruptedException, IOException;

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

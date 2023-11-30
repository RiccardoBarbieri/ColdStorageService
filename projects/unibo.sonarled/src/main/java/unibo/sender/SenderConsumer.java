package unibo.sender;

import unibo.prodcon.runnable.RunnableConsumer;

import java.io.BufferedInputStream;
import java.io.IOException;

public class SenderConsumer extends RunnableConsumer {


    public SenderConsumer(BufferedInputStream in) {
        super(in);
    }

    @Override
    public void consume(BufferedInputStream in) throws IOException {
        byte[] buffer = new byte[1024];
        int read = in.read(buffer);
        if (read > 0) {
            System.out.println("Received: " + new String(buffer, 0, read));
        }
    }
}

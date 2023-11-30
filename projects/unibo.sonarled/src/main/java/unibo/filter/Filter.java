package unibo.filter;

import unibo.prodcon.Consumer;
import unibo.prodcon.Producer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;

public abstract class Filter {

    protected final BufferedInputStream in;

    protected final BufferedOutputStream out;

    public abstract boolean filter(String message);

    public void forward(BufferedInputStream in, BufferedOutputStream out) throws InterruptedException, IOException {
        byte[] buffer = new byte[1024];
        int read = in.read(buffer);
        if (read > 0) {
            if (!filter(new String(buffer, 0, read))) {
                out.write(buffer, 0, read);
                out.flush();
            }
        }
    }

    public Filter(BufferedInputStream in, BufferedOutputStream out) {
        this.in = in;
        this.out = out;
    }

}

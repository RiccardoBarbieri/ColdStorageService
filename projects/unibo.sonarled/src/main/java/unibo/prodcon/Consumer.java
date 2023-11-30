package unibo.prodcon;

import java.io.BufferedInputStream;
import java.io.IOException;

public interface Consumer {

    void consume(BufferedInputStream in) throws InterruptedException, IOException;
}

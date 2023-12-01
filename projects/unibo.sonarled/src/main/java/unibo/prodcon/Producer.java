package unibo.prodcon;

import java.io.BufferedOutputStream;
import java.io.IOException;

public interface Producer {

    void produce(BufferedOutputStream out) throws InterruptedException, IOException;
}

package unibo.connection;

import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import unibo.basicomm23.utils.CommUtils;
import unibo.prodcon.Producer;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class CoapObserver implements CoapHandler, Producer {
    private static final String className = "CoapObserver";
    private static final Logger logger = LoggerFactory.getLogger(CoapObserver.class);

    private BufferedOutputStream out;

    private CoapResponse response;

    public CoapObserver(BufferedOutputStream out) {
        this.out = out;
    }

    @Override
    public void onLoad(CoapResponse response) {
        logger.debug("onLoad | received update " + response.getResponseText());

        this.response = response;

        try {
            produce(this.out);
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onError() {
        logger.error("onError | error");
    }

    @Override
    public void produce(BufferedOutputStream out) throws InterruptedException, IOException {
//        out.write(this.response.getResponseText().getBytes());
//        out.flush();
//        System.out.println("CoapObserver produce : " + this.response.toString());
        System.out.println("______________CoapObserver | Received: " + this.response.getResponseText());
        OutputStreamWriter writer = new OutputStreamWriter(out);
        BufferedWriter bufferedWriter = new BufferedWriter(writer);
        bufferedWriter.write(this.response.toString());
        bufferedWriter.newLine();
        bufferedWriter.flush();
    }
}

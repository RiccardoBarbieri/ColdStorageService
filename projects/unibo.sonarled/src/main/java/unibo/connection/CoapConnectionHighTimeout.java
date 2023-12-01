package unibo.connection;

import org.eclipse.californium.core.CoapClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import unibo.basicomm23.coap.CoapConnection;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.utils.Connection;


public class CoapConnectionHighTimeout extends CoapConnection {

    private static final Logger logger = LoggerFactory.getLogger(CoapConnectionHighTimeout.class);

    public CoapConnectionHighTimeout(String address, String path) {
        super(address, path);
        setCoapClient(address, path);
    }

    @Override
    protected void setCoapClient(String addressWithPort, String path) {
        logger.debug("setCoapClient addressWithPort=" + addressWithPort);
        this.url = "coap://" + addressWithPort + "/" + path;
        this.client = new CoapClient(url);
        this.client.useExecutor(); //To be shutdown
        this.client.setTimeout(15000L);
    }
}
/*
Log4j by default looks for a file called log4j.properties or log4j.xml on the classpath
System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "ERROR");
*/

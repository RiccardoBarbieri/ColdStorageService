package unibo.springSAGSim.connection;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.elements.exception.ConnectorException;
import unibo.basicomm23.coap.CoapConnection;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.utils.Connection;

import java.io.IOException;


public class CoapConnectionHighTimeout extends CoapConnection {

    public CoapConnectionHighTimeout(String address, String path) {
        super(address, path);
        setCoapClient(address, path);
    }

    @Override
    protected void setCoapClient(String addressWithPort, String path) {
        //CommUtils.outmagenta(  "    +++ CoapConn | setCoapClient addressWithPort=" +  addressWithPort  );
        //url            = "coap://"+address + ":5683/"+ path;
        this.url = "coap://" + addressWithPort + "/" + path;
        if (Connection.trace) CommUtils.outyellow("    +++ CoapConn | setCoapClient url=" + url);
        this.client = new CoapClient(url);
        this.client.useExecutor(); //To be shutdown
        if (Connection.trace)
            CommUtils.outyellow("    +++ CoapConn | STARTS client url=" + url); //+ " client=" + client );
        this.client.setTimeout(15000L);
    }
}
/*
Log4j by default looks for a file called log4j.properties or log4j.xml on the classpath
System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "ERROR");
*/

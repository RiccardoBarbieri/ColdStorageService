package unibo.connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedOutputStream;

public class ObserverSetup {
    private static final Logger logger = LoggerFactory.getLogger(ObserverSetup.class);

    private final CoapConnectionHighTimeout observerConn;
    private final BufferedOutputStream out;
    private ConnectionUtilsCoap connection;

    public ObserverSetup(ConnectionUtilsCoap connection, BufferedOutputStream out) {
        this.out = out;
        this.connection = connection;
        this.observerConn = this.connection.connectLocalActorUsingCoap();
    }

    public void startObserving() {
        logger.debug("startObserving | observerConn: " + observerConn);
        this.observerConn.observeResource(new CoapObserver(this.out));
    }
}

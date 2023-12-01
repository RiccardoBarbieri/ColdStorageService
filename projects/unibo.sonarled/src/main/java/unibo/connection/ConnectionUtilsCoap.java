package unibo.connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.mqtt.MqttConnection;
import unibo.basicomm23.utils.CommUtils;

public class ConnectionUtilsCoap extends ConnectionUtils {

    private static final String className = "ConnectionUtilsCoap";
    private static final Logger logger = LoggerFactory.getLogger(ConnectionUtilsCoap.class);
    String actorCtx = "ctx_coldstorageservice";
    String actorName = "coldstorageservice";
    String actorCtxPort = "8021";
    String actorCtxHost = "192.168.1.20";
//    String actorCtxHost = "localhost";

    public CoapConnectionHighTimeout connectLocalActorUsingCoap() {
        return connectActorUsingCoap(actorCtxHost, actorCtxPort, actorCtx, actorName);
    }

    public void forwardDistance(Interaction conn, String distance) {
        String functionName = "sendDistance";
        try {
            String msg = "" + CommUtils.buildDispatch("sonar", "distance", "distance(" + distance + ")", actorName);
            sendDispatch(conn, msg);
        } catch (Exception e) {
            logger.error("forwardDistance | ERROR: " + e.getMessage());
        }
    }

    private void sendDispatch(Interaction conn, String msg) {
        try {
            logger.debug("sendDispatch | msg:" + msg + ", conn: " + conn);
            conn.forward(msg);
        } catch (Exception e) {
            logger.error("sendDispatch | ERROR: " + e.getMessage());
        }
    }
}

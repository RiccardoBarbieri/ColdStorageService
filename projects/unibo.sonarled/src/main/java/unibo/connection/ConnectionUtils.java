package unibo.connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.mqtt.MqttConnection;
import unibo.basicomm23.utils.CommSystemConfig;
import unibo.basicomm23.utils.CommUtils;

public class ConnectionUtils {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionUtils.class);
    private static final String className = "ConnectionUtils";
    private static Interaction conn;

    public static CoapConnectionHighTimeout connectActorUsingCoap(String actorCtxAddr, String actorCtxPort, String actorCtx, String actorName) {
        try {
            CommSystemConfig.tracing = true;
            String path = actorCtx + "/" + actorName;

            conn = new CoapConnectionHighTimeout(actorCtxAddr + ":" + actorCtxPort, path);
            logger.debug("connectActorUsingCoap | connected to actor, conn:" + conn);
        } catch (Exception e) {
            logger.error("connectActorUsingCoap | ERROR: " + e.getMessage());
        }
        return (CoapConnectionHighTimeout) conn;
    }

    public static MqttConnection connectActorUsingMqtt(String brokerAddress, String topic) {
        try {
            CommSystemConfig.tracing = true;

            conn = MqttConnection.create("controller", brokerAddress, topic);
            logger.debug("connectActorUsingMqtt | connected to actor, conn:" + conn);
        } catch (Exception e) {
            logger.error("connectActorUsingMqtt | ERROR: " + e.getMessage());
        }
        return (MqttConnection) conn;
    }

}

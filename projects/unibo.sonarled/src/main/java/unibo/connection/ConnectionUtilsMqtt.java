package unibo.connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import unibo.basicomm23.mqtt.MqttConnection;
import unibo.basicomm23.utils.CommUtils;

public class ConnectionUtilsMqtt extends ConnectionUtils {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionUtilsMqtt.class);
    private static final String className = "ConnectionUtilsMqtt";

    private static final String brokerAddress = "tcp://mqtt.eclipseprojects.io";

    private static final String topic = "unibo/sonar/events";

    public MqttConnection connectLocalActorUsingMqtt() {
        return connectActorUsingMqtt(brokerAddress, topic);
    }

    public void emitDistance(MqttConnection conn, String distance) {
        String functionName = "emitDistance";
        try {
            String msg = "" + CommUtils.buildEvent("sonar", "distance", "distance(" + distance + ")");
            emitEvent(conn, msg, functionName);
        } catch (Exception e) {
            logger.error("emitDistance | ERROR: " + e.getMessage());
        }
    }

    private void emitEvent(MqttConnection conn, String msg, String functionName) {
        try {
            System.out.println(className + " " + functionName + " | msg:" + msg + ", conn: " + conn);
            logger.debug("emitEvent | msg:" + msg + ", conn: " + conn);
            conn.publish(topic, msg);
        } catch (Exception e) {
            logger.error("emitEvent | ERROR: " + e.getMessage());
        }
    }
}

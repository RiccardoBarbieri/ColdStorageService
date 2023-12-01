package unibo.sender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import unibo.basicomm23.coap.CoapConnection;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.mqtt.MqttConnection;
import unibo.connection.ConnectionUtilsMqtt;
import unibo.prodcon.runnable.RunnableConsumer;
import org.eclipse.californium.elements.exception.ConnectorException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MqttControllerConsumer extends RunnableConsumer {
    private static final Logger logger = LoggerFactory.getLogger(MqttControllerConsumer.class);

	private ConnectionUtilsMqtt connection;
    private Interaction msgConn;

    public MqttControllerConsumer(BufferedInputStream in, ConnectionUtilsMqtt connection) {
        super(in);
        this.connection = connection;

        this.msgConn = connection.connectLocalActorUsingMqtt();
    }

    @Override
    public void consume(BufferedInputStream in) throws IOException, InterruptedException {
        logger.trace("consume | consuming");
        InputStreamReader reader = new InputStreamReader(in);
        BufferedReader br = new BufferedReader(reader);
        String line = br.readLine();
        if (line != null) {
//            System.out.println("Received: " + line);
            connection.emitDistance((MqttConnection) this.msgConn, line);
        }
    }
}

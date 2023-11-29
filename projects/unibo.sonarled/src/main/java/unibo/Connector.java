package unibo;


import unibo.basicomm23.coap.CoapConnection;
import unibo.basicomm23.interfaces.Interaction;
import unibo.connection.CoapObserver;
import unibo.connection.Connection;
import unibo.led.Led;
import unibo.sonar.Sonar;

public class Connector {

	private Connection connection;
    private CoapConnection observerConn;
    private Interaction requestConn;

    public Connector(Connection connection, Sonar sonarProducer, Led led, SenderConsumer senderConsumer) {
        this.connection = connection;

        this.observerConn = connection.connectLocalActorUsingCoap();
        this.observerConn.observeResource(new CoapObserver());

        this.requestConn = connection.connectLocalActorUsingCoap();


    }

    public static void main(String args[]) {
        System.out.println("ASd");
    }

}

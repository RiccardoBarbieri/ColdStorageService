package unibo.led;

import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.gpio.digital.DigitalOutputConfig;
import com.pi4j.io.gpio.digital.DigitalOutputConfigBuilder;
import com.pi4j.io.gpio.digital.DigitalState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import unibo.prodcon.runnable.RunnableConsumer;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LedControllerConsumer extends RunnableConsumer implements Led {
    private static final Logger logger = LoggerFactory.getLogger(LedControllerConsumer.class);

    public final Integer LED_PIN = 18;
    private final DigitalOutput ledPin;
    private Thread ledThread;

    public LedControllerConsumer(BufferedInputStream in) {
        super(in);
        Context pi4jContext = com.pi4j.Pi4J.newAutoContext();

        DigitalOutputConfigBuilder ledPinConfig = DigitalOutputConfig.newBuilder(pi4jContext)
                .id("led")
                .address(LED_PIN)
                .initial(DigitalState.LOW)
                .shutdown(DigitalState.LOW)
                .provider("pigpio-digital-output");
        this.ledPin = pi4jContext.create(ledPinConfig);
        logger.debug("LedControllerConsumer | ledPin created");
    }

    @Override
    public void turnOn() throws InterruptedException {
        if (this.ledThread != null) {
            this.ledThread.stop();
        }
        logger.debug("turnOn | turningOn");
        System.out.println("LedControllerConsumer | turningOn");
        this.ledPin.high();
    }

    @Override
    public void turnOff() throws InterruptedException {
        if (this.ledThread != null) {
            this.ledThread.stop();
        }
        logger.debug("turnoOff | turningOff");
        System.out.println("LedControllerConsumer | turningOff");
        this.ledPin.low();
    }

    @Override
    public void blink(int times, int milliseconds) throws InterruptedException {
        if (times < 0) {
            this.ledThread = new Thread(() -> {
                logger.debug("blink | blinking");
                while (true) {
                    try {
                        this.ledPin.high();
                        Thread.sleep(milliseconds);
                        this.ledPin.low();
                        Thread.sleep(milliseconds);
                    } catch (InterruptedException e) {
                        System.out.println("LedControllerConsumer | blink | interrupted");
                    }
                }
            });
            this.ledThread.start();
        } else {
            for (int i = 0; i < times; i++) {
                turnOn();
                Thread.sleep(milliseconds);
                turnOff();
                Thread.sleep(milliseconds);
            }
        }
    }

    @Override
    public void consume(BufferedInputStream in) throws InterruptedException, IOException {
        logger.trace("consume | reading from input stream");
        InputStreamReader reader = new InputStreamReader(in);
        BufferedReader br = new BufferedReader(reader);
        String line = br.readLine();
        String msg = line.split("\\(")[1].split("\\)")[0];
        System.out.println("______________LedController | Received: " + msg);
        if (msg != null) {
            if (msg.contains("home")) {
                turnOff();
            } else if (msg.contains("stopped")) {
                turnOn();
            } else if (msg.contains("moving")) {
                blink(-1, 500);
            }
        }
    }
}

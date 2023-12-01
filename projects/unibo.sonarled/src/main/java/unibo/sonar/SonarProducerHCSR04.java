package unibo.sonar;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import unibo.prodcon.runnable.RunnableProducer;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class SonarProducerHCSR04 extends RunnableProducer implements Sonar {
    private static final Logger logger = LoggerFactory.getLogger(SonarProducerHCSR04.class);

    public final Integer TRIGGER_PIN = 14;
    public final Integer ECHO_PIN = 15;

    private final long delay;

    private final DigitalOutput triggerPin;
    private final DigitalInput echoPin;

    public SonarProducerHCSR04(BufferedOutputStream out, long delay) {
        super(out);
        this.delay = delay;
        Context pi4jContext = Pi4J.newAutoContext();

        DigitalOutputConfigBuilder triggerPinConfig = DigitalOutputConfig.newBuilder(pi4jContext)
                .id("trigger")
                .address(TRIGGER_PIN)
                .initial(DigitalState.LOW)
                .shutdown(DigitalState.LOW)
                .provider("pigpio-digital-output");
        this.triggerPin = pi4jContext.create(triggerPinConfig);

        DigitalInputConfigBuilder echoPinConfig = DigitalInputConfig.newBuilder(pi4jContext)
                .id("echo")
                .address(ECHO_PIN)
                .provider("pigpio-digital-input");
        this.echoPin = pi4jContext.create(echoPinConfig);
        logger.debug("SonarProducerHCSR04 | triggerPin and echoPin created");
    }

    public Float getDistance() throws InterruptedException {

        logger.debug("getDistance | sampling distance from sonar");

        triggerPin.high();
        Thread.sleep(0, 10000);
        triggerPin.low();

        while (echoPin.isLow()) {
            continue;
        }
        Long startTime = System.nanoTime();
        while (echoPin.isHigh()) {
            continue;
        }
        Long endTime = System.nanoTime();

        long duration = endTime - startTime;

        BigDecimal halfDuration = BigDecimal.valueOf(duration).divide(BigDecimal.valueOf(2), 1, RoundingMode.DOWN);
        BigDecimal seconds = halfDuration.divide(BigDecimal.valueOf(1000000000), 9, RoundingMode.DOWN);
        return BigDecimal.valueOf(34300).multiply(seconds).floatValue();
    }

    @Override
    public void produce(BufferedOutputStream out) throws InterruptedException, IOException {
        logger.trace("produce | producing");
        OutputStreamWriter writer = new OutputStreamWriter(out);
        BufferedWriter bufferedWriter = new BufferedWriter(writer);
        bufferedWriter.write(this.getDistance().toString().strip());
        bufferedWriter.newLine();
        bufferedWriter.flush();
    }

    @Override
    public void run() {
        while (true) {
            try {
                produce(this.out);
                Thread.sleep(this.delay);
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}

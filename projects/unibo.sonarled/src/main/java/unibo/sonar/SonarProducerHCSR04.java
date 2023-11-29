package unibo.sonar;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.*;
import unibo.prodcon.runnable.RunnableProducer;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class SonarProducerHCSR04 extends RunnableProducer implements Sonar {

    public final Integer TRIGGER_PIN = 14;
    public final Integer ECHO_PIN = 15;

    private final long delay;

    public SonarProducerHCSR04(OutputStream out, long delay) {
        super(out);
        this.delay = delay;
    }

    public Float getDistance() throws InterruptedException {
//        Context pi4jContext = Pi4J.newAutoContext();
//
//        DigitalOutputConfigBuilder triggerPinConfig = DigitalOutputConfig.newBuilder(pi4jContext)
//                .id("trigger")
//                .address(TRIGGER_PIN)
//                .initial(DigitalState.LOW)
//                .shutdown(DigitalState.LOW)
//                .provider("pigpio-digital-output");
//        DigitalOutput triggerPin = pi4jContext.create(triggerPinConfig);
//
//        DigitalInputConfigBuilder echoPinConfig = DigitalInputConfig.newBuilder(pi4jContext)
//                .id("echo")
//                .address(ECHO_PIN)
//                .provider("pigpio-digital-input");
//        DigitalInput echoPin = pi4jContext.create(echoPinConfig);
//
//        triggerPin.high();
//        Thread.sleep(0, 10000);
//        triggerPin.low();

//        while (echoPin.isLow()) {
//            continue;
//        }
        Long startTime = System.nanoTime();
//        while (echoPin.isHigh()) {
//            continue;
//        }
        Thread.sleep(2934);
        Long endTime = System.nanoTime();

        long duration = endTime - startTime;

        BigDecimal halfDuration = BigDecimal.valueOf(duration).divide(BigDecimal.valueOf(2), 1, RoundingMode.DOWN);
        BigDecimal seconds = halfDuration.divide(BigDecimal.valueOf(1000000000), 9, RoundingMode.DOWN);
        return BigDecimal.valueOf(34300).multiply(seconds).floatValue();
    }

    @Override
    public void produce(OutputStream out) throws InterruptedException, IOException {
        out.write(this.getDistance().toString().getBytes());
        out.flush();
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

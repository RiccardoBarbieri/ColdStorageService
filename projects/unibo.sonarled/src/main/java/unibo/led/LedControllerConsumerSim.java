package unibo.led;

import unibo.prodcon.runnable.RunnableConsumer;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LedControllerConsumerSim extends RunnableConsumer implements Led{
    public LedControllerConsumerSim(BufferedInputStream in) {
        super(in);
    }

    @Override
    public void turnOn() throws InterruptedException {
        System.out.println("LedControllerConsumerSim: turnOn");

    }

    @Override
    public void turnOff() throws InterruptedException {
        System.out.println("LedControllerConsumerSim: turnOff");
    }

    @Override
    public void blink(int times, int milliseconds) throws InterruptedException {
        System.out.println("LedControllerConsumerSim: blink");
    }

    @Override
    public void consume(BufferedInputStream in) throws InterruptedException, IOException {
        InputStreamReader reader = new InputStreamReader(in);
        BufferedReader br = new BufferedReader(reader);
        String line = br.readLine();
        String msg = line.split("\\(")[1].split("\\)")[0];
        if (msg != null) {
            if (msg.contains("home")) {
                turnOff();
            }
            else if (msg.contains("stopped")) {
                turnOn();
            }
            else if (msg.contains("moving")) {
                blink(-1, 500);
            }
        }
    }
}

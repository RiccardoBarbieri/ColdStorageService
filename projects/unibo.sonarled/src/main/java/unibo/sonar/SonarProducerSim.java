package unibo.sonar;

import unibo.prodcon.runnable.RunnableProducer;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class SonarProducerSim extends RunnableProducer implements Sonar {

    private final long delay;
    public SonarProducerSim(BufferedOutputStream out, long delay) {
        super(out);
        this.delay = delay;
    }

    @Override
    public Float getDistance() {
        return 42f;
    }

    @Override
    public void produce(BufferedOutputStream out) throws InterruptedException, IOException {
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

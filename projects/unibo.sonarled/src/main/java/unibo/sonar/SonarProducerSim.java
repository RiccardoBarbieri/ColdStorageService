package unibo.sonar;

import java.io.BufferedOutputStream;

public class SonarProducerSim extends SonarProducerHCSR04 {
    public SonarProducerSim(BufferedOutputStream out, long delay) {
        super(out, delay);
    }

    @Override
    public Float getDistance() {
        return 42f;
    }
}

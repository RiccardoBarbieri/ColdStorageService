package unibo.sonar;

import java.io.BufferedOutputStream;

public class SonarStupid extends SonarProducerHCSR04 {
    public SonarStupid(BufferedOutputStream out, long delay) {
        super(out, delay);
    }

    @Override
    public Float getDistance() {
        return 42f;
    }
}

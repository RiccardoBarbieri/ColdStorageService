package unibo.filter;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;

public class SonarrThresholdFilter extends RunnableFilter {

    private final Float threshold;
    public SonarrThresholdFilter(BufferedInputStream in, BufferedOutputStream out, Float threshold) {
        super(in, out);
        this.threshold = threshold;
    }

    @Override
    public boolean filter(String message) {
        return false;
    }
}

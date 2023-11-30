package unibo.filter;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;

public class SonarCleanerFilter extends RunnableFilter {

    private final Float low;
    private final Float high;

    public SonarCleanerFilter(BufferedInputStream in, BufferedOutputStream out, Float low, Float high) {
        super(in, out);
        this.low = low;
        this.high = high;

    }


    @Override
    public boolean filter(String message) {
        Float value = Float.parseFloat(message);
        return value < low || value > high;
    }
}

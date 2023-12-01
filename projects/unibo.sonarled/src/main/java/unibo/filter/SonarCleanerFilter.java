package unibo.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class SonarCleanerFilter extends RunnableFilter {
    private static final Logger logger = LoggerFactory.getLogger(SonarCleanerFilter.class);

    private final Float low;
    private final Float high;

    public SonarCleanerFilter(BufferedInputStream in, BufferedOutputStream out, Float low, Float high) {
        super(in, out);
        this.low = low;
        this.high = high;

    }

    @Override
    public void forward(BufferedInputStream in, BufferedOutputStream out) throws InterruptedException, IOException {
        OutputStreamWriter writer = new OutputStreamWriter(out);
        BufferedWriter bufferedWriter = new BufferedWriter(writer);
        InputStreamReader reader = new InputStreamReader(in);
        BufferedReader bufferedReader = new BufferedReader(reader);

        String line = bufferedReader.readLine();
        if (line != null) {
            if (!filter(line)) {
                bufferedWriter.write(line);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
            else if (Float.parseFloat(line) > high) {
                bufferedWriter.write(Float.toString(high));
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
            else if (Float.parseFloat(line) < low) {
                bufferedWriter.write(Float.toString(low));
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        }

    }


    @Override
    public boolean filter(String message) {
        logger.debug("filter | " + message);
        Float value = Float.parseFloat(message);
        return value < low || value > high;
    }
}

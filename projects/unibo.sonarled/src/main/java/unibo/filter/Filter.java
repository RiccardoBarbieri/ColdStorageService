package unibo.filter;

import unibo.prodcon.Consumer;
import unibo.prodcon.Producer;

import java.io.*;

public abstract class Filter {

    protected final BufferedInputStream in;

    protected final BufferedOutputStream out;

    public abstract boolean filter(String message);

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
        }

    }

    public Filter(BufferedInputStream in, BufferedOutputStream out) {
        this.in = in;
        this.out = out;
    }

}

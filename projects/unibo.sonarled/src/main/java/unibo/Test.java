package unibo;

import unibo.sonar.SonarProducerHCSR04;
import unibo.sonar.SonarStupid;

import java.io.*;

public class Test {

    public static void main(String[] args) throws IOException {
        System.out.println("Starting...");

        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream();
        out.connect(in);

        SonarStupid sonar = new SonarStupid(new BufferedOutputStream(out), 1000);
        SenderConsumer sender = new SenderConsumer(new BufferedInputStream(in));


        
    }
}

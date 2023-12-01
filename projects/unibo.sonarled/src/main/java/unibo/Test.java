package unibo;

import unibo.filter.RunnableFilter;
import unibo.filter.SonarCleanerFilter;
import unibo.prodcon.runnable.RunnableConsumer;
import unibo.prodcon.runnable.RunnableProducer;
import unibo.sender.SenderConsumer;
import unibo.sonar.Sonar;
import unibo.sonar.SonarStupid;

import java.io.*;
import java.nio.channels.Pipe;

public class Test {

    public static void main(String[] args) throws IOException {
        System.out.println("Starting...");

        PipedOutputStream sonarToCleaner = new PipedOutputStream();
        PipedInputStream cleanerFromSonar = new PipedInputStream();
        PipedOutputStream cleanerToSender = new PipedOutputStream();
        PipedInputStream senderFromCleaner = new PipedInputStream();

        sonarToCleaner.connect(cleanerFromSonar);
        cleanerToSender.connect(senderFromCleaner);

        RunnableProducer sonar = new SonarStupid(new BufferedOutputStream(sonarToCleaner), 1000);
        RunnableFilter filter = new SonarCleanerFilter(new BufferedInputStream(cleanerFromSonar), new BufferedOutputStream(cleanerToSender), 2f, 170f);

        RunnableConsumer sender = new SenderConsumer(new BufferedInputStream(senderFromCleaner));


        
    }
}

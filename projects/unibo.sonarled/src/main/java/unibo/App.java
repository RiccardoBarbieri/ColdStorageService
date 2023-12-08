package unibo;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import unibo.connection.ConnectionUtilsCoap;
import unibo.connection.ConnectionUtilsMqtt;
import unibo.connection.ObserverSetup;
import unibo.filter.LedUpdatesFilter;
import unibo.filter.RunnableFilter;
import unibo.filter.SonarCleanerFilter;
import unibo.led.LedControllerConsumer;
import unibo.prodcon.runnable.RunnableConsumer;
import unibo.prodcon.runnable.RunnableProducer;
import unibo.sender.MqttControllerConsumer;
import unibo.sonar.SonarProducerHCSR04;

import java.io.*;

public class App {

    public static void main(String[] args) throws IOException, InterruptedException {

        Configurator.setRootLevel(Level.WARN);


        System.out.println("Starting...");

        PipedOutputStream sonarToCleaner = new PipedOutputStream();
        PipedInputStream cleanerFromSonar = new PipedInputStream();
        PipedOutputStream cleanerToSender = new PipedOutputStream();
        PipedInputStream senderFromCleaner = new PipedInputStream();

        PipedOutputStream observerToFilter = new PipedOutputStream();
        PipedInputStream filterFromObserver = new PipedInputStream();
        PipedOutputStream filterToLed = new PipedOutputStream();
        PipedInputStream ledFromFilter = new PipedInputStream();

        // Connect the pipes
        sonarToCleaner.connect(cleanerFromSonar);
        cleanerToSender.connect(senderFromCleaner);

        observerToFilter.connect(filterFromObserver);
        filterToLed.connect(ledFromFilter);

        // Setup the observer
        ObserverSetup observerSetup = new ObserverSetup(new ConnectionUtilsCoap(), new BufferedOutputStream(observerToFilter));

        // Setup led updates filter
        RunnableFilter ledUpdatesFilter = new LedUpdatesFilter(new BufferedInputStream(filterFromObserver), new BufferedOutputStream(filterToLed), "ttstate");

        // Setup led controller
        RunnableConsumer ledController = new LedControllerConsumer(new BufferedInputStream(ledFromFilter));

        // Setup sonar
        RunnableProducer sonar = new SonarProducerHCSR04(new BufferedOutputStream(sonarToCleaner), 1000);

        // Setup sonar cleaner filter
        RunnableFilter sonarCleanerFilter = new SonarCleanerFilter(new BufferedInputStream(cleanerFromSonar), new BufferedOutputStream(cleanerToSender), 2f, 170f);

        // Setup sender
        MqttControllerConsumer sender = new MqttControllerConsumer(new BufferedInputStream(senderFromCleaner), new ConnectionUtilsMqtt());

        // Start all the threads
        Thread.sleep(2000);
        observerSetup.startObserving();
        sonar.start();
        sonarCleanerFilter.start();
        sender.start();
        ledUpdatesFilter.start();
        ledController.start();
    }
}

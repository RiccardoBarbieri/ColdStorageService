package unibo.led;

public interface Led {

    void turnOn() throws InterruptedException;
    void turnOff() throws InterruptedException;

    void blink(int times, int milliseconds) throws InterruptedException;
}

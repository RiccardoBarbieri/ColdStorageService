package unibo.led;

public class LedInfiniteBlink implements Led {

    private Thread t;

    @Override
    public void turnOn() throws InterruptedException {
        if (this.t != null) {
            this.t.interrupt();
        }

    }

    @Override
    public void turnOff() throws InterruptedException {
        if (this.t != null) {
            this.t.interrupt();
        }

    }

    @Override
    public void blink(int times, int milliseconds) throws InterruptedException {
        if (times < 0) {
            this.t = new Thread(() -> {
                while (true) {
                    try {
                        turnOn();
                        Thread.sleep(milliseconds);
                        turnOff();
                        Thread.sleep(milliseconds);
                    } catch (InterruptedException ignored) {
                    }
                }
            });
            this.t.start();
        }
        else {
            for (int i = 0; i < times; i++) {
                turnOn();
                Thread.sleep(milliseconds);
                turnOff();
                Thread.sleep(milliseconds);
            }
        }
    }
}

package wsdfhjxc.taponium.engine;

public abstract class TimedHandler {
    private final long interval;
    private long lastTime;
    private long delay, _delay; // _delay is used to store last delay value for the FPS counter
    private boolean highPriority;

    public TimedHandler(int frequency, boolean highPriority) {
        interval = delay = 1000 / frequency;
        this.highPriority = highPriority;
    }

    protected abstract void handle();

    public void poll() {
        while (delay >= interval) {
            _delay = delay;
            delay = highPriority ? delay - interval : 0;
            lastTime = System.currentTimeMillis() - delay;

            handle();
        }

        delay = System.currentTimeMillis() - lastTime;
    }

    public void resetDelay() {
        lastTime = System.currentTimeMillis();
        delay = 0;
    }

    public double getDeltaTime() {
        return interval / 1000.0;
    }

    public double getAlpha() {
        return (double) delay / interval;
    }

    public double getFPS() {
        return 1000.0 / _delay;
    }
}
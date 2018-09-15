package wsdfhjxc.taponium.game;

public class ScoreCounter {
    private long current = 0;
    private long max = 0;

    public void add(long value) {
        current += value;
        max = current > max ? current : max;
    }

    public long getCurrent() {
        return current;
    }

    public long getMax() {
        return max;
    }

    public boolean isNegative() {
        return current < 0;
    }

    public boolean isBelowMax() {
        return current < max;
    }
}
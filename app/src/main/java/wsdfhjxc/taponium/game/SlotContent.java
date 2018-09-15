package wsdfhjxc.taponium.game;

public class SlotContent {
    private SlotContentType type;
    private double chance;
    private double minDuration, maxDuration;

    public SlotContent(SlotContentType type, double chance, double minDuration, double maxDuration) {
        set(type, chance, minDuration, maxDuration);
    }

    public final void set(SlotContentType type, double chance, double minDuration, double maxDuration) {
        setType(type);
        setChance(chance);
        setMinDuration(minDuration);
        setMaxDuration(maxDuration);
    }

    public final void setType(SlotContentType type) {
        this.type = type;
    }

    public final void setChance(double chance) {
        this.chance = chance;
    }

    public final void setMinDuration(double minDuration) {
        this.minDuration = minDuration;
    }

    public final void setMaxDuration(double maxDuration) {
        this.maxDuration = maxDuration;
    }

    public SlotContentType getType() {
        return type;
    }

    public double getOccurrenceChance() {
        return chance;
    }

    public double getOccurrenceThreshold() {
        return 1.0 - chance;
    }

    public double getMinDuration() {
        return minDuration;
    }

    public double getMaxDuration() {
        return maxDuration;
    }
}
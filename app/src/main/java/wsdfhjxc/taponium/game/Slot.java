package wsdfhjxc.taponium.game;

public class Slot {
    private SlotContentType contentType;
    private double totalDuration = 0.0;
    private double currentDuration = 0.0;
    private double _currentDuration = 0.0; // previous value which is used for interpolation

    private final Board board;

    public Slot(Board board) {
        this.board = board;
    }

    public void set(SlotContentType contentType, double totalDuration) {
        setContentType(contentType);
        setTotalDuration(totalDuration);
        this.currentDuration = 0.0;
        this._currentDuration = 0.0;
    }

    public final void setContentType(SlotContentType contentType) {
        this.contentType = contentType;
    }

    public final void setTotalDuration(double totalDuration) {
        this.totalDuration = totalDuration;
    }

    public void scaleDuration(double durationScale) {
        this.totalDuration *= durationScale;
        this.currentDuration *= durationScale;
        this._currentDuration *= durationScale;
    }

    public void update(double deltaTime) {
        if (contentType != null) {
            _currentDuration = currentDuration;
            currentDuration += deltaTime;

            if (currentDuration >= totalDuration) {
                board.slotTotalDurationPassed(this);

                contentType = null;
                currentDuration = 0.0;
                _currentDuration = 0.0;
            }
        }
    }

    public boolean isFree() {
        return contentType == null;
    }

    public SlotContentType getContentType() {
        return contentType;
    }

    public double getDurationRatio() {
        return currentDuration / totalDuration;
    }

    public double getInterpolatedDurationRatio(double alpha) {
        double interpolatedDuration = (1.0 - alpha) * _currentDuration + alpha * currentDuration;
        return interpolatedDuration / totalDuration;
    }
}
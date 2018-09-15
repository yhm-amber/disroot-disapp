package wsdfhjxc.taponium.engine;

public class RenderHandler extends TimedHandler {
    private final ApplicationView applicationView;

    public RenderHandler(ApplicationView applicationView, int frequency) {
        super(frequency, false);
        this.applicationView = applicationView;
    }

    @Override
    protected void handle() {
        applicationView.update();
    }
}
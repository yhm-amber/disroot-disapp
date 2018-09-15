package wsdfhjxc.taponium.engine;

public class UpdateHandler extends TimedHandler {
    private final SceneKeeper sceneKeeper;
    private final double deltaTime;

    public UpdateHandler(SceneKeeper sceneKeeper, int frequency) {
        super(frequency, true);
        this.sceneKeeper = sceneKeeper;
        deltaTime = getDeltaTime();
    }

    @Override
    public void handle() {
        for (Scene scene : sceneKeeper.scenes) {
            scene.handleUpdate(deltaTime);
        }
    }
}
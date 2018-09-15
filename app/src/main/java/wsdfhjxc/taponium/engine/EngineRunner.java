package wsdfhjxc.taponium.engine;

import android.app.*;
import android.os.*;

import wsdfhjxc.taponium.scenes.*;

public class EngineRunner implements Runnable {
    private boolean running = false;
    private boolean paused = false;
    private final Thread runnerThread = new Thread(this);
    private final Activity activity;

    private final SceneKeeper sceneKeeper;
    private final ResourceKeeper resourceKeeper;

    private final InputHandler inputHandler;
    private final UpdateHandler updateHandler;
    private final RenderHandler renderHandler;

    public EngineRunner(Activity activity) {
        this.activity = activity;

        sceneKeeper = new SceneKeeper();
        resourceKeeper = new ResourceKeeper(activity);

        updateHandler = new UpdateHandler(sceneKeeper, 15); // 15 updates per second

        ApplicationView applicationView = new ApplicationView(activity, sceneKeeper, updateHandler);
        activity.setContentView(applicationView);

        inputHandler = new InputHandler(applicationView, sceneKeeper);
        renderHandler = new RenderHandler(applicationView, 30); // 30 renders per second

        // flex config for resolution independence calculations
        FlexConfig flexConfig = new FlexConfig(activity, 1080); // base resolution is 1080p width

        // add initial default scene to processing list
        sceneKeeper.addScene(new DefaultScene(sceneKeeper, resourceKeeper, renderHandler, flexConfig));
    }

    @Override
    public void run() {
        while (running && sceneKeeper.hasScenes()) {
            if (!paused) {
                sceneKeeper.poll();
                inputHandler.poll();
                updateHandler.poll();
                renderHandler.poll();
            }

            SystemClock.sleep(1); // prevent CPU from hogging and draining battery
        }

        sceneKeeper.removeAllScenes();
        resourceKeeper.unloadEverything();

        activity.finish();
    }

    public void start() {
        if (!runnerThread.isAlive()) {
            running = true;
            runnerThread.start();
        } else {
            resume();
        }
    }

    public void stop() {
        running = false;
    }

    public void pause() {
        paused = true;
    }

    public void resume() {
        paused = false;
        updateHandler.resetDelay(); // ignore a big time difference after coming back from pause
    }

    public void backPressed() {
        for (Scene scene : sceneKeeper.scenes) {
            scene.backPressed();
        }
    }
}
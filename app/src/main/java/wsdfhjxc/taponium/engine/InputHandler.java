package wsdfhjxc.taponium.engine;

import android.view.*;
import java.util.concurrent.*;

public class InputHandler extends TimedHandler implements ApplicationView.OnTouchListener {
    private final SceneKeeper sceneKeeper;
    private final BlockingQueue<MotionEvent> motionEvents;

    public InputHandler(ApplicationView applicationView, SceneKeeper sceneKeeper) {
        super(1000, true); // 1000 input catches per second should be more than enough
        this.sceneKeeper = sceneKeeper;
        motionEvents = new LinkedBlockingQueue();
        applicationView.setOnTouchListener(this);
    }

    @Override
    public void handle() {
        while (!motionEvents.isEmpty()) {
            MotionEvent motionEvent = motionEvents.poll();
            if (motionEvent != null) {
                for (Scene scene : sceneKeeper.scenes) {
                    scene.handleInput(motionEvent);
                }
            }
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        motionEvents.offer(motionEvent);
        return true;
    }
}
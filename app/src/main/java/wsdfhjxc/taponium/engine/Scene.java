package wsdfhjxc.taponium.engine;

import android.graphics.*;
import android.view.*;

public abstract class Scene implements Comparable<Scene> {
    protected final SceneKeeper sceneKeeper;
    protected final ResourceKeeper resourceKeeper;
    protected final FlexConfig flexConfig;

    private final int id;
    protected int order = 0;

    public Scene(SceneKeeper sceneKeeper, ResourceKeeper resourceKeeper, FlexConfig flexConfig, int id, int order) {
        this.sceneKeeper = sceneKeeper;
        this.resourceKeeper = resourceKeeper;
        this.flexConfig = flexConfig;
        this.id = id;
        this.order = order;
    }

    public abstract void load();

    public abstract void unload();

    public abstract void backPressed();

    public abstract void handleInput(MotionEvent motionEvent);

    public abstract void handleUpdate(double deltaTime);

    public abstract void handleRender(Canvas canvas, Paint paint, double alpha);

    public int getId() {
        return id;
    }

    public int getOrder() {
        return order;
    }

    @Override
    public int compareTo(Scene anotherScene) {
        return order - anotherScene.getOrder();
    }

    @Override
    public boolean equals(Object object) {
        return (object instanceof Scene && ((Scene)object).getId() == id);
    }

    @Override
    public int hashCode() {
        return id;
    }
}
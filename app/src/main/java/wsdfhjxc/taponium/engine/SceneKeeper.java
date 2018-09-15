package wsdfhjxc.taponium.engine;

import java.util.*;

public class SceneKeeper {
    public final List<Scene> scenes;
    private final List<Scene> scenesToLoad;
    private final List<Scene> scenesToAdd;
    private final List<Scene> scenesToRemove;
    private final List<Scene> scenesToUnload;

    public SceneKeeper() {
        scenes = new ArrayList();
        scenesToLoad = new ArrayList();
        scenesToAdd = new ArrayList();
        scenesToRemove = new ArrayList();
        scenesToUnload = new ArrayList();
    }

    private void sort() {
        Collections.sort(scenes);
    }

    public void addScene(Scene scene) {
        if (!scenesToAdd.contains(scene) && !scenesToRemove.contains(scene)) {
            scenesToAdd.add(scene);
        }
    }

    public void removeScene(Scene scene) {
        if (!scenesToRemove.contains(scene)) {
            scenesToRemove.add(scene);
        }
    }

    public void removeAllScenes() {
        for (Scene scene : scenes) {
            scenesToRemove.add(scene);
        }
    }

    synchronized private void loadScenes() {
        for (Scene scene : scenesToLoad) {
            scene.load();
            scenes.add(scene);
        }

        scenesToLoad.clear();
    }

    synchronized private void unloadScenes() {
        for (Scene scene : scenesToUnload) {
            scenes.remove(scene);
            scene.unload();
        }

        scenesToUnload.clear();
    }

    private boolean addScenes() {
        boolean hasSomethingBeenAdded = !scenesToAdd.isEmpty();

        for (Scene scene : scenesToAdd) {
            scenesToLoad.add(scene);
        }

        scenesToAdd.clear();
        loadScenes();

        return hasSomethingBeenAdded;
    }

    private void removeScenes() {
        for (Scene scene : scenesToRemove) {
            scenesToUnload.add(scene);
        }

        scenesToRemove.clear();
        unloadScenes();
    }

    public void poll() {
        removeScenes();

        if (addScenes()) {
            sort();
        }
    }

    public boolean hasScenes() {
        return !(scenes.isEmpty() && scenesToAdd.isEmpty() && scenesToLoad.isEmpty());
    }
}
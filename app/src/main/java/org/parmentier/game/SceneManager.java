
package org.parmentier.game;
import java.util.Queue;
    
public class SceneManager {
    Queue<Scene> scenes;

    public SceneManager() {
        this.scenes = new java.util.LinkedList<>();
    }

    public void pushScene(Scene scene) {
        scenes.clear();
        scenes.add(scene);
    }

    public Scene popScene() {
        return scenes.poll();
    }

    public Scene activeScene() {
        return scenes.peek();
    }
}

package org.parmentier.game;
import java.util.Stack;
    
public class SceneManager {
    Stack<Scene> scenes;

    public SceneManager() {
        this.scenes = new Stack<>();
    }

    public void pushScene(Scene scene) {
        this.scenes.push(scene);
        Game.getInstance().refresh();
    }

    public Scene popScene() {
        Scene removedScene = this.scenes.pop();
        Game.getInstance().refresh();
        return removedScene;
    }

    public Scene activeScene() {
        return this.scenes.peek();
    }
}
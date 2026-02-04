
package org.parmentier.game;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Game {
    private final SceneManager sceneManager;
    private long lastTime = 0;

    public Game() {
        this.sceneManager = new SceneManager();
    }

    public SceneManager getSceneManager() {
        return sceneManager;
    }

    public void update(double deltaTime, StackPane root) {
        Scene activeScene = sceneManager.activeScene();
        if (activeScene != null) {
            activeScene.update(deltaTime, root);
        }
    }

    public void render(GraphicsContext gc, StackPane root) {
        Scene activeScene = sceneManager.activeScene();
        if (activeScene != null) {
            activeScene.render(gc, root);
        }
    }

    public void start(Stage stage) {
        Canvas canvas = new Canvas(800, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        final int WIDTH = 300;
        final int HEIGHT = 300;
        StackPane root = new StackPane(canvas);

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastTime == 0) {
                    lastTime = now;
                    return;
                }

                double deltaTime = (now - lastTime) / 10e8;
                lastTime = now;

                update(deltaTime, root);
                render(gc, root);
            }
        }.start();


        stage.setScene(new javafx.scene.Scene(root, WIDTH, HEIGHT));
        stage.setTitle("JavaFX Classic Game Loop");
        stage.show();
    }
}
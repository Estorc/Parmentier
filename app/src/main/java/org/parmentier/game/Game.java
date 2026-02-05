
package org.parmentier.game;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Game {
    private static Game instance;
    private final SceneManager sceneManager;
    private long lastTime = 0;

    public Game() {
        this.sceneManager = new SceneManager();
        instance = this;
    }

    public SceneManager getSceneManager() {
        return sceneManager;
    }

    public static Game getInstance() {
        return instance;
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
        final int WIDTH = 300;
        final int HEIGHT = 300;
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        StackPane root = new StackPane(canvas);
        StackPane uiLayer = new StackPane();
        root.getChildren().add(uiLayer);

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastTime == 0) {
                    lastTime = now;
                    return;
                }

                double deltaTime = (now - lastTime) / 10e8;
                lastTime = now;

                update(deltaTime, uiLayer);
                render(gc, uiLayer);
            }
        }.start();


        stage.setScene(new javafx.scene.Scene(root, WIDTH, HEIGHT));
        stage.setTitle("JavaFX Classic Game Loop");
        stage.show();
    }
}
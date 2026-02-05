
package org.parmentier.game;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Game {
    private static Game instance;
    private StackPane uiLayer;
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

    public void refresh() {
        if (this.uiLayer != null) sceneManager.activeScene().initialize(uiLayer);
    }

    public void update(double deltaTime) {
        Scene activeScene = sceneManager.activeScene();
        if (activeScene != null) {
            activeScene.update(deltaTime, uiLayer);
        }
    }

    public void render(GraphicsContext gc) {
        Scene activeScene = sceneManager.activeScene();
        if (activeScene != null) {
            activeScene.render(gc, uiLayer);
        }
    }

    public void start(Stage stage) {
        final int WIDTH = 300;
        final int HEIGHT = 300;
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        StackPane root = new StackPane(canvas);
        uiLayer = new StackPane();
        uiLayer.setMaxWidth(HEIGHT);
        uiLayer.setMaxHeight(HEIGHT);
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

                update(deltaTime);
                render(gc);
            }
        }.start();


        stage.setScene(new javafx.scene.Scene(root, WIDTH, HEIGHT));
        stage.setTitle("JavaFX Classic Game Loop");
        stage.show();
        sceneManager.activeScene().initialize(uiLayer);
    }
}

package org.parmentier.game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;

public interface Scene {
    void update(double deltaTime, StackPane root);
    void render(GraphicsContext gc, StackPane root);
}
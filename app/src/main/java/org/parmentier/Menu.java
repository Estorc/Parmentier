package org.parmentier;

import org.parmentier.game.Game;
import org.parmentier.game.Level;
import org.parmentier.game.Scene;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.canvas.GraphicsContext;


public abstract class Menu implements Scene{
    private boolean initialized = false;

    @Override
    public void update(double deltaTime, StackPane menuInterface) {
        if (!initialized) {
            levelScene(menuInterface);
            initialized = true;
        }
    }

    public abstract void levelScene(StackPane menuInterface);

    @Override
    public void render(GraphicsContext gc, StackPane uiLayer){
        
    }
}
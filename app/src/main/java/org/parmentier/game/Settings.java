package org.parmentier.game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.parmentier.game.Game;
import org.parmentier.game.MainMenu;
import org.parmentier.game.SceneManager;
import org.parmentier.Parmentier;

public class Settings implements org.parmentier.game.Scene {
    private boolean initialized = false;

    @Override
    public void update(double deltaTime, StackPane uiLayer) {
        if (!initialized) {
            start(uiLayer);
            initialized = true;
        }
    }

    @Override
    public void render(GraphicsContext gc, StackPane uiLayer) {
        return;
    }

    public void start(StackPane uiLayer) {

        VBox layout = new VBox();
        VBox buttonBox = new VBox();
        
        layout.setSpacing(20);
        buttonBox.setSpacing(10);
        layout.setAlignment(javafx.geometry.Pos.TOP_CENTER);
        MenuButton themes = new MenuButton("Thèmes");
        themes.getItems().addAll(new MenuItem("Clair"), new MenuItem("Sombre"));
        MenuButton locale = new MenuButton("Langues");
        locale.getItems().addAll(new MenuItem("Français"), new MenuItem("English"));
        Button returnButton = new Button("Retour");
        Label titleLabel = new Label("Paramètres");
        titleLabel.getStyleClass().add("title-label");
        themes.getStyleClass().add("button-setting");
        locale.getStyleClass().add("button-setting");
        returnButton.getStyleClass().add("button");

        buttonBox.getChildren().add(themes);
        buttonBox.getChildren().add(locale);
        buttonBox.getChildren().add(returnButton);
        buttonBox.setAlignment(javafx.geometry.Pos.CENTER);
        layout.getChildren().add(titleLabel);
        layout.getChildren().add(buttonBox);
        uiLayer.getChildren().add(layout);
        uiLayer.getStylesheets().add(getClass().getResource("/app.css").toExternalForm());
    }
}

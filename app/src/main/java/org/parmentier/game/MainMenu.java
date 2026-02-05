package org.parmentier.game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class MainMenu implements org.parmentier.game.Scene {
    private boolean initialized = false;

    @Override
    public void update(double deltaTime, StackPane uiLayer) {
        if (!initialized) {
            start(uiLayer);
            uiLayer.getChildren().clear();
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
        Button jouer = new Button("Jouer");
        Button settings = new Button("Paramètres");
        Button leave = new Button("Quitter");
        Label titleLabel = new Label("Parmentier");
        titleLabel.getStyleClass().add("title-label");
        jouer.getStyleClass().add("button");
        settings.getStyleClass().add("button");
        leave.getStyleClass().add("button");

        buttonBox.getChildren().add(jouer);
        buttonBox.getChildren().add(settings);
        buttonBox.getChildren().add(leave);
        buttonBox.setAlignment(javafx.geometry.Pos.CENTER);
        layout.getChildren().add(titleLabel);
        layout.getChildren().add(buttonBox);
        uiLayer.getChildren().add(layout);
        uiLayer.getStylesheets().add(getClass().getResource("/app.css").toExternalForm());

        /*leave.setOnAction(e -> {
            uiLayer.close();
        });*/
    }
}

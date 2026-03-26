package org.parmentier.game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class Settings implements org.parmentier.game.Scene {

    @Override
    public void update(double deltaTime, StackPane uiLayer) {
        //
    }

    @Override
    public void render(GraphicsContext gc, StackPane uiLayer) {
        //
    }

    @Override
    public void initialize(StackPane uiLayer) {
        uiLayer.getChildren().clear();
        if (!uiLayer.getStyleClass().contains("main-menu-bg")) {
            uiLayer.getStyleClass().add("main-menu-bg");
        }

        VBox layout = new VBox();
        layout.setId("settingsBox");
        VBox buttonBox = new VBox();

        layout.setSpacing(20);
        buttonBox.setSpacing(10);
        layout.setAlignment(javafx.geometry.Pos.TOP_CENTER);
        MenuButton themes = new MenuButton("Thèmes");

        MenuItem clair = new MenuItem("Clair");
        MenuItem sombre = new MenuItem("Sombre");
        themes.getItems().addAll(clair, sombre);

        MenuButton locale = new MenuButton("Langues");
        locale.getItems().addAll(new MenuItem("Français"), new MenuItem("English"));
        Button returnButton = new Button("Retour");
        Label titleLabel = new Label("Paramètres");
        titleLabel.getStyleClass().add("title-label-settings");
        themes.getStyleClass().add("button-setting");
        locale.getStyleClass().add("button-setting");
        returnButton.getStyleClass().add("menu-button");

        buttonBox.getChildren().add(themes);
        buttonBox.getChildren().add(locale);
        buttonBox.getChildren().add(returnButton);
        buttonBox.setAlignment(javafx.geometry.Pos.CENTER);
        layout.getChildren().add(titleLabel);
        layout.getChildren().add(buttonBox);
        uiLayer.getChildren().add(layout);

        if (!uiLayer.getStyleClass().contains("main-menu-bg")) {
            uiLayer.getStyleClass().add("main-menu-bg");
        }

        if (uiLayer.getStylesheets().isEmpty()) {
            uiLayer.getStylesheets().add(getClass().getResource("/lightMode.css").toExternalForm());
        }

        clair.setOnAction(e -> {
            uiLayer.getStylesheets().clear();
            uiLayer.getStylesheets().add(getClass().getResource("/lightMode.css").toExternalForm());
        });

        sombre.setOnAction(e -> {
            uiLayer.getStylesheets().clear();
            uiLayer.getStylesheets().add(getClass().getResource("/darkMode.css").toExternalForm());
        });

        returnButton.setOnMouseClicked(e -> {
            Game.getInstance().getSceneManager().popScene();
        });
    }

    /**
     * Default destructor forthe Settings class. Since there are no specific resources to clean up, this method is left empty.
     */
    @Override
    public void destroy() {
        //
    }

}

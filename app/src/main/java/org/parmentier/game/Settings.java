/** ********************************************************************************
 * Represents the settings scene of the Parmentier game, allowing players to customize
 * their gaming experience by adjusting themes and languages.
 * This class implements the Scene interface, providing methods for updating, rendering,
 * initializing, and destroying the settings scene. The user interface is built using JavaFX components,
 * including buttons and menu items for theme and language selection.
 ***********************************************************************************
 * @author Estorc
 * @version v1.0
 * @package org.parmentier.game
 * @copyright Copyright (c) 2026 Parmentier's team GNU GENERAL PUBLIC LICENSE.
 **********************************************************************************/
/*                             This file is part of
 *                                  Parmentier
 *           (https://github.com/Estorc/Projet-Genie-Logiciel-L3-Parmentier)
 ***********************************************************************************/

package org.parmentier.game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Slider;

/**
 * Represents the settings scene of the Parmentier game, allowing players to customize
 * their gaming experience by adjusting themes and languages.
 * This class implements the Scene interface, providing methods for updating, rendering,
 * initializing, and destroying the settings scene. The user interface is built using JavaFX components,
 * including buttons and menu items for theme and language selection.
 */
public class Settings implements org.parmentier.game.Scene {
    private Slider soundSlider;
    public static javafx.scene.paint.Color currentStrokeColor = javafx.scene.paint.Color.BLACK;

    /**
     * Default constructor for the Settings class.
     * This constructor initializes the settings scene, setting up any necessary resources or state.
     */
    @Override
    public void update(double deltaTime, StackPane uiLayer) {
        //
    }

    /**
     * Renders the settings scene on the canvas, drawing all visual elements based on the current game state.
     * This method is called at each frame of the game loop to ensure that the scene is visually updated in response to changes in the game state,
     * user interactions, and animations.
     * @param gc The GraphicsContext used to draw on the canvas, allowing the scene to render its visual elements such as backgrounds,
     * nodes, bridges, and other game components.
     * @param uiLayer The StackPane that serves as the UI layer for the scene, allowing the scene to render or update UI elements
     * in conjunction with the canvas rendering.
     */
    @Override
    public void render(GraphicsContext gc, StackPane uiLayer) {
        //
    }

    /**
     * Initializes the settings scene, setting up the user interface elements such as buttons and menu items for theme and language selection.
     * This method is called when the scene is first loaded, allowing it to prepare the UI layer with the necessary components for user interaction.
     * @param uiLayer The StackPane that serves as the UI layer for the scene, allowing the scene to add interactive elements such as
     * buttons and labels.
     */
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

        // Sounds
        Label soundLabel = new Label("Volume son");
        soundLabel.getStyleClass().add("textUser");
        
        soundSlider = new Slider(0, 1, Audio.getVolume());
        soundSlider.setMaxWidth(200);
        soundSlider.getStyleClass().add("sound-slider");
        
        soundSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            Audio.setVolume(newVal.doubleValue());
        });

        Button returnButton = new Button("Retour");
        Label titleLabel = new Label("Paramètres");
        titleLabel.getStyleClass().add("title-label-settings");
        themes.getStyleClass().add("button-setting");
        returnButton.getStyleClass().add("menu-button");

        Button resetLeaderboardButton = new Button("Réinitialiser les classements");
        resetLeaderboardButton.getStyleClass().add("menu-button");

        resetLeaderboardButton.setOnAction(e -> {
            org.parmentier.game.Audio.playClickSound();

            javafx.scene.layout.VBox modal = new javafx.scene.layout.VBox(30);
            modal.setAlignment(javafx.geometry.Pos.CENTER);
            modal.getStyleClass().add("main-menu-bg");
            modal.setStyle("-fx-padding: 40; -fx-border-color: " + (Settings.currentStrokeColor == javafx.scene.paint.Color.WHITE ? "white" : "black") + "; -fx-border-width: 2;");
            modal.setMaxSize(600, 300);

            Label warningLabel = new Label("Attention, vous vous apprêtez à supprimer les classements enregistrés pour tous les utilisateurs, ce choix est irrévocable, en êtes-vous absolument sûr ?");
            warningLabel.setWrapText(true);
            warningLabel.getStyleClass().add("textUser");
            warningLabel.setStyle("-fx-font-size: 18px; -fx-text-alignment: center; -fx-opacity: 1;");
            
            Button confirmBtn = new Button("Oui, supprimer les classements");
            confirmBtn.getStyleClass().add("menu-button");
            // Set base styles to prevent inheriting the generic blue background
            confirmBtn.setStyle("-fx-border-color: #ff4444; -fx-text-fill: #ff4444; -fx-background-color: transparent;");
            
            // Hover states to emulate the menu-button hover but with red tint
            confirmBtn.setOnMouseEntered(ev -> {
                confirmBtn.setStyle("-fx-border-color: #ff6666; -fx-text-fill: #ff6666; -fx-background-color: rgba(255, 68, 68, 0.2);");
            });
            confirmBtn.setOnMouseExited(ev -> {
                confirmBtn.setStyle("-fx-border-color: #ff4444; -fx-text-fill: #ff4444; -fx-background-color: transparent;");
            });
            Button cancelBtn = new Button("Annuler");
            cancelBtn.getStyleClass().add("menu-button");
            cancelBtn.setStyle("");

            confirmBtn.setOnAction(ev -> {
                org.parmentier.game.Audio.playClickSound();
                java.io.File dir = new java.io.File(org.parmentier.Parmentier.SAVES_DIR);
                if (dir.exists() && dir.isDirectory()) {
                    java.io.File[] files = dir.listFiles((d, name) -> name.matches(".*leaderboard\\.sav"));
                    if (files != null) {
                        for (java.io.File f : files) {
                            f.delete();
                        }
                    }
                }
                uiLayer.getChildren().remove(modal);
            });

            cancelBtn.setOnAction(ev -> {
                org.parmentier.game.Audio.playClickSound();
                uiLayer.getChildren().remove(modal);
            });

            javafx.scene.layout.HBox btnRow = new javafx.scene.layout.HBox(20, confirmBtn, cancelBtn);
            btnRow.setAlignment(javafx.geometry.Pos.CENTER);

            modal.getChildren().addAll(warningLabel, btnRow);
            uiLayer.getChildren().add(modal);
        });

        buttonBox.getChildren().add(themes);
        buttonBox.getChildren().add(resetLeaderboardButton);
        buttonBox.getChildren().add(returnButton);
        buttonBox.setAlignment(javafx.geometry.Pos.CENTER);
        layout.getChildren().add(titleLabel);
        layout.getChildren().add(soundLabel);
        layout.getChildren().add(soundSlider);
        layout.getChildren().add(buttonBox);
        uiLayer.getChildren().add(layout);

        if (!uiLayer.getStyleClass().contains("main-menu-bg")) {
            uiLayer.getStyleClass().add("main-menu-bg");
        }

        if (uiLayer.getStylesheets().isEmpty()) {
            uiLayer.getStylesheets().add(getClass().getResource("/lightMode.css").toExternalForm());
        }

        clair.setOnAction(e -> {
            org.parmentier.game.Audio.playClickSound();
            Settings.currentStrokeColor = javafx.scene.paint.Color.BLACK;
            uiLayer.getStylesheets().clear();
            uiLayer.getStylesheets().add(getClass().getResource("/lightMode.css").toExternalForm());
        });

        sombre.setOnAction(e -> {
            org.parmentier.game.Audio.playClickSound();
            Settings.currentStrokeColor = javafx.scene.paint.Color.WHITE;
            uiLayer.getStylesheets().clear();
            uiLayer.getStylesheets().add(getClass().getResource("/darkMode.css").toExternalForm());
        });

        returnButton.setOnMouseClicked(e -> {
            org.parmentier.game.Audio.playClickSound();
            Game.getInstance().getSceneManager().popScene();
        });
    }

    /**
     * Default destructor forthe Settings class.
     * Since there are no specific resources to clean up, this method is left empty.
     */
    @Override
    public void destroy() {
        //
    }

}

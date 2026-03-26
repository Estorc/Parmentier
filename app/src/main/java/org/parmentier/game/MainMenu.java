/** ********************************************************************************
 * Represents the main menu scene of the Parmentier game, providing options for the player to start a new game,
 * access the tutorial, adjust settings, or exit the application.
 * The main menu features a visually appealing layout with a logo, title, subtitle, and interactive buttons.
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

import org.parmentier.MenuChoiceLevel;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.geometry.Pos;
import javafx.geometry.Insets;

/**
 * Represents the main menu scene of the Parmentier game, providing options for the player to start a new game,
 * access the tutorial, adjust settings, or exit the application.
 * The main menu features a visually appealing layout with a logo, title, subtitle, and interactive buttons.
 */
public class MainMenu implements org.parmentier.game.Scene {

    /**
     * Default constructor for the MainMenu class.
     * This constructor initializes the main menu scene, setting up any necessary resources or state.
     */
    @Override
    public void update(double deltaTime, StackPane uiLayer) {
        //
    }

    /**
     * Renders the main menu scene on the canvas, drawing all visual elements based on the current game state.
     * This method is called at each frame of the game loop to ensure that the scene is visually updated in response to changes in the game state,
     * user interactions, and animations.
     * @param gc The GraphicsContext used to draw on the canvas, allowing the scene to render its visual elements such as backgrounds,
     * nodes, bridges, and other game components.
     * @param uiLayer The StackPane that serves as the UI layer for the scene,
     * allowing the scene to render or update UI elements in conjunction with the canvas rendering.
     */
    @Override
    public void render(GraphicsContext gc, StackPane uiLayer) {
        //
    }

    /**
     * Creates a StackPane containing a circle with a number inside, used for the logo in the main menu.
     * @param number The number to be displayed inside the circle, representing a node in the Parmentier puzzle game.
     * @return A StackPane containing the circle and the number, styled according to the main menu's design.
     */
    private StackPane createCircledNumber(String number) {
        Circle circle = new Circle(22);
        circle.getStyleClass().add("logo-circle");
        Text text = new Text(number);
        text.getStyleClass().add("logo-number");
        StackPane pane = new StackPane(circle, text);
        return pane;
    }

    /**
     * Initializes the main menu scene, setting up the UI elements such as the logo, title, subtitle, and buttons for user interaction.
     * This method is called when the main menu scene is first displayed, allowing it to prepare the necessary UI components and layout
     * for the player to interact with.
     * @param uiLayer The StackPane that serves as the UI layer for the scene, allowing the main menu to add interactive elements
     * such as buttons and labels.
     */
    @Override
    public void initialize(StackPane uiLayer) {
        uiLayer.getChildren().clear();
        VBox layout = new VBox();
        layout.getStyleClass().add("main-menu-bg");

        // Logo hashi game grid 2x2
        GridPane logoGrid = new GridPane();
        logoGrid.setHgap(10);
        logoGrid.setVgap(6);
        logoGrid.setAlignment(Pos.CENTER);
        logoGrid.add(createCircledNumber("4"), 0, 0);
        logoGrid.add(createCircledNumber("3"), 1, 0);
        logoGrid.add(createCircledNumber("2"), 0, 1);
        logoGrid.add(createCircledNumber("5"), 1, 1);

        // Title
        Label titleLabel = new Label("Parmentier");
        titleLabel.getStyleClass().add("title-label");

        // Subtitle
        Label subtitleLabel = new Label("Hashi");
        subtitleLabel.getStyleClass().add("subtitle-label");

        // Buttons
        VBox buttonBox = new VBox(12);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));
        Button jouer = new Button("Jouer");
        Button tutorial = new Button("Didacticiel");
        Button settings = new Button("Paramètres");
        Button leave = new Button("Quitter");
        jouer.getStyleClass().add("menu-button");
        tutorial.getStyleClass().add("menu-button");
        settings.getStyleClass().add("menu-button");
        leave.getStyleClass().add("menu-button");
        jouer.setMaxWidth(320);
        jouer.setMinWidth(320);
        tutorial.setMaxWidth(320);
        tutorial.setMinWidth(320);
        settings.setMaxWidth(320);
        settings.setMinWidth(320);
        leave.setMaxWidth(320);
        leave.setMinWidth(320);
        buttonBox.getChildren().addAll(jouer, tutorial, settings, leave);

        layout.setSpacing(8);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40, 0, 40, 0));
        layout.getChildren().addAll(logoGrid, subtitleLabel, titleLabel, buttonBox);

        uiLayer.getChildren().add(layout);
        uiLayer.getStylesheets().add(getClass().getResource("/lightMode.css").toExternalForm());

        jouer.setOnMouseClicked( e -> Game.getInstance().getSceneManager().pushScene(new MenuChoiceLevel()));
        settings.setOnMouseClicked( e -> Game.getInstance().getSceneManager().pushScene(new Settings()));
        leave.setOnMouseClicked( e -> System.exit(0));
        tutorial.setOnMouseClicked( e -> Game.getInstance().getSceneManager().pushScene(new Didacticiel()));
    }

    /**
     * Default destructor for the MainMenu class.
     * Since there are no specific resources to clean up, this method is left empty.
     */
    @Override
    public void destroy() {
        //
    }
}

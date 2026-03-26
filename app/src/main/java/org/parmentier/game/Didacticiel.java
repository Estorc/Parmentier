/** ********************************************************************************
 * Represents the tutorial scene of the Parmentier game, providing an introduction to the game's mechanics and rules.
 * This class implements the Scene interface, allowing it to be integrated seamlessly into the game's scene management system.
 * The tutorial scene features a visually appealing layout with a title, introductory text, and a button to start the tutorial level.
 * The introductory text provides a comprehensive overview of the Hashiwokakero puzzle, explaining its objectives, rules,
 * and strategies for solving it. The layout is designed to be user-friendly and engaging, with clear labels and interactive
 * elements to guide the player through the tutorial experience.
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

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.TextAlignment;

/**
 * Represents the tutorial scene of the Parmentier game, providing an introduction to the game's mechanics and rules.
 * This class implements the Scene interface, allowing it to be integrated seamlessly into the game's scene management system.
 * The tutorial scene features a visually appealing layout with a title, introductory text, and a button to start the tutorial level.
 * The introductory text provides a comprehensive overview of the Hashiwokakero puzzle, explaining its objectives, rules,
 * and strategies for solving it. The layout is designed to be user-friendly and engaging, with clear labels and interactive
 * elements to guide the player through the tutorial experience.
 */
public class Didacticiel implements org.parmentier.game.Scene {
    
    /**
     * Default constructor for the Didacticiel class.
     * This constructor initializes the tutorial scene, setting up any necessary resources or state.
     */
    @Override
    public void update(double deltaTime, StackPane uiLayer) {
        //
    }

    /**
     * Renders the tutorial scene on the canvas, drawing all visual elements based on the current game state.
     * This method is called at each frame of the game loop to ensure that the scene is visually updated in response to changes
     * in the game state, user interactions, and animations.
     * @param gc The GraphicsContext used to draw on the canvas, allowing the scene to render its visual elements such as backgrounds,
     * nodes, bridges, and other game components.
     * @param uiLayer The StackPane that serves as the UI layer for the scene, allowing the scene to render or update UI elements in
     * conjunction with the canvas rendering.
     */
    @Override
    public void render(GraphicsContext gc, StackPane uiLayer) {
        //
    }

    /**
     * Initializes the tutorial scene, setting up the user interface elements such as labels and buttons.
     * This method is called when the scene is first loaded, allowing it to prepare the layout and interactive components for display.
     * @param uiLayer The StackPane that serves as the UI layer for the scene, allowing the scene to add interactive elements such as
     * buttons and labels.
     */
    @Override
    public void initialize(StackPane uiLayer) {
        uiLayer.getChildren().clear();
        String textintro = "Le Hashiwokakero, couramment appelé Hashi, est un casse-tête logique dont l'objectif est de relier une série d'îles numérotées par des ponts afin de former un réseau unique et continu. Chaque île contient un chiffre qui dicte précisément le nombre de ponts devant s'y raccorder. Le tracé de ces ponts suit des contraintes strictes : ils ne peuvent être que horizontaux ou verticaux, ne doivent jamais se croiser ni survoler une autre île, et la connexion entre deux îles est limitée à un maximum de deux ponts. La résolution repose sur l'élimination progressive des options : on commence généralement par les îles aux chiffres élevés situées dans les coins ou les bords, car leurs possibilités de connexion sont physiquement limitées. Le défi ultime réside dans la règle de connectivité globale, qui interdit la formation de sous-groupes isolés ; chaque pont posé doit contribuer à l'édification d'un chemin unique reliant l'intégralité de l'archipel, transformant ainsi chaque décision en un exercice d'anticipation topologique.";
        VBox layout = new VBox();
        layout.getStyleClass().add("main-menu-bg");
        // Title
        Label titleLabel = new Label("Parmentier");
        titleLabel.getStyleClass().add("title-label");

        // Buttons
        VBox buttonBox = new VBox(12);
        Label intro = new Label(textintro);
        intro.getStyleClass().add("textIntro");
        intro.setWrapText(true);
        intro.setMaxWidth(600); 
        intro.setTextAlignment(TextAlignment.CENTER);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));
        Button tutorial = new Button("Didacticiel");
        tutorial.getStyleClass().add("menu-button");
        tutorial.setMaxWidth(320);
        tutorial.setMinWidth(320);
        buttonBox.getChildren().addAll(tutorial);

        layout.setSpacing(8);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40, 0, 40, 0));
        layout.getChildren().addAll(titleLabel, intro, buttonBox);

        uiLayer.getChildren().add(layout);
        uiLayer.getStylesheets().add(getClass().getResource("/app.css").toExternalForm());
        tutorial.setOnMouseClicked( e -> Game.getInstance().getSceneManager().pushScene(new Level("didacticiel")));
    }

    /**
     * Default destructor for the MainMenu class. Since there are no specific resources to clean up, this method is left empty.
     */
    @Override
    public void destroy() {
        //
    }
}

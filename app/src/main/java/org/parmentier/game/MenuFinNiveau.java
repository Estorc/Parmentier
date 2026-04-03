/** ********************************************************************************
 * Represents the end of level menu in the Parmentier game.
 * This class is responsible for displaying the player's performance, including
 * the time taken and score achieved, and providing options to proceed to the next level or return to the main menu.
 * The menu is designed to be visually appealing and user-friendly, with clear labels and buttons for navigation.
 * It implements the Scene interface, allowing it to be integrated seamlessly into the game's scene management system.
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

/**
 * Represents the end of level menu in the Parmentier game.
 * This class is responsible for displaying the player's performance, including
 * the time taken and score achieved, and providing options to proceed to the next level or return to the main menu.
 * The menu is designed to be visually appealing and user-friendly, with clear labels and buttons for navigation.
 * It implements the Scene interface, allowing it to be integrated seamlessly into the game's scene management system.
 */
public class MenuFinNiveau implements org.parmentier.game.Scene {

    /** The time taken by the player to complete the level, displayed in the end of level menu. */
    private String deltaTime;

    /** The score achieved by the player for completing the level, displayed in the end of level menu. */
    private Integer score;

    /**
     * Constructor for the MenuFinNiveau class, initializing the end of level menu with the player's performance data.
     * @param time  The time taken by the player to complete the level, which will be displayed in the end of level menu.
     * @param score The score achieved by the player for completing the level, which will be displayed in the end of level menu.
     */
    public MenuFinNiveau(String time, Integer score) {
        this.deltaTime = time;
        this.score = score;
    }

    /**
     * Updates the end of level menu based on the elapsed time and user interactions.
     * This method is called at each frame of the game loop to allow the menu to update its state, handle user input,
     * and perform any necessary logic. In this implementation, the update method is currently empty, as the end of level
     * menu does not require dynamic updates or interactions beyond the initial display of the player's performance data and navigation options.
     * @param deltaTime The time elapsed since the last update, used to ensure smooth animations and consistent game
     * behavior regardless of frame rate. In this implementation, the deltaTime parameter is not utilized, as the end
     * of level menu does not have any time-based animations or updates.
     * @param uiLayer   The StackPane that serves as the UI layer for the scene, allowing the menu to modify or update
     * UI elements based on user interactions or game state changes. In this implementation, the uiLayer parameter is not
     * utilized in the update method, as the end of level menu does not have any dynamic UI elements that require updates after the initial display.
     */
    @Override
    public void update(double deltaTime, StackPane uiLayer) {
        //
    }

    /**
     * Renders the end of level menu on the canvas, drawing all visual elements based on the current game state.
     * This method is called at each frame of the game loop to ensure that the menu is visually updated in response to changes
     * in the game state, user interactions, and animations. In this implementation, the render method is currently empty, as
     * the end of level menu does not require dynamic rendering or animations beyond the initial display of the player's performance
     * data and navigation options.
     * @param gc The GraphicsContext used to draw on the canvas, allowing the menu to render its visual elements such as backgrounds,
     * nodes, bridges, and other game components. In this implementation, the gc parameter is not utilized in the render method,
     * as the end of level menu does not have any canvas-based visual elements that require rendering.
     * @param uiLayer The StackPane that serves as the UI layer for the scene, allowing the menu to render or update UI elements in 
     * conjunction with the canvas rendering. In this implementation, the uiLayer parameter is not utilized in the render method,
     * as the end of level menu does not have any dynamic UI elements that require updates after the initial display.
     */
    @Override
    public void render(GraphicsContext gc, StackPane uiLayer) {
        //
    }

    /**
     * Initializes the end of level menu, setting up the user interface elements such as labels for the player's
     * performance data and buttons for navigation.
     * This method is called when the end of level menu is first loaded, allowing it to prepare the UI layer with the
     * necessary components for user interaction. The menu displays the time taken and score achieved by the player,
     * and provides buttons to proceed to the next level or return to the main menu.
     * @param uiLayer The StackPane that serves as the UI layer for the scene, allowing the menu to add interactive elements
     * such as buttons and labels. In this implementation, the uiLayer is cleared and populated with a GridPane layout containing
     * labels for the player's performance data and buttons for navigation.
     */
    @Override
    public void initialize(StackPane uiLayer) {
        uiLayer.getChildren().clear();

        GridPane layout = new GridPane();
        layout.setHgap(20);
        layout.setVgap(10);

        layout.setAlignment(javafx.geometry.Pos.CENTER);
        Label titleLabel = new Label("Vous avez gagné !");
        Label timeLabel = new Label("Temps : " + deltaTime);
        Label scoreLabel = new Label("Score : " + score);
        Button nextLevelButton = new Button("Niveau suivant");
        Button mainMenuButton = new Button("Menu principal");
        Button sbButton = new Button("Classement");
        GridPane.setHalignment(titleLabel, javafx.geometry.HPos.CENTER);
        GridPane.setHalignment(timeLabel, javafx.geometry.HPos.CENTER);
        GridPane.setHalignment(scoreLabel, javafx.geometry.HPos.CENTER);
        GridPane.setHalignment(nextLevelButton, javafx.geometry.HPos.CENTER);
        GridPane.setHalignment(mainMenuButton, javafx.geometry.HPos.CENTER);
        GridPane.setHalignment(sbButton, javafx.geometry.HPos.CENTER);


        titleLabel.getStyleClass().add("title-label");
        timeLabel.getStyleClass().add("textUser");
        scoreLabel.getStyleClass().add("textUser");
        nextLevelButton.getStyleClass().add("button");
        mainMenuButton.getStyleClass().add("button");
        sbButton.getStyleClass().add("button");
        layout.add(titleLabel, 2, 1);
        layout.add(timeLabel, 2, 2);
        layout.add(scoreLabel, 2, 3);
        layout.add(nextLevelButton, 3, 4);
        layout.add(mainMenuButton, 1, 4);
        layout.add(sbButton, 2, 4);
        uiLayer.getChildren().add(layout);

        nextLevelButton.setOnMouseClicked(e -> {
            org.parmentier.game.Audio.playClickSound();
            // pop the current end of level menu
            Game.getInstance().getSceneManager().popScene();
        });
        mainMenuButton.setOnMouseClicked(e -> {
            org.parmentier.game.Audio.playClickSound();
            // pop the current end of level menu
            Game.getInstance().getSceneManager().popScene();
            // pop the current level scene
            Game.getInstance().getSceneManager().popScene();
            
        });

        sbButton.setOnMouseClicked(e -> {
            org.parmentier.game.Audio.playClickSound();

            Game.getInstance().getSceneManager().pushScene(new MenuLeaderboard("level aopkzaoifne"));
        });
    }

    /**
     * Default destructor for the MainMenu class. Since there are no specific resources to clean up, this method is left empty.
     */
    @Override
    public void destroy() {
        //
    }
}

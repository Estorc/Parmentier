/** ********************************************************************************
 * Represents a game instance of the Parmentier puzzle game, managing the game loop,
 * scenes, and user interactions.
 * The Game class is responsible for initializing the game, updating the game state,
 * and rendering the game visuals. It uses a SceneManager to handle different scenes
 * within the game, such as the main menu, level selection, and gameplay scenes.
 * The game loop is implemented using JavaFX's AnimationTimer, which calls the update
 * and render methods at each frame.
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

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Represents a game instance of the Parmentier puzzle game, managing the game loop,
 * scenes, and user interactions.
 * The Game class is responsible for initializing the game, updating the game state,
 * and rendering the game visuals. It uses a SceneManager to handle different scenes
 * within the game, such as the main menu, level selection, and gameplay scenes.
 * The game loop is implemented using JavaFX's AnimationTimer, which calls the update
 * and render methods at each frame.
 */
public class Game {
    /**
     * The singleton instance of the Game class, ensuring that only one instance
     * of the game exists throughout the application.
     */
    private static Game instance;

    /**
     * The SceneManager responsible for managing the different scenes in the game,
     * such as the main menu, level selection, and gameplay scenes.
     */
    private final SceneManager sceneManager;

    /**
     * The UI layer of the game, represented as a StackPane, which is used to display
     * UI elements such as buttons, labels, and other interactive components on top
     * of the game canvas.
     */
    private StackPane uiLayer;

    /**
     * The last time the game loop was updated, used to calculate the delta time for
     * smooth animations and game state updates. This variable is updated at each frame
     * to ensure that the game runs at a consistent speed regardless of the frame rate.
     */
    private long lastTime = 0;

    /**
     * Constructs a new Game instance and initializes the SceneManager. The constructor
     * also sets the singleton instance of the Game class to this newly created instance.
     */
    public Game() {
        this.sceneManager = new SceneManager();
        instance = this;
    }

    /**
     * Gets the SceneManager responsible for managing the different scenes in the game.
     * @return The SceneManager instance.
     * This method allows other parts of the application to access the SceneManager to switch
     * between scenes, such as moving from the main menu to the gameplay scene or vice versa.
     */
    public SceneManager getSceneManager() {
        return sceneManager;
    }

    /**
     * Gets the singleton instance of the Game class, ensuring that only one instance of the game
     * exists throughout the application.
     * @return The singleton instance of the Game class.
     * This method allows other parts of the application to access the Game instance, which is necessary
     * for managing the game state, switching scenes, and handling user interactions across different
     * components of the game.
     */
    public static Game getInstance() {
        return instance;
    }

    /**
     * Refreshes the game by reinitializing the active scene.
     * @param uiLayer The UI layer to be passed to the active scene's initialize method.
     * This method is useful for resetting the game state or updating the visuals after certain actions,
     * such as completing a level or returning to the main menu.
     */
    public void refresh() {
        if (this.uiLayer != null && !sceneManager.isEmpty())
            sceneManager.activeScene().initialize(uiLayer);
    }

    /**
     * Updates the game state based on the elapsed time since the last update.
     * @param deltaTime The time elapsed since the last update, in seconds.
     * This method is called at each frame of the game loop to update the game state,
     * such as moving characters, checking for collisions, and handling user input. The deltaTime
     * parameter allows for smooth animations and consistent game behavior regardless of the frame rate.
     */
    public void update(double deltaTime) {
        if (sceneManager.isEmpty()) return;
        Scene activeScene = sceneManager.activeScene();
        if (activeScene != null) {
            activeScene.update(deltaTime, uiLayer);
        }
    }

    /**
     * Renders the game visuals on the canvas using the provided GraphicsContext.
     * @param gc The GraphicsContext used for drawing the game visuals on the canvas.
     * This method is called at each frame of the game loop to render the current state of the game,
     * such as drawing characters, backgrounds, and UI elements.
     */
    public void render(GraphicsContext gc) {
        if (sceneManager.isEmpty()) return;
        Scene activeScene = sceneManager.activeScene();
        if (activeScene != null) {
            activeScene.render(gc, uiLayer);
        }
    }

    /**
     * Starts the game by setting up the JavaFX stage, initializing the game loop using AnimationTimer,
     * and displaying the initial scene.
     * @param stage The JavaFX Stage on which the game will be displayed.
     * This method initializes the game window, sets up the canvas for drawing, and starts the game loop.
     * It also initializes the active scene, allowing the game to start with the appropriate visuals and
     * interactions based on the initial scene (e.g., main menu, level selection, or gameplay).
     */
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
        stage.setOnCloseRequest(event -> {
            System.out.println("Window is closing...");
            sceneManager.clearScenes();
        });
        sceneManager.activeScene().initialize(uiLayer);
    }
}
/** ********************************************************************************
 * Represents a scene in the Parmentier puzzle game, which can be a level, main menu,
 * or any other screen that the player interacts with.
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
import javafx.scene.layout.StackPane;

/**
 * Represents a scene in the Parmentier puzzle game, which can be a level, main menu,
 * or any other screen that the player interacts with.
 */
public interface Scene {
    /**
     * Initializes the scene, setting up any necessary UI elements, loading resources, and preparing the scene for display.
     * @param uiLayer   The StackPane that serves as the UI layer for the scene, allowing the scene to add interactive elements such as buttons and labels.
     */
    void initialize(StackPane uiLayer);
    /**
     * Updates the scene based on the elapsed time and user interactions. This method is called at each frame of the
     * game loop to allow the scene to update its state, handle user input, and perform any necessary logic.
     * @param deltaTime The time elapsed since the last update, used to ensure smooth animations and consistent game behavior regardless of frame rate.
     * @param uiLayer   The StackPane that serves as the UI layer for the scene, allowing the scene to modify or update UI elements based on user
     *                  interactions or game state changes.
     */
    void update(double deltaTime, StackPane uiLayer);

    /**
     * Renders the scene on the canvas, drawing all visual elements based on the current game state. This method is called at each frame of the
     * game loop to ensure that the scene is visually updated in response to changes in the game state, user interactions, and animations.
     * @param gc      The GraphicsContext used to draw on the canvas, allowing the scene to render its visual elements such as backgrounds, nodes,
     *                bridges, and other game components.
     * @param uiLayer The StackPane that serves as the UI layer for the scene, allowing the scene to render or update UI elements in conjunction
     *                with the canvas rendering.
     */
    void render(GraphicsContext gc, StackPane uiLayer);

    /**
     * Performs destruction or cleanup operations when the scene is no longer needed.
     */
    void destroy();
}
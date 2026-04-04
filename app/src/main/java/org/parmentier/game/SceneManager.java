/** ********************************************************************************
 * Serves as the central manager for handling different scenes in the game, such as the main menu,
 * level selection, and gameplay scenes. It maintains a stack of scenes, allowing for easy navigation
 * between them by pushing new scenes onto the stack or popping existing ones.
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
import java.util.Stack;
    
/**
 * Serves as the central manager for handling different scenes in the game, such as the main menu,
 * level selection, and gameplay scenes. It maintains a stack of scenes, allowing for easy navigation
 * between them by pushing new scenes onto the stack or popping existing ones.
 */
public class SceneManager {
    /**
     * The stack of scenes currently active in the game.
     * The top of the stack represents the currently active scene,
     * while the rest of the stack allows for returning to previous scenes when needed.
     */
    Stack<Scene> scenes;

    /**
     * Constructs a new SceneManager with an empty stack of scenes. This initializes the scene management system,
     * allowing the game to start with no active scenes and enabling the addition of scenes as the game progresses.
     */
    public SceneManager() {
        this.scenes = new Stack<>();
    }

    /**
     * Pushes a new scene onto the stack, making it the active scene.
     * @param scene The scene to be added to the stack and set as the active scene.
     */
    public void pushScene(Scene scene) {
        this.scenes.push(scene);
        Game.getInstance().refresh();
    }

    /**
     * Pops the current active scene from the stack, returning to the previous scene if one exists.
     * @return The scene that was removed from the stack, or null if the stack is empty.
     */
    public Scene popScene() {
        Scene removedScene = this.scenes.pop();
        removedScene.destroy();
        Game.getInstance().refresh();
        return removedScene;
    }

    /**
     * Retrieves the currently active scene, which is the scene at the top of the stack.
     * @return The currently active scene, or null if the stack is empty.
     */
    public Scene activeScene() {
        return this.scenes.peek();
    }

    /**
     * Checks if the stack of scenes is empty, indicating that there are no active scenes in the game.
     */
    public boolean isEmpty() {
        return this.scenes.isEmpty();
    }

    /**
     * Clears all scenes from the stack, effectively resetting the scene management system.
     */
    public void clearScenes() {
        while (!scenes.isEmpty()) {
            popScene();
        }
    }
}

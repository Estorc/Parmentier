/** ********************************************************************************
 * Represents the main application class for the Parmentier puzzle game, responsible for initializing the game and launching the main menu.
 ***********************************************************************************
 * @author Estorc
 * @version v1.0
 * @package org.parmentier
 * @copyright Copyright (c) 2026 Parmentier's team GNU GENERAL PUBLIC LICENSE.
 **********************************************************************************/
/*                             This file is part of
 *                                  Parmentier
 *           (https://github.com/Estorc/Projet-Genie-Logiciel-L3-Parmentier)
 ***********************************************************************************/

package org.parmentier;

import org.parmentier.game.Game;
import org.parmentier.game.MainMenu;

import javafx.application.Application;

/**
 * Main application class for the Parmentier puzzle game.
 */
public class App extends Application {

    /**
     * The main entry point of the Parmentier window.
     * @brief primaryStage Render context of the window.
     */
    @Override
    public void start(javafx.stage.Stage primaryStage) {
        Game game = new Game();
        game.getSceneManager().pushScene(new MenuLogin(game));
        game.start(primaryStage);
    }

    /**
     * The main entry point of the Parmentier application.
     * @param args Command-line arguments passed to the application (not used in this implementation).
     */
    public static void main(String[] args) {
        System.out.println("Welcome to Parmentier!");
        launch(args);
    }
}

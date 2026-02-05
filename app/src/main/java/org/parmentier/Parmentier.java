/** ********************************************************************************
 * Parmentier - A puzzle game based on bridges between islands.
 ***********************************************************************************
 * @author Estorc
 * @version v1.0
 * @package org.parmentier
 * @copyright Copyright (c) 2026 Parmentier MIT License.
 **********************************************************************************/
/*                             This file is part of
 *                                  Parmentier
 *           (https://github.com/Estorc/Projet-Genie-Logiciel-L3-Parmentier)
 ***********************************************************************************
 * Copyright (c) 2026 Parmentier.
 * This file is licensed under the MIT License.
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ***********************************************************************************/

package org.parmentier;

import org.parmentier.game.Game;
import org.parmentier.game.Level;
import org.parmentier.game.MainMenu;
import org.parmentier.game.Settings;

import javafx.application.Application;

/**
 * Main application class for the Parmentier puzzle game.
 */
public class Parmentier extends Application {

    @Override
    public void start(javafx.stage.Stage primaryStage) {
        Game game = new Game();
        game.getSceneManager().pushScene(new Settings());
        game.start(primaryStage);
    }

    public static void main(String[] args) {
        System.out.println("Welcome to Parmentier!");
        launch(args);
    }
}

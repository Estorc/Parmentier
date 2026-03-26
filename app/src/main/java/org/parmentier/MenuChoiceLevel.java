/** ********************************************************************************
 * Represents a menu in the Parmentier puzzle game, serving as a base class for different
 * types of menus such as the main menu, settings menu, and pause menu.
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

import java.io.IOException;
import java.net.URL;

import org.parmentier.game.Game;
import org.parmentier.game.Level;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

/**
 * Represents a menu in the Parmentier puzzle game, serving as a base class for different
 * types of menus such as the main menu, settings menu, and pause menu.
 */
public class MenuChoiceLevel extends Menu{

    /**
     * Initializes the menu for level selection by loading the corresponding FXML layout and adding it to the provided StackPane.
     * @param interfaceLevelChoice The StackPane to which the level selection menu will be added. This pane is cleared before adding the new menu layout.
     * @exception IOException If there is an error loading the FXML file, an error message is printed to the standard error stream.
     */
    @Override
    public void initialize(StackPane interfaceLevelChoice) {
        try {
            URL fxmlLocation = getClass().getResource("/SceneBuilderLevel.fxml");
            
            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();
            
            interfaceLevelChoice.getChildren().clear();
            interfaceLevelChoice.getChildren().add(root);
        } catch (IOException e) {
            System.err.println("Erreur : " + e.getMessage());
        }
    }

    /**
     * Default destructor for the MenuChoiceLevel class. Since there are no specific resources to clean up, this method is left empty.
     */
    @Override
    public void destroy() {
        //
    }

    /**
     * Renders the menu for level selection. Since this menu is primarily defined by its FXML layout, there is no custom rendering logic needed in this method.
     * @param gc The GraphicsContext used for rendering.
     * @param uiLayer The StackPane that serves as the UI layer for the menu.
     */
    @Override
    public void render(GraphicsContext gc, StackPane uiLayer) {
        // Rien à dessiner pour le menu de choix de niveau
    }

    /**
     * Updates the menu for level selection. Since this menu does not have dynamic elements that require updating, this method is left empty.
     * @param deltaTime The time elapsed since the last update, which is not used in this menu.
     * @param interfaceLevelChoice The StackPane that serves as the UI layer for the menu, which is not modified in this method.
     */
    @Override
    public void update(double deltaTime, StackPane interfaceLevelChoice) {
        // Aucune mise à jour nécessaire pour le menu de choix de niveau
    }

    /**
     * Handles mouse click events on the level selection menu.
     * @param event The MouseEvent triggered by the user's click on the menu.
     */
    @FXML
    public void manageImageClick(MouseEvent event) {
        Game.getInstance().getSceneManager().pushScene(new Level(((javafx.scene.Node) event.getSource()).getId()));
    }


}

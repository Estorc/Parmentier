/** ********************************************************************************
 * Represents the Modes Menu scene in the Parmentier game.
 * This class is responsible for displaying the different game modes available to the player,
 * such as the tutorial and levels. It provides a user interface for selecting a game mode and
 * serves as a central hub for navigating to different parts of the game.
 * The ModesMenu class implements the Scene interface, allowing it to be integrated seamlessly
 * into the game's scene management system. It includes methods for updating the scene, rendering
 * visual elements, initializing the user interface, and destroying the scene when it is no longer needed.
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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Represents the Modes Menu scene in the Parmentier game.
 * This class is responsible for displaying the different game modes available to the player,
 * such as the tutorial and levels. It provides a user interface for selecting a game mode and
 * serves as a central hub for navigating to different parts of the game.
 * The ModesMenu class implements the Scene interface, allowing it to be integrated seamlessly
 * into the game's scene management system. It includes methods for updating the scene, rendering
 * visual elements, initializing the user interface, and destroying the scene when it is no longer needed.
 */
public class ModesMenu implements org.parmentier.game.Scene {

    @Override
    public void update(double deltaTime, StackPane uiLayer) {
        //
    }

    @Override
    public void render(GraphicsContext gc, StackPane uiLayer) {
        //
    }

    @Override
    public void initialize(StackPane uiLayer) {

    VBox general = new VBox();

	Label title = new Label("Modes de Jeux");
	
	VBox btnZone = new VBox();

	Button didacticiel = new Button("Didacticiel");
	Button niveaux = new Button("niveaux");	

	uiLayer.getStylesheets().add(getClass().getResource("/lightMode.css").toExternalForm());
        general.setSpacing(20);
        btnZone.setSpacing(10);

	title.getStyleClass().add("title-label");
	didacticiel.getStyleClass().add("button");
        niveaux.getStyleClass().add("button");

        btnZone.setAlignment(javafx.geometry.Pos.CENTER);
        general.setAlignment(javafx.geometry.Pos.TOP_CENTER);

        btnZone.getChildren().add(didacticiel);
        btnZone.getChildren().add(niveaux);
        
        general.getChildren().add(title);
        general.getChildren().add(btnZone);
        uiLayer.getChildren().add(general);
        

    }

    /**
     * Default destructor for the ModesMenu class. Since there are no specific resources to clean up, this method is left empty.
     */
    @Override
    public void destroy() {
        //
    }

}

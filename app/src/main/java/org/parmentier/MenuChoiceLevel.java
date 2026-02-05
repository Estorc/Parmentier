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

public class MenuChoiceLevel extends Menu{

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

    @Override
    public void render(GraphicsContext gc, StackPane uiLayer) {
        // Rien à dessiner pour le menu de choix de niveau
    }

    @Override
    public void update(double deltaTime, StackPane interfaceLevelChoice) {
        // Aucune mise à jour nécessaire pour le menu de choix de niveau
    }

    @FXML
    public void manageImageClick(MouseEvent event) {
        Game.getInstance().getSceneManager().pushScene(new Level());
    }


}
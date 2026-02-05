package org.parmentier;

import org.parmentier.game.Game;
import org.parmentier.game.Level;
import org.parmentier.game.Scene;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.canvas.GraphicsContext;
import java.io.IOException;
import javafx.scene.input.MouseEvent;
import java.net.URL;

public class MenuChoiceLevel extends Menu{

    @Override
    public void levelScene(StackPane interfaceLevelChoice) {
        try {
            URL fxmlLocation = getClass().getResource("/SceneBuilderLevel.fxml");
            
            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();
            
            interfaceLevelChoice.getChildren().clear();
            interfaceLevelChoice.getChildren().add(root);
        } catch (IOException e) {
            System.err.println("Erreur : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void manageImageClick(MouseEvent event) {
        javafx.scene.Node source = (javafx.scene.Node) event.getSource();
        Game.getInstance().getSceneManager().pushScene(new Level());
    }


}
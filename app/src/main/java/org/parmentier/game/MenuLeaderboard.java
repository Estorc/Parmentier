package org.parmentier.game;


import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class MenuLeaderboard implements org.parmentier.game.Scene {
    String level;

    MenuLeaderboard(String level){

        this.level = level;

    }



    @Override
    public void initialize(StackPane uiLayer){
        uiLayer.getChildren().clear();

        GridPane layout = new GridPane();
        layout.getStyleClass().add("cachemisere");
        layout.setHgap(20);
        layout.setVgap(10);

        layout.setAlignment(javafx.geometry.Pos.CENTER);
        Label titleLabel = new Label("Niveau X");
        Label leaderboardLabel = new Label("Classement des meilleurs joueurs");
        Label scoreLabel = new Label("Score : ");
        Button closeLeaderboardButton = new Button("Fermer le classement");
        Button mainMenuButton = new Button("Menu principal");
        //Button sbButton = new Button("Classement");
        GridPane.setHalignment(titleLabel, javafx.geometry.HPos.CENTER);
        GridPane.setHalignment(leaderboardLabel, javafx.geometry.HPos.CENTER);
        GridPane.setHalignment(scoreLabel, javafx.geometry.HPos.CENTER);
        GridPane.setHalignment(closeLeaderboardButton, javafx.geometry.HPos.CENTER);
        GridPane.setHalignment(mainMenuButton, javafx.geometry.HPos.CENTER);
        //GridPane.setHalignment(sbButton, javafx.geometry.HPos.CENTER);


        titleLabel.getStyleClass().add("title-label");
        leaderboardLabel.getStyleClass().add("textUser");
        scoreLabel.getStyleClass().add("textUser");
        closeLeaderboardButton.getStyleClass().add("button");
        mainMenuButton.getStyleClass().add("button");
        //sbButton.getStyleClass().add("button");
        layout.add(titleLabel, 2, 1);
        layout.add(leaderboardLabel, 2, 2);
        layout.add(scoreLabel, 2, 3);
        layout.add(closeLeaderboardButton, 2, 4);
        layout.add(mainMenuButton, 2, 5);
        //layout.add(nextLevelButton, 3, 4);
        //layout.add(mainMenuButton, 1, 4);
        //layout.add(sbButton, 2, 4);
        uiLayer.getChildren().add(layout);

        closeLeaderboardButton.setOnMouseClicked(e -> {
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
        }

    @Override
    public void update(double deltaTime, StackPane uiLayer){
        //
    }

    @Override
    public void render(GraphicsContext gc, StackPane uiLayer) {
        //
    }

    @Override
    public void destroy() {
        //
    }
}

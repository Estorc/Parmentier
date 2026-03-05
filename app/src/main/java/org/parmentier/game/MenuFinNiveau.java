package org.parmentier.game;

import org.parmentier.MenuChoiceLevel;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class MenuFinNiveau implements org.parmentier.game.Scene {

    private String deltaTime;
    private Integer score;

    public MenuFinNiveau(String time, Integer score) {
        this.deltaTime = time;
        this.score = score;
    }

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
        uiLayer.getChildren().clear();

        GridPane layout = new GridPane();
        layout.getStyleClass().add("cachemisere");
        layout.setHgap(20);
        layout.setVgap(10);

        layout.setAlignment(javafx.geometry.Pos.CENTER);
        Label titleLabel = new Label("Vous avez gagné !");
        Label timeLabel = new Label("Temps : " + deltaTime);
        Label scoreLabel = new Label("Score : " + score);
        Button nextLevelButton = new Button("Niveau suivant");
        Button mainMenuButton = new Button("Menu principal");
        Button sbButton = new Button("Classement");
        GridPane.setHalignment(titleLabel, javafx.geometry.HPos.CENTER);
        GridPane.setHalignment(timeLabel, javafx.geometry.HPos.CENTER);
        GridPane.setHalignment(scoreLabel, javafx.geometry.HPos.CENTER);
        GridPane.setHalignment(nextLevelButton, javafx.geometry.HPos.CENTER);
        GridPane.setHalignment(mainMenuButton, javafx.geometry.HPos.CENTER);
        GridPane.setHalignment(sbButton, javafx.geometry.HPos.CENTER);


        titleLabel.getStyleClass().add("title-label");
        timeLabel.getStyleClass().add("textUser");
        scoreLabel.getStyleClass().add("textUser");
        nextLevelButton.getStyleClass().add("button");
        mainMenuButton.getStyleClass().add("button");
        sbButton.getStyleClass().add("button");
        layout.add(titleLabel, 2, 1);
        layout.add(timeLabel, 2, 2);
        layout.add(scoreLabel, 2, 3);
        layout.add(nextLevelButton, 3, 4);
        layout.add(mainMenuButton, 1, 4);
        layout.add(sbButton, 2, 4);
        uiLayer.getChildren().add(layout);

        nextLevelButton.setOnMouseClicked(e -> {
            Game.getInstance().getSceneManager().pushScene(new MenuChoiceLevel());
        });
        mainMenuButton.setOnMouseClicked(e -> {
            Game.getInstance().getSceneManager().pushScene(new MainMenu());
        });
    }

    /**
     * Default destructor for the MainMenu class. Since there are no specific resources to clean up, this method is left empty.
     */
    @Override
    public void destroy() {
        //
    }
}

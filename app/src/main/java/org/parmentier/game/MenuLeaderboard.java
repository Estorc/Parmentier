package org.parmentier.game;

import org.parmentier.game.Leaderboard;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;

public class MenuLeaderboard implements org.parmentier.game.Scene {
    String level;
    Leaderboard leaderboard;

    public MenuLeaderboard(String level) {
        this.level = level;
        leaderboard = new Leaderboard();

    }

    public void leaderboardPlaceHolder() {
        for (int i = 0; i < 20; i++) {
            Performance temp = new Performance();
            temp.setPlaceHolder();
            this.leaderboard.addPerformance(temp);
        }
        this.leaderboard.afficherLeaderboard();
    }

    @Override
    public void initialize(StackPane uiLayer) {
        uiLayer.getChildren().clear();
        if (!uiLayer.getStyleClass().contains("main-menu-bg")) {
            uiLayer.getStyleClass().add("main-menu-bg");
        }
        this.leaderboard = loadLeaderBoard();
        GridPane layout = new GridPane();
        layout.getStyleClass().add("cachemisere");
        layout.setHgap(20);
        layout.setVgap(10);

        layout.setAlignment(javafx.geometry.Pos.CENTER);

        String numberOnly = this.level.replaceAll("[^0-9]", "");
        int levelNumber = Integer.parseInt(numberOnly);

        Label titleLabel = new Label("Niveau " + levelNumber);
        Label leaderboardLabel = new Label("CLASSEMENT DES MEILLEURS JOUEURS");
        Label scoreLabel = new Label("Score : ");
        Button closeLeaderboardButton = new Button("Fermer le classement");
        Button mainMenuButton = new Button("Menu principal");
        // Button sbButton = new Button("Classement");
        GridPane.setHalignment(titleLabel, javafx.geometry.HPos.CENTER);
        GridPane.setHalignment(leaderboardLabel, javafx.geometry.HPos.CENTER);
        GridPane.setHalignment(scoreLabel, javafx.geometry.HPos.CENTER);
        GridPane.setHalignment(closeLeaderboardButton, javafx.geometry.HPos.CENTER);
        GridPane.setHalignment(mainMenuButton, javafx.geometry.HPos.CENTER);
        // GridPane.setHalignment(sbButton, javafx.geometry.HPos.CENTER);

        VBox leaderboardVBox = new VBox(10);
        leaderboardVBox.setAlignment(javafx.geometry.Pos.CENTER);

        javafx.scene.layout.HBox header = new javafx.scene.layout.HBox(30);
        header.setAlignment(javafx.geometry.Pos.CENTER);
        header.getStyleClass().add("border-bottom-only");

        Label nomH = new Label("Nom");
        nomH.setPrefWidth(120);
        nomH.setAlignment(javafx.geometry.Pos.CENTER);
        nomH.getStyleClass().add("textUser");

        Label tempsH = new Label("Temps");
        tempsH.setPrefWidth(120);
        tempsH.setAlignment(javafx.geometry.Pos.CENTER);
        tempsH.getStyleClass().add("textUser");

        Label aidesH = new Label("Aides util.");
        aidesH.setPrefWidth(120);
        aidesH.setAlignment(javafx.geometry.Pos.CENTER);
        aidesH.getStyleClass().add("textUser");

        Label scoreH = new Label("Score");
        scoreH.setPrefWidth(120);
        scoreH.setAlignment(javafx.geometry.Pos.CENTER);
        scoreH.getStyleClass().add("textUser");

        Label noteH = new Label("Note");
        noteH.setPrefWidth(120);
        noteH.setAlignment(javafx.geometry.Pos.CENTER);
        noteH.getStyleClass().add("textUser");

        header.getChildren().addAll(nomH, tempsH, aidesH, scoreH, noteH);
        leaderboardVBox.getChildren().add(header);

        for (Performance p : leaderboard.getPerformances()) {
            leaderboardVBox.getChildren().add(p.affichagePerformance());
        }

        ScrollPane scrollPane = new ScrollPane(leaderboardVBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(400);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent; -fx-control-inner-background: transparent; -fx-padding: 0;");

        titleLabel.getStyleClass().add("title-label");
        leaderboardLabel.getStyleClass().add("textUser");
        scoreLabel.getStyleClass().add("textUser");
        closeLeaderboardButton.getStyleClass().add("button");
        mainMenuButton.getStyleClass().add("button");

        layout.add(titleLabel, 2, 1);
        layout.add(leaderboardLabel, 2, 2);
        layout.add(scrollPane, 2, 3);
        // sbButton.getStyleClass().add("button");

        layout.add(scoreLabel, 2, 4);
        layout.add(closeLeaderboardButton, 2, 5);
        layout.add(mainMenuButton, 2, 6);
        // layout.add(nextLevelButton, 3, 4);
        // layout.add(mainMenuButton, 1, 4);
        // layout.add(sbButton, 2, 4);
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
    public void update(double deltaTime, StackPane uiLayer) {
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

    public Leaderboard loadLeaderBoard() {
        Leaderboard lb = new Leaderboard();
        String filename = org.parmentier.Parmentier.SAVES_DIR + this.level + "_leaderboard.sav";
        java.io.File file = new java.io.File(filename);
        if (!file.exists())
            return lb;

        try {
            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 3) {
                    Performance p = new Performance();
                    p.setNameTag(parts[0]);
                    try {
                        p.setTimeElapsedSeconds(Integer.parseInt(parts[1]));
                        p.setScore(Integer.parseInt(parts[2]));
                        if (parts.length >= 4 && parts[3].length() > 0) {
                            p.setNote(parts[3].charAt(0));
                        }
                    } catch (NumberFormatException nfe) {
                    }
                    lb.addPerformance(p);
                }
            }
            reader.close();
        } catch (java.io.IOException e) {
            System.err.println("Erreur lors du chargement du leaderboard : " + e.getMessage());
        }
        return lb;
    }
}

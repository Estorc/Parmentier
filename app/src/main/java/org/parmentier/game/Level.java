/** ********************************************************************************
 * Represents a level in the Parmentier puzzle game, managing the game state, user interactions,
 * and rendering of the level.
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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.parmentier.hint.Hint;
import org.parmentier.hint.HintBulb;
import org.parmentier.level.Bridge;
import org.parmentier.level.GridData;
import org.parmentier.level.Node;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;


/**
 * Represents a level in the Parmentier puzzle game, managing the game state, user interactions,
 * and rendering of the level.
 */
public class Level implements org.parmentier.game.Scene {
    private Label hintLabel;
    /**
     * The horizontal shift applied to the level's rendering, used to center the level on the canvas.
     */
    private int xShift = 0;

    /**
     * The vertical shift applied to the level's rendering, used to center the level on the canvas.
     */
    private int yShift = 0;

    /** 
     * The scale factor applied to the level's rendering, used to scale the level to fit the canvas size.
     */
    private double scaleFactor = 1.0;

    /**
     * The currently selected bridge, which is highlighted for user interaction.
     */
    private Bridge selectedBridge = null;

    /**
     * The list of active bridges in the level, representing the current state of the puzzle.
     */
    private ArrayList<Bridge> activeBridges;

    /**
     * The list of nodes in the level, representing the key elements of the puzzle that must be connected by bridges.
     */
    private ArrayList<Node> nodes;

    /**
     * The grid data representing the layout of the level, including the nodes and potential bridges.
     */
    private GridData level;

    /**
     * The Timeline object used as a stopwatch to track the elapsed time since the level started.
     */
    private Timeline stopwatch;

    /**
     * The number of seconds that have elapsed since the level started, used to update the stopwatch label.
     */
    private int timeElapsed = 0;

    private int score = 0;
    private int timeLowMinutes = 5;
    private int timeHighMinutes = 30;
    private double timeCoeffMin = 0.67;
    private float timeCoeffMax = 1;

    /**
     * Flag to track whether the level has been completed, preventing the save file
     * from being recreated when destroy() is called after level completion.
     */
    private boolean isCompleted = false;

    /**
     * The label used to display the elapsed time on the UI. This label is updated by the stopwatch Timeline to show
     * the current elapsed time in minutes and seconds.
     */
    private Label stopwatchLabel;

    private Button checkButton;
    private Button helpButton;
    private Button backButton = new Button("Retour");

    public Level(String levelName) {
        // Try loading saved state first, if it fails load the level data from the resource file
        
        boolean loadedFromSave = false;
        InputStream input;
        try {
          java.nio.file.Path savePath = java.nio.file.Paths.get("saves/" + Game.getInstance().getCurrentUserName() + "_" + levelName + ".sav");
          if (java.nio.file.Files.exists(savePath)) {
              System.out.println("Chargement de la sauvegarde pour le niveau : " + levelName);
              input = java.nio.file.Files.newInputStream(savePath);
              this.level = new GridData(input, levelName);
              loadedFromSave = true;
          }
        } catch (IOException e) {
          System.err.println("Erreur lors du chargement de la sauvegarde : " + e.getMessage());
        }
        if (!loadedFromSave) {
          try {
            input = getClass().getResourceAsStream("/levels/" + levelName + ".lvl");
            this.level = new GridData(input, levelName);
          } catch (Exception e) {
            System.err.println("Erreur lors du chargement du niveau : " + e.getMessage());
            this.level = null;
            throw new RuntimeException("Failed to load level: " + levelName);
          }
        }

        // restore saved stopwatch time if present
        if (loadedFromSave && this.level != null) {
            this.timeElapsed = this.level.getsavedChrono();
        }
    };

    /**
     * Attempts to connect two nodes with a bridge, checking for valid connections and potential intersections with existing bridges.
     * @param from The starting node of the bridge.
     * @param to The ending node of the bridge.
     * @return true if the connection is valid and has been made, false otherwise.
     */
    public boolean tryConnect(Node from, Node to) {
        // Check if from and to are aligned
        Bridge bridge = from.getBridge(to);
        if (bridge == null) {
            return false;
        }
        // Ensure correct orientation
        from = bridge.getFrom();
        to = bridge.getTo();
        for (Bridge activeBridge : activeBridges) {
            Node fromActive = activeBridge.getFrom();
            Node toActive = activeBridge.getTo();
            for (int i = 0; i < 2; i++) {
                if (from == fromActive || from == toActive || to == fromActive || to == toActive) {
                    continue;
                }
                if (org.parmentier.math.Segment.isIntersecting(from.getPosition(), to.getPosition(), fromActive.getPosition(), toActive.getPosition())) {
                    return false;
                }
            }
                
        }
        Bridge bridgeBetween = from.getBridge(to);
        if (selectedBridge != bridgeBetween) {
            selectedBridge = bridgeBetween;
        }
        return true;
    }

    /**
     * Attempts to connect the closest node to the given coordinates with a bridge, based on the user's mouse position.
     * @param x The x-coordinate of the mouse position.
     * @param y The y-coordinate of the mouse position.
     */
    public void tryConnectClosest(double x, double y) {
        List<Node> sortedNodes = nodes.stream().sorted((n1, n2) -> {
            double d1 = Math.hypot(n1.getCanvasPosition().getX() - x, n1.getCanvasPosition().getY() - y);
            double d2 = Math.hypot(n2.getCanvasPosition().getX() - x, n2.getCanvasPosition().getY() - y);
            return Double.compare(d1, d2);
        }).toList();
        for (int i = 0; i < Math.min(2, sortedNodes.size()); i++) {
            Node from = sortedNodes.get(i);
            double dx = from.getCanvasPosition().getX() - x;
            double dy = from.getCanvasPosition().getY() - y;
            double angle = Math.atan2(dy, dx);
            Bridge bridge;
            if (Math.abs(Math.cos(angle)) > Math.abs(Math.sin(angle))) {
                if (Math.cos(angle) < 0) {
                    bridge = from.getBridge(Bridge.Direction.RIGHT);
                } else {
                    bridge = from.getBridge(Bridge.Direction.LEFT);
                }
            } else {
                if (Math.sin(angle) > 0) {
                    bridge = from.getBridge(Bridge.Direction.TOP);
                } else {
                    bridge = from.getBridge(Bridge.Direction.BOTTOM);
                }
            }
            if (bridge == null) {
                continue;
            }
            Node closestNode;
            if (bridge.getFrom() == from) {
                closestNode = bridge.getTo();
            } else {
                closestNode = bridge.getFrom();
            }
            if (tryConnect(from, closestNode)) {
                break;
            }
        }
    }

    /**
     * Starts the stopwatch to track the elapsed time since the level started. This method initializes a Timeline that updates every second,
     * incrementing the timeElapsed counter and updating the stopwatchLabel to display the current elapsed time in minutes and seconds format.
     */
    private void startStopwatch() {
        stopwatchLabel = new Label("Temps écoulé: " + getStopwatchTime());
        stopwatchLabel.setTextFill(Color.GRAY);
        stopwatchLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        stopwatch = new Timeline(
            new KeyFrame(Duration.seconds(1), e -> {
                timeElapsed++;
                int minutes = timeElapsed / 60;
                int seconds = timeElapsed % 60;
                stopwatchLabel.setText(String.format("Temps écoulé: %02d:%02d", minutes, seconds));
            })
        );
        stopwatch.setCycleCount(Timeline.INDEFINITE);
        stopwatch.play();
    }

    private String getStopwatchTime() {
        int minutes = timeElapsed / 60;
        int seconds = timeElapsed % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private void calculateScore(){

        int timeLow = this.timeLowMinutes * 60;

        int timeHigh = this.timeHighMinutes * 60;

        double timeBornedShifted = Math.clamp(timeElapsed, timeLow, timeHigh) - timeLow;

        // We need a value between 0 et 1 to get our final coeff

        double timeNormalized = timeBornedShifted / (timeHigh - timeLow);

        // The coefficient of time is normalized between our minimal coefficient and our maximal coefficient.

        double timeCoeff = (timeNormalized - timeCoeffMin) + timeCoeffMax;

        // J'attends que le nombre de ponts attendus soit disponible.

        //int score = (int)((this.activeBridges.stream().reduce(0, (a,b) -> , combiner) * 1000) * timeCoeff);

        

    }

        
        

    private char calculateNote(){

        char note;

        int scoreMax = 25000;

        double scoreMin = 25000 * timeCoeffMin;

        double etendue = scoreMax - scoreMin;

        if((scoreMax - score) >= (etendue * 0.90)) {note = 'S';}

        else if (scoreMax - score >= (etendue * 0.80)) note = 'A';

        else if (scoreMax - score >= (etendue * 0.70)) note = 'B';

        else if (scoreMax - score >= (etendue * 0.60)) note = 'C';

        else if (scoreMax - score >= etendue* 0.40) note = 'D';

        else note = 'F';

        return note;

    }

    private void helpButton(){
        helpButton = new Button("Aide");
        helpButton.getStyleClass().add("menu-button");
        StackPane.setMargin(helpButton, new javafx.geometry.Insets(10));

        hintLabel = new Label();
        hintLabel.getStyleClass().add("hint-banner");
        hintLabel.setVisible(false);
        hintLabel.setWrapText(true);
        hintLabel.setMaxWidth(300);
        StackPane.setAlignment(hintLabel, javafx.geometry.Pos.CENTER_RIGHT);
        StackPane.setMargin(hintLabel, new javafx.geometry.Insets(0, 30, 0, 0));

        helpButton.setOnMouseClicked(e -> {
            org.parmentier.game.Audio.playClickSound();
            if (hintLabel.isVisible()) {
                hintLabel.setVisible(false);
            } else {
                HintBulb hintBulb = org.parmentier.hint.HintBulb.get();
                Hint hint = hintBulb.getRandomHint(level);
                String text = (hint != null) ? hint.getHintText() : "Aucune aide disponible.";
                hintLabel.setText(text);
                hintLabel.setVisible(true);
            }
        });
    }

    /**
     * Initializes the level scene by setting up the UI elements, loading the level data, and configuring user interactions.
     * @param uiLayer The StackPane that serves as the UI layer for the level scene.
     */
    @Override
    public void initialize(StackPane uiLayer) {
        System.out.println("Initializing level scene...");
        
        org.parmentier.hint.HintBulb.get().RenableHints(); // reenable hints for new level
        
        uiLayer.getChildren().clear();
        javafx.scene.canvas.Canvas canvas = Game.getInstance().getCanvas();
        if (!uiLayer.getChildren().contains(canvas)) {
            uiLayer.getChildren().add(0, canvas);
        }

        this.nodes = new ArrayList<>();
        this.activeBridges = new ArrayList<>();

       
        startStopwatch();
        helpButton();
        uiLayer.getChildren().add(hintLabel);

        StackPane.setAlignment(stopwatchLabel, javafx.geometry.Pos.TOP_CENTER);
        StackPane.setAlignment(helpButton, javafx.geometry.Pos.TOP_RIGHT);
        StackPane.setAlignment(backButton, javafx.geometry.Pos.BOTTOM_RIGHT);
        StackPane.setMargin(backButton, new javafx.geometry.Insets(10));
        backButton.getStyleClass().add("menu-button");
        backButton.setOnMouseClicked(e -> {
            org.parmentier.game.Audio.playClickSound();
            Game.getInstance().getSceneManager().popScene();
        });
        uiLayer.getChildren().add(helpButton);
        uiLayer.getChildren().add(backButton);
        uiLayer.getChildren().add(stopwatchLabel);


        for (int i = 0; i < level.getWidth(); i++) {
            for (int j = 0; j < level.getHeight(); j++) {
                if (level.getNodeAt(i, j) != null) {
                    Node node = level.getNodeAt(i, j);
                    this.nodes.add(node);
                    if (!node.getBridges().isEmpty()) {
                        for (Bridge bridge : node.getBridges()) if (bridge.getState() > 0) activeBridges.add(bridge); 
                    }
                }
            }
        }


        uiLayer.setOnMouseMoved(e -> {
            tryConnectClosest((e.getX() - xShift)/scaleFactor, (e.getY() - yShift)/scaleFactor);
        });


        uiLayer.setOnMouseClicked(e -> {
            selectedBridge.toggleState();
            if (selectedBridge.getState() == 0) {
                activeBridges.remove(selectedBridge);
            } else if (!activeBridges.contains(selectedBridge)) {
                activeBridges.add(selectedBridge);
            }
        });
        Button check = new Button("Vérifier");
        check.getStyleClass().add("menu-button");
        StackPane.setAlignment(check, javafx.geometry.Pos.BOTTOM_CENTER);
        StackPane.setMargin(check, new javafx.geometry.Insets(10));

        Label checkLabel = new Label();
        checkLabel.getStyleClass().add("hint-banner");
        checkLabel.setVisible(false);
        checkLabel.setWrapText(true);
        checkLabel.setMaxWidth(500);
        StackPane.setAlignment(checkLabel, javafx.geometry.Pos.BOTTOM_CENTER);
        StackPane.setMargin(checkLabel, new javafx.geometry.Insets(0, 0, 90, 0));

        uiLayer.getChildren().add(checkLabel);
        uiLayer.getChildren().add(check);

        check.setOnMouseClicked(e -> {
            org.parmentier.game.Audio.playClickSound();
            if (checkLabel.isVisible()) {
                checkLabel.setVisible(false);
            } else {
                boolean status = level.checkState();
                if (status) {
                    isCompleted = true;
                    Game.getInstance().getSceneManager().popScene();
                    Game.getInstance().getSceneManager().pushScene(new MenuFinNiveau(getStopwatchTime(), 42));
                    // supprimer save
                    java.nio.file.Path path = java.nio.file.Paths.get("saves/" + Game.getInstance().getCurrentUserName() + "_" + level.getName() + ".sav");
                    try {
                        java.nio.file.Files.deleteIfExists(path);
                    } catch (IOException ex) {
                        System.err.println("Erreur lors de la suppression de la sauvegarde : " + ex.getMessage());
                    }
                } else {
                    String err_msg;
                    int err = level.countErrors();
                    if (err > 0)
                        err_msg = "La grille comporte " + Integer.toString(err) + " erreurs. Continuez à essayer !";
                    else
                        err_msg = "Vous n'avez fait aucune erreur. Continuez comme ça !";
                    checkLabel.setText(err_msg);
                    checkLabel.setVisible(true);
                    Timeline hideTimeline = new Timeline(new KeyFrame(Duration.seconds(3), ev -> checkLabel.setVisible(false)));
                    hideTimeline.setCycleCount(1);
                    hideTimeline.play();
                }
            }
        });
    }

    /**
     * Auto save and stop the stopwatch when the level scene is destroyed.
     */
    @Override
    public void destroy() {
        if (stopwatch != null) {
            stopwatch.stop();
        }
        if (!isCompleted) {
            level.saveState(timeElapsed);
        }
        level.checkState();
    }

    /**
     * Updates the level scene based on the elapsed time and user interactions.
     */
    @Override
    public void update(double deltaTime, StackPane uiLayer) {
        //
    }

    /**
     * Renders the level scene on the canvas, drawing the background, nodes, and bridges based on the current game state.
     */
    @Override
    public void render(GraphicsContext gc, StackPane uiLayer) {
        gc.clearRect(-Game.WIDTH, -Game.HEIGHT, Game.WIDTH*3, Game.HEIGHT*3);
        scaleFactor = gc.getCanvas().getHeight() / Game.HEIGHT;
        this.xShift = (int) (gc.getCanvas().getWidth() - level.getWidth() * Node.SIZE * scaleFactor) / 2;
        this.yShift = (int) (gc.getCanvas().getHeight() - level.getHeight() * Node.SIZE * scaleFactor) / 2;

        gc.setTransform(scaleFactor, 0, 0, scaleFactor, xShift, yShift);
        if (selectedBridge != null) {
            selectedBridge.draw(gc, true);
        }
        for (Bridge bridge : activeBridges) {
            bridge.draw(gc, false);
        }
        for (Node node : nodes) {
            node.draw(gc);
        }
    }
    
}

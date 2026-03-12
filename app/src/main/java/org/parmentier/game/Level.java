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

import java.util.ArrayList;
import java.util.List;

import org.parmentier.level.Bridge;
import org.parmentier.level.GridData;
import org.parmentier.level.Node;
import org.parmentier.hint.HintBulb;
import org.parmentier.hint.Hint;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.layout.HBox;
import javafx.util.Duration;


/**
 * Represents a level in the Parmentier puzzle game, managing the game state, user interactions,
 * and rendering of the level.
 */
public class Level implements org.parmentier.game.Scene {
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
    private int elapsedSeconds = 0;

    /**
     * The label used to display the elapsed time on the UI. This label is updated by the stopwatch Timeline to show
     * the current elapsed time in minutes and seconds.
     */
    private Label stopwatchLabel;

    private Button checkButton;
    private Button helpButton;

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
     * incrementing the elapsedSeconds counter and updating the stopwatchLabel to display the current elapsed time in minutes and seconds format.
     */
    private void startStopwatch() {
        stopwatchLabel = new Label("Temps écoulé: 00:00");
        stopwatchLabel.setTextFill(Color.GRAY);
        stopwatchLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        stopwatch = new Timeline(
            new KeyFrame(Duration.seconds(1), e -> {
                elapsedSeconds++;
                int minutes = elapsedSeconds / 60;
                int seconds = elapsedSeconds % 60;
                stopwatchLabel.setText(String.format("Temps écoulé: %02d:%02d", minutes, seconds));
            })
        );
        stopwatch.setCycleCount(Timeline.INDEFINITE);
        stopwatch.play();
    }

    private String getStopwatchTime() {
        int minutes = elapsedSeconds / 60;
        int seconds = elapsedSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private void helpButton(){
        helpButton = new Button("Aide");
        helpButton.getStyleClass().add("button");
        StackPane.setMargin(helpButton, new javafx.geometry.Insets(10));
        helpButton.setOnMouseClicked(e -> {
            HintBulb hintBulb = org.parmentier.hint.HintBulb.get();
            Hint hint = hintBulb.getRandomHint(level);
            if (hint != null) {
                Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
                alert.setTitle("Aide");
                alert.setHeaderText(null);
                alert.setContentText(hint.getHintText());
                alert.showAndWait();
            } else {
                Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
                alert.setTitle("Aide");
                alert.setHeaderText(null);
                alert.setContentText("Aucune aide disponible.");
                alert.showAndWait();
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
        uiLayer.getChildren().clear();

        this.nodes = new ArrayList<>();
        this.activeBridges = new ArrayList<>();

        

        java.nio.file.Path levelPath = java.nio.file.Paths.get("save.txt");

        level = new GridData(levelPath);
       
        startStopwatch();
        helpButton();

        StackPane.setAlignment(stopwatchLabel, javafx.geometry.Pos.TOP_CENTER);
        StackPane.setAlignment(helpButton, javafx.geometry.Pos.TOP_RIGHT);
        uiLayer.getChildren().add(helpButton);
        
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
        // My work...
        Button check = new Button("Check");
        check.getStyleClass().add("button");
        StackPane.setAlignment(check, javafx.geometry.Pos.BOTTOM_CENTER);
        uiLayer.getChildren().add(check);

        check.setOnMouseClicked(e -> {
            int status = level.checkState();
            if (status == 0) {
                Game.getInstance().getSceneManager().pushScene(new MenuFinNiveau(getStopwatchTime(), 42));
            } else {
                Alert gridStat = new Alert(Alert.AlertType.INFORMATION);
                gridStat.setTitle("Résultat de la vérification");
                gridStat.setHeaderText(null);
                gridStat.setContentText("Le niveau n'est pas encore complété. Continuez à essayer !");
                gridStat.showAndWait();
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
        level.saveState();
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

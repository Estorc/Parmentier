package org.parmentier;

import org.parmentier.level.Level;
import org.parmentier.level.Node;
import org.parmentier.level.Bridge;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.GridPane;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.scene.layout.VBox;


public class Parmentier extends Application {

    private Bridge selectedBridge = null;
    private ArrayList<Pane> bridgePanes;
    private ArrayList<Bridge> activeBridges;
    private ArrayList<Node> nodes;
    private int bridgeSize = 0;
    private Node selectedNode = null;
    private GridPane gridpane;
    private Timeline stopwatch;
    private int elapsedSeconds = 0;
    private Label stopwatchLabel;


    public static void main(String[] args) {
        launch(args);
    }

    private void traceBridge(Pane linePane, double x, double y, int width, int height, Color color, int strokeWidth) {
        Line line = new Line(x, y, x + 32 * (width-1), y + 32 * (height-1));
        line.setStroke(color);
        line.setStrokeWidth(strokeWidth);
        linePane.getChildren().add(line);
    }

    public void drawBridge(Bridge bridge, int cx, int cy, int x, int y, int width, int height) {
        Pane linePane = new Pane();
        linePane.setMinSize(32 * width, 32 * height);
        linePane.setMaxSize(32 * width, 32 * height);
        if (bridge == selectedBridge) {
            for (int i = 0; i < (bridge.getState() + 1)%Bridge.MAX_STATE; i++) {
                int state = (bridge.getState() + 1)%Bridge.MAX_STATE;
                double tx = x + (((double) (i+1)/(state+1))*2-1)*x;
                double ty = y + (((double) (i+1)/(state+1))*2-1)*y;
                traceBridge(linePane, tx, ty, width, height, Color.GRAY, 3);
            }
        }
        if (bridge.getState() != 0) {
            activeBridges.add(bridge);
            for (int i = 0; i < bridge.getState(); i++) {
                int state = bridge.getState();
                double tx = x + (((double) (i+1)/(state+1))*2-1)*x;
                double ty = y + (((double) (i+1)/(state+1))*2-1)*y;
                traceBridge(linePane, tx, ty, width, height, Color.BLACK, 1);
            }
        }
        gridpane.add(linePane, cx, cy, width, height);
        this.bridgePanes.add(linePane);
    }

    public void refresh() {
        for (Pane pane : this.bridgePanes) {
            gridpane.getChildren().remove(pane);
        }
        this.bridgePanes.clear();
        this.activeBridges.clear();
        for (Node node : this.nodes) {
            int x = node.getPosition()[0];
            int y = node.getPosition()[1];
            for (Bridge bridge : node.getBridges()) {
                if (bridge.getFrom() != node) {
                    continue;
                }
                Pane linePane;
                switch (bridge.getDirection()) {
                    case Bridge.Direction.LEFT:
                    case Bridge.Direction.RIGHT:
                        drawBridge(bridge, x+1, y, 0, 16, bridge.getLength()+1, 1);
                        break;
                    case Bridge.Direction.TOP:
                    case Bridge.Direction.BOTTOM:
                        drawBridge(bridge, x, y+1, 16, 0, 1, bridge.getLength()+1);
                        break;
                }
            }
        }
    }

    public boolean isIntersecting(Node a, Node b, Node c, Node d) {
        float aX = a.getPosition()[0];
        float aY = a.getPosition()[1];
        float bX = b.getPosition()[0];
        float bY = b.getPosition()[1];
        float cX = c.getPosition()[0];
        float cY = c.getPosition()[1];
        float dX = d.getPosition()[0];
        float dY = d.getPosition()[1];

        float denominator = ((bX - aX) * (dY - cY)) - ((bY - aY) * (dX - cX));
        float numerator1  = ((aY - cY) * (dX - cX)) - ((aX - cX) * (dY - cY));
        float numerator2  = ((aY - cY) * (bX - aX)) - ((aX - cX) * (bY - aY));

        // Collinear case
        if (denominator == 0 && numerator1 == 0 && numerator2 == 0) {
            return onSegment(aX, aY, bX, bY, cX, cY) ||
                onSegment(aX, aY, bX, bY, dX, dY) ||
                onSegment(cX, cY, dX, dY, aX, aY) ||
                onSegment(cX, cY, dX, dY, bX, bY);
        }

        // Parallel but not collinear
        if (denominator == 0) return false;

        float r = numerator1 / denominator;
        float s = numerator2 / denominator;

        return (r >= 0 && r <= 1) && (s >= 0 && s <= 1);
    }

    private boolean onSegment(float ax, float ay, float bx, float by, float px, float py) {
        return px >= Math.min(ax, bx) && px <= Math.max(ax, bx) &&
            py >= Math.min(ay, by) && py <= Math.max(ay, by);
    }

    public boolean tryConnect(Node from, Node to, boolean preview) {
        // Check if from and to are aligned
        Bridge bridge = from.getBridge(from, to);
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
                if (isIntersecting(from, to, fromActive, toActive)) {
                    return false;
                }
            }
                
        }
        Bridge bridgeBetween = from.getBridge(from, to);
        if (preview) {
            if (selectedBridge != bridgeBetween) {
                selectedBridge = bridgeBetween;
                refresh();
            }
            return true;
        }
        bridgeBetween.toggleState();
        refresh();
        return true;
    }

    public boolean tryConnect(Node from, Node to) {
        return tryConnect(from, to, false);
    }

    public void selectNode(Node node) {
        if (this.selectedNode != null && this.selectedNode != node) {
            tryConnect(this.selectedNode, node);
            this.selectedNode = null;
        } else {
            this.selectedNode = node;
        }
    }

    public void tryConnectClosest(double x, double y, boolean preview) {
        List<Node> sortedNodes = nodes.stream().sorted((n1, n2) -> {
            double d1 = Math.hypot(n1.getCircle().getLayoutX() - x, n1.getCircle().getLayoutY() - y);
            double d2 = Math.hypot(n2.getCircle().getLayoutX() - x, n2.getCircle().getLayoutY() - y);
            return Double.compare(d1, d2);
        }).toList();
        for (int i = 0; i < Math.min(2, sortedNodes.size()); i++) {
            Node from = sortedNodes.get(i);
            double dx = from.getCircle().getLayoutX() - x;
            double dy = from.getCircle().getLayoutY() - y;
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
            if (tryConnect(from, closestNode, preview)) {
                break;
            }
        }
    }

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

    @Override
    public void start(Stage primaryStage) {

        // this.bridgePanes = new ArrayList<>();
        // this.nodes = new ArrayList<>();
        // this.activeBridges = new ArrayList<>();

        final int WIDTH = 300;
        final int HEIGHT = 300;

        // Level level = new Level("level1.txt");

        //level.printLevel();

        StackPane root = new StackPane();

        VBox layout = new VBox();
        VBox buttonBox = new VBox();
        
        layout.setSpacing(20);
        buttonBox.setSpacing(10);
        layout.setAlignment(javafx.geometry.Pos.TOP_CENTER);
        Button jouer = new Button("Jouer");
        Button leave = new Button("Quitter");
        Label titleLabel = new Label("Parmentier");
        titleLabel.getStyleClass().add("title-label");
        jouer.getStyleClass().add("button");
        leave.getStyleClass().add("button");

        buttonBox.getChildren().add(jouer);
        buttonBox.getChildren().add(leave);
        buttonBox.setAlignment(javafx.geometry.Pos.CENTER);
        layout.getChildren().add(titleLabel);
        layout.getChildren().add(buttonBox);
        root.getChildren().add(layout);

        leave.setOnAction(e -> {
            primaryStage.close();
        });
        //this.gridpane = new GridPane();
        //gridpane.setAlignment(javafx.geometry.Pos.CENTER);
        
        //startStopwatch();
        //StackPane.setAlignment(stopwatchLabel, javafx.geometry.Pos.TOP_CENTER);
        //
        //root.getChildren().add(stopwatchLabel);
        //root.getChildren().add(gridpane);
        //for (int i = 0; i < level.getWidth(); i++) {
        //    for (int j = 0; j < level.getHeight(); j++) {
        //        if (level.getNodeAt(i, j) != null) {
        //            Node node = level.getNodeAt(i, j);
        //            Circle circle = new Circle(16, Color.LIGHTGRAY);
        //            circle.setStroke(Color.BLACK);
        //            circle.setFill(Color.WHITE);
        //            Label label = new Label(Integer.toString(node.getValue()));
        //            label.setMaxSize(32, 32);
        //            label.setAlignment(javafx.geometry.Pos.CENTER);
        //            node.setCircle(circle);
        //            // use 2x2 cells for each button
        //            gridpane.add(circle, j, i);
        //            gridpane.add(label, j, i);
        //            //gridpane.add(btn, j, i);
        //            this.nodes.add(node);
        //        }
        //    }
        //}

        //refresh();


        //root.setOnMouseMoved(e -> {
        //    tryConnectClosest(e.getSceneX(), e.getSceneY(), true);
        //});

        //root.setOnMouseClicked(event -> {
        //    tryConnectClosest(event.getSceneX(), event.getSceneY(), false);
        //});
        
        primaryStage.setTitle("Parmentier");

        Scene scene = new Scene(root, WIDTH, HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/app.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

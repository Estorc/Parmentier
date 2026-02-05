package org.parmentier.game;

import java.util.ArrayList;
import java.util.List;

import org.parmentier.level.Bridge;
import org.parmentier.level.GridData;
import org.parmentier.level.Node;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class Level implements org.parmentier.game.Scene {
    private int xShift = 0;
    private int yShift = 0;
    private Bridge selectedBridge = null;
    private ArrayList<Bridge> activeBridges;
    private ArrayList<Node> nodes;
    private GridData level;
    private Timeline stopwatch;
    private int elapsedSeconds = 0;
    private Label stopwatchLabel;

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
                if (isIntersecting(from, to, fromActive, toActive)) {
                    return false;
                }
            }
                
        }
        Bridge bridgeBetween = from.getBridge(to);
        if (preview) {
            if (selectedBridge != bridgeBetween) {
                selectedBridge = bridgeBetween;
            }
            return true;
        }
        bridgeBetween.toggleState();
        if (bridgeBetween.getState() == 0) {
            activeBridges.remove(bridgeBetween);
        } else if (!activeBridges.contains(bridgeBetween)) {
            activeBridges.add(bridgeBetween);
        }
        return true;
    }

    public boolean tryConnect(Node from, Node to) {
        return tryConnect(from, to, false);
    }

    public void tryConnectClosest(double x, double y, boolean preview) {
        List<Node> sortedNodes = nodes.stream().sorted((n1, n2) -> {
            double d1 = Math.hypot(n1.getCanvasPosition()[0] - x, n1.getCanvasPosition()[1] - y);
            double d2 = Math.hypot(n2.getCanvasPosition()[0] - x, n2.getCanvasPosition()[1] - y);
            return Double.compare(d1, d2);
        }).toList();
        for (int i = 0; i < Math.min(2, sortedNodes.size()); i++) {
            Node from = sortedNodes.get(i);
            double dx = from.getCanvasPosition()[0] - x;
            double dy = from.getCanvasPosition()[1] - y;
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
    public void initialize(StackPane uiLayer) {
        this.nodes = new ArrayList<>();
        this.activeBridges = new ArrayList<>();

        

        level = new GridData("level1.txt");
       
        startStopwatch();
        StackPane.setAlignment(stopwatchLabel, javafx.geometry.Pos.TOP_CENTER);
        
        uiLayer.getChildren().add(stopwatchLabel);


        for (int i = 0; i < level.getWidth(); i++) {
            for (int j = 0; j < level.getHeight(); j++) {
                if (level.getNodeAt(i, j) != null) {
                    Node node = level.getNodeAt(i, j);
                    this.nodes.add(node);
                }
            }
        }


        uiLayer.setOnMouseMoved(e -> {
            tryConnectClosest(e.getX() - xShift, e.getY() - yShift, true);
        });


        uiLayer.setOnMouseClicked(e -> {
            tryConnectClosest(e.getX() - xShift, e.getY() - yShift, false);
        });
    }

    @Override
    public void update(double deltaTime, StackPane uiLayer) {
        //
    }

    @Override
    public void render(GraphicsContext gc, StackPane uiLayer) {
        gc.setFill(Color.LIGHTBLUE);
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        this.xShift = (int) gc.getCanvas().getWidth() / 2 - (level.getWidth() * 32) / 2;
        this.yShift = (int) gc.getCanvas().getHeight() / 2 - (level.getHeight() * 32) / 2;
        gc.setTransform(1, 0, 0, 1, xShift, yShift);
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

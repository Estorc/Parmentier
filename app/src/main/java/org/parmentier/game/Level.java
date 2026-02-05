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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;



public class Level implements org.parmentier.game.Scene {
    private int xShift, yShift;
    private boolean initialized = false;
    private Bridge selectedBridge = null;
    private ArrayList<Pane> bridgePanes;
    private ArrayList<Bridge> activeBridges;
    private ArrayList<Node> nodes;
    private Node selectedNode = null;
    private GridPane gridpane;
    private Timeline stopwatch;
    private int elapsedSeconds = 0;
    private int score = 0;
    private int timeLowMinutes = 5;
    private int timeHighMinutes = 30;
    private float timeCoeffMin = 0.67;
    private float timeCoeffMax = 1;
    private Label stopwatchLabel;

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
                switch (bridge.getDirection()) {
                    case Bridge.Direction.LEFT, Bridge.Direction.RIGHT -> drawBridge(bridge, x+1, y, 0, 16, bridge.getLength()+1, 1);
                    case Bridge.Direction.TOP, Bridge.Direction.BOTTOM -> drawBridge(bridge, x, y+1, 16, 0, 1, bridge.getLength()+1);
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



    /* This method calcule score with the numbers of bridge at the end of the level and the time used to complete the level */
    private void calculateScore(){
       int timeLow = this.timeLowMinutes  * 60;
       int timeHigh = this.timeHighMinutes * 60;

       double timeBornedShifted = (((elapsedSeconds + timeLow + (Math.abs(elapsedSeconds - timeLow)))/2) + timeHigh - (Math.abs(((elapsedSeconds + timeLow + (Math.abs(elapsedSeconds - timeLow)))/2) - timeHigh))) - timeLow;
       //OU 
       timeBornedShifted = Math.min( Math.max(elapsedSeconds, timeLow) , timeHigh) - timeLow;
       //OU clamp(val, min, max)
       timeBornedShifted = Math.clamp(elapsedSeconds, timeLow, timeHigh) - timeLow;
       
       // We need a value between 0 et 1 to get our final coeff
       double timeNormalized = timeBornedShifted / (timeHigh - timeLow);

       // The coefficient of time is normalized between our minimal coefficient and our maximal coefficient.
       double timeCoeff = (timeNormalized - timeCoeffMin) + timeCoeffMax;

       // J'attends que le nombre de ponts attendus soit disponible.
       //int score = (int)((this.activeBridges.stream().reduce(0, (a,b) -> , combiner) * 1000) * timeCoeff);

    }
    public void start(StackPane uiLayer) {

        this.xShift = 0;
        this.yShift = 0;

        uiLayer.getChildren().clear();

        this.bridgePanes = new ArrayList<>();
        this.nodes = new ArrayList<>();
        this.activeBridges = new ArrayList<>();

        

        GridData level = new GridData("level1.txt");

        level.printLevel();

        this.gridpane = new GridPane();
        gridpane.setAlignment(javafx.geometry.Pos.CENTER);
       
        startStopwatch();
        StackPane.setAlignment(stopwatchLabel, javafx.geometry.Pos.TOP_CENTER);
        
        uiLayer.getChildren().add(stopwatchLabel);
        uiLayer.getChildren().add(gridpane);


        for (int i = 0; i < level.getWidth(); i++) {
            for (int j = 0; j < level.getHeight(); j++) {
                if (level.getNodeAt(i, j) != null) {
                    Node node = level.getNodeAt(i, j);
                    Circle circle = new Circle(16, Color.LIGHTGRAY);
                    circle.setStroke(Color.BLACK);
                    circle.setFill(Color.WHITE);
                    Label label = new Label(Integer.toString(node.getValue()));
                    label.setMaxSize(32, 32);
                    label.setAlignment(javafx.geometry.Pos.CENTER);
                    node.setCircle(circle);
                    // use 2x2 cells for each button
                    gridpane.add(circle, j, i);
                    gridpane.add(label, j, i);
                    //gridpane.add(btn, j, i);
                    this.nodes.add(node);
                }
            }
        }

        refresh();


        uiLayer.setOnMouseMoved(e -> {
            tryConnectClosest(e.getSceneX() - xShift + uiLayer.getLayoutX(), e.getSceneY() - yShift + uiLayer.getLayoutY(), true);
        });


        uiLayer.setOnMouseClicked(event -> {
            tryConnectClosest(event.getSceneX() - xShift + uiLayer.getLayoutX(), event.getSceneY() - yShift + uiLayer.getLayoutY(), false);
        });
    }

    @Override
    public void update(double deltaTime, StackPane uiLayer) {
        if (!initialized) {
            start(uiLayer);
            uiLayer.getChildren().clear();
            initialized = true;
        }
    }

    @Override
    public void render(GraphicsContext gc, StackPane uiLayer) {
        gc.setFill(Color.LIGHTBLUE);
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        this.xShift = (int) gc.getCanvas().getWidth() / 2 - (gridpane.getColumnCount() * 32) / 2;
        this.yShift = (int) gc.getCanvas().getHeight() / 2 - (gridpane.getRowCount() * 32) / 2;
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

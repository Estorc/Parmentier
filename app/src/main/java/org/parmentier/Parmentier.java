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
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import java.util.ArrayList;

public class Parmentier extends Application {

    private ArrayList<Pane> bridgePanes;
    private ArrayList<Node> nodes;
    private int bridgeSize = 0;
    private Node selectedNode = null;
    private GridPane gridpane;

    public static void main(String[] args) {
        launch(args);
    }

    public void refresh() {
        System.out.println("Refreshing bridges...");
        for (Pane pane : this.bridgePanes) {
            gridpane.getChildren().remove(pane);
        }
        this.bridgePanes.clear();
        for (Node node : this.nodes) {
            int x = node.getPosition()[0];
            int y = node.getPosition()[1];
            for (Bridge bridge : node.getBridges()) {
                Pane linePane;
                switch (bridge.getDirection()) {
                    case Bridge.Direction.HORIZONTAL:
                        linePane = new Pane();
                        linePane.setMinSize(32 * bridge.getLength(), 32);
                        linePane.setMaxSize(32 * bridge.getLength(), 32);
                        if (bridge.getState() != 0) {
                            Line line = new Line(0, 16, 32 * bridge.getLength(), 16);
                            line.setStrokeWidth(bridge.getState());
                            linePane.getChildren().add(line);
                        }
                        gridpane.add(linePane, x + 1, y, bridge.getLength(), 1);
                        this.bridgePanes.add(linePane);
                        break;
                    case Bridge.Direction.VERTICAL:
                        linePane = new Pane();
                        linePane.setMinSize(32, 32 * bridge.getLength());
                        linePane.setMaxSize(32, 32 * bridge.getLength());
                        if (bridge.getState() != 0) {
                            Line line = new Line(16, 0, 16, 32 * bridge.getLength());
                            line.setStrokeWidth(bridge.getState());
                            linePane.getChildren().add(line);
                        }
                        gridpane.add(linePane, x, y + 1, 1, bridge.getLength());
                        this.bridgePanes.add(linePane);
                        break;
                }
            }
        }
    }

    public void tryConnect(Node from, Node to) {
        // Check if from and to are aligned
        int[] fromPos = from.getPosition();
        int[] toPos = to.getPosition();
        if (fromPos[0] == toPos[0] || fromPos[1] == toPos[1]) {
            from.getBridge(from, to).toggleState();
            refresh();
        }
    }

    public void selectNode(Node node) {
        if (this.selectedNode != null && this.selectedNode != node) {
            tryConnect(this.selectedNode, node);
            this.selectedNode = null;
        } else {
            this.selectedNode = node;
        }
    }

    @Override
    public void start(Stage primaryStage) {

        this.bridgePanes = new ArrayList<>();
        this.nodes = new ArrayList<>();

        final int WIDTH = 300;
        final int HEIGHT = 250;

        Level level = new Level("level1.txt");

        level.printLevel();

        StackPane root = new StackPane();
        this.gridpane = new GridPane();
        //Center the gridpane
        gridpane.setAlignment(javafx.geometry.Pos.CENTER);



        root.getChildren().add(gridpane);

        for (int i = 0; i < level.getWidth(); i++) {
            for (int j = 0; j < level.getHeight(); j++) {
                if (level.getNodeAt(i, j) != null) {
                    Node node = level.getNodeAt(i, j);
                    Button btn = new Button();
                    btn.setText(level.getValueAt(i, j) + "");
                    btn.setMinSize(32, 32);
                    btn.setId(i + ":" + j);
                    btn.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            System.out.println(btn.getId() + " clicked!");
                            selectNode(node);
                        }
                    });
                    // use 2x2 cells for each button
                    gridpane.add(btn, j, i);
                    this.nodes.add(node);
                }
            }
        }

        refresh();

        primaryStage.setTitle("Hello World!");


        primaryStage.setScene(new Scene(root, WIDTH, HEIGHT));
        primaryStage.show();
    }
}

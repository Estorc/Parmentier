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

public class Parmentier extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        final int WIDTH = 300;
        final int HEIGHT = 250;

        Level level = new Level("level1.txt");

        level.printLevel();

        StackPane root = new StackPane();
        GridPane gridpane = new GridPane();
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
                    btn.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            System.out.println("click :p");
                        }
                    });
                    // use 2x2 cells for each button
                    gridpane.add(btn, j, i);
                    if (node.getBridge(Bridge.Direction.HORIZONTAL) != null) {
                        Pane linePane = new Pane();
                        linePane.setMinSize(32 * node.getBridge(Bridge.Direction.HORIZONTAL).getLength(), 32);
                        Line line = new Line(0, 16, 32 * node.getBridge(Bridge.Direction.HORIZONTAL).getLength(), 16);
                        line.setStrokeWidth(2);
                        linePane.getChildren().add(line);
                        gridpane.add(linePane, j + 1, i, node.getBridge(Bridge.Direction.HORIZONTAL).getLength(), 1);
                    }
                    if (node.getBridge(Bridge.Direction.VERTICAL) != null) {
                        Pane linePane = new Pane();
                        linePane.setMinSize(32, 32 * node.getBridge(Bridge.Direction.VERTICAL).getLength());
                        Line line = new Line(16, 0, 16, 32 * node.getBridge(Bridge.Direction.VERTICAL).getLength());
                        line.setStrokeWidth(2);
                        linePane.getChildren().add(line);
                        gridpane.add(linePane, j, i + 1, 1, node.getBridge(Bridge.Direction.VERTICAL).getLength());
                    }
                }
            }
        }

        primaryStage.setTitle("Hello World!");


        primaryStage.setScene(new Scene(root, WIDTH, HEIGHT));
        primaryStage.show();
    }
}

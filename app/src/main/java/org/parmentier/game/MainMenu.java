package org.parmentier.game;

import org.parmentier.MenuChoiceLevel;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.geometry.Pos;
import javafx.geometry.Insets;

public class MainMenu implements org.parmentier.game.Scene {

    @Override
    public void update(double deltaTime, StackPane uiLayer) {
        //
    }

    @Override
    public void render(GraphicsContext gc, StackPane uiLayer) {
        //
    }

    private StackPane createCircledNumber(String number) {
        Circle circle = new Circle(22);
        circle.getStyleClass().add("logo-circle");
        Text text = new Text(number);
        text.getStyleClass().add("logo-number");
        StackPane pane = new StackPane(circle, text);
        return pane;
    }

    @Override
    public void initialize(StackPane uiLayer) {
        uiLayer.getChildren().clear();
        VBox layout = new VBox();
        layout.getStyleClass().add("main-menu-bg");

        // Logo hashi game grid 2x2
        GridPane logoGrid = new GridPane();
        logoGrid.setHgap(10);
        logoGrid.setVgap(6);
        logoGrid.setAlignment(Pos.CENTER);
        logoGrid.add(createCircledNumber("4"), 0, 0);
        logoGrid.add(createCircledNumber("3"), 1, 0);
        logoGrid.add(createCircledNumber("2"), 0, 1);
        logoGrid.add(createCircledNumber("5"), 1, 1);

        // Title
        Label titleLabel = new Label("Parmentier");
        titleLabel.getStyleClass().add("title-label");

        // Subtitle
        Label subtitleLabel = new Label("Hashiwokakero");
        subtitleLabel.getStyleClass().add("subtitle-label");

        // Buttons
        VBox buttonBox = new VBox(12);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));
        Button jouer = new Button("Jouer");
        Button tutorial = new Button("Didacticiel");
        Button settings = new Button("Paramètres");
        Button leave = new Button("Quitter");
        jouer.getStyleClass().add("menu-button");
        tutorial.getStyleClass().add("menu-button");
        settings.getStyleClass().add("menu-button");
        leave.getStyleClass().add("menu-button");
        jouer.setMaxWidth(320);
        jouer.setMinWidth(320);
        tutorial.setMaxWidth(320);
        tutorial.setMinWidth(320);
        settings.setMaxWidth(320);
        settings.setMinWidth(320);
        leave.setMaxWidth(320);
        leave.setMinWidth(320);
        buttonBox.getChildren().addAll(jouer, tutorial, settings, leave);

        layout.setSpacing(8);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40, 0, 40, 0));
        layout.getChildren().addAll(logoGrid, titleLabel, subtitleLabel, buttonBox);

        uiLayer.getChildren().add(layout);
        uiLayer.getStylesheets().add(getClass().getResource("/app.css").toExternalForm());

        jouer.setOnMouseClicked( e -> Game.getInstance().getSceneManager().pushScene(new MenuChoiceLevel()));
        settings.setOnMouseClicked( e -> Game.getInstance().getSceneManager().pushScene(new Settings()));
        leave.setOnMouseClicked( e -> System.exit(0));
        tutorial.setOnMouseClicked( e -> Game.getInstance().getSceneManager().pushScene(new Level("didacticiel")));
    }

    /**
     * Default destructor for the MainMenu class. Since there are no specific resources to clean up, this method is left empty.
     */
    @Override
    public void destroy() {
        //
    }
}

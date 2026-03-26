package org.parmentier.game;

import org.parmentier.MenuChoiceLevel;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.TextAlignment;

public class Didacticiel implements org.parmentier.game.Scene {
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
        String textintro = "Le Hashiwokakero, couramment appelé Hashi, est un casse-tête logique dont l'objectif est de relier une série d'îles numérotées par des ponts afin de former un réseau unique et continu. Chaque île contient un chiffre qui dicte précisément le nombre de ponts devant s'y raccorder. Le tracé de ces ponts suit des contraintes strictes : ils ne peuvent être que horizontaux ou verticaux, ne doivent jamais se croiser ni survoler une autre île, et la connexion entre deux îles est limitée à un maximum de deux ponts. La résolution repose sur l'élimination progressive des options : on commence généralement par les îles aux chiffres élevés situées dans les coins ou les bords, car leurs possibilités de connexion sont physiquement limitées. Le défi ultime réside dans la règle de connectivité globale, qui interdit la formation de sous-groupes isolés ; chaque pont posé doit contribuer à l'édification d'un chemin unique reliant l'intégralité de l'archipel, transformant ainsi chaque décision en un exercice d'anticipation topologique.";
        VBox layout = new VBox();
        layout.getStyleClass().add("main-menu-bg");
        // Title
        Label titleLabel = new Label("Parmentier");
        titleLabel.getStyleClass().add("title-label");

        // Buttons
        VBox buttonBox = new VBox(12);
        Label intro = new Label(textintro);
        intro.getStyleClass().add("textIntro");
        intro.setWrapText(true);
        intro.setMaxWidth(600); 
        intro.setTextAlignment(TextAlignment.CENTER);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));
        Button tutorial = new Button("Didacticiel");
        tutorial.getStyleClass().add("menu-button");
        tutorial.setMaxWidth(320);
        tutorial.setMinWidth(320);
        buttonBox.getChildren().addAll(tutorial);

        layout.setSpacing(8);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40, 0, 40, 0));
        layout.getChildren().addAll(titleLabel, intro, buttonBox);

        uiLayer.getChildren().add(layout);
        uiLayer.getStylesheets().add(getClass().getResource("/app.css").toExternalForm());
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

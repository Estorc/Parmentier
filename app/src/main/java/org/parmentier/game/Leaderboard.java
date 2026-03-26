package org.parmentier.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

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


public class Leaderboard {

    // Liste qui stocke toutes les performances
    private List<Performance> performances;

    // Constructeur : initialise la liste vide
    public Leaderboard() {
        this.performances = new ArrayList<>();
    }

    /**
     * Ajoute une performance au classement.
     */
    public void addPerformance(Performance p) {
        this.performances.add(p);
    }

    /**
     * Trie la liste en utilisant la logique compareTo 
     * définie dans la classe Performance.
     */
    public void trierClassement() {
        Collections.sort(this.performances);
    }

    /**
     * Affiche le tableau des scores de manière lisible dans la console.
     */
    public void afficherLeaderboard() {
        // On trie avant d'afficher pour être sûr du classement
        trierClassement();

        System.out.println("\n========== LEADERBOARD ==========");
        System.out.println("Rang | Joueur     | Score | Note");
        System.out.println("---------------------------------");

        for (int i = 0; i < performances.size(); i++) {
            Performance p = performances.get(i);
            // i + 1 pour que le premier soit au rang 1
            System.out.printf("%-4d | %-10s | %-5d | %-2c\n", 
                (i + 1), 
                p.getNameTag(), 
                p.getScore(), 
                p.getNote());
        }
        System.out.println("=================================\n");
    }

    // Getter pour accéder à la liste brute si besoin
    public List<Performance> getPerformances() {
        return performances;
    }
}

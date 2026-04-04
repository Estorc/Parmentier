package org.parmentier.game;

import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.geometry.Pos;public class Performance implements Comparable<Performance> {

    private String nameTag;
    private int timeElapsedSeconds;
    private double timeElapsedMinutes;
    private int helpUsed;
    private int verificationsUsed;
    private int score;
    private int nbClics;
    private char note;

    /**
     * Logique de comparaison pour classer les performances.
     * Retourne :
     * - Un entier négatif si "this" est inférieur à "perf"
     * - Zéro si ils sont égaux
     * - Un entier positif si "this" est supérieur à "perf"
     */
    @Override
    public int compareTo(Performance perf) {
        // Exemple : Comparaison basée sur le score (ordre décroissant : le plus haut score en premier)
        // Pour un ordre croissant, inversez simplement : Integer.compare(this.score, perf.score)
        int comparison = Integer.compare(this.score , perf.getScore());

        // Si les scores sont identiques, on peut départager avec le temps (le plus rapide gagne)
        if (comparison == 0) {
            comparison = Integer.compare(this.timeElapsedSeconds, perf.getTimeElapsedSeconds());
        }
        if (comparison == 0) {
            comparison = Integer.compare(this.helpUsed, perf.getHelpUsed());
        }
        if (comparison == 0) {
            comparison = Integer.compare(this.nbClics, perf.getNbClics());
        }
        return comparison;
    }

    // --- GETTERS & SETTERS ---


    // This method fill a Performance with abstract value
    public Performance setPlaceHolder(){
        Performance temp = new Performance();
        this.nameTag = "pakoRabin";
        this.timeElapsedSeconds = 666;
        this.timeElapsedMinutes = 3.33;
        this.helpUsed = 4;
        this.verificationsUsed = 8;
        this.score = 99089;
        this.nbClics = 390;
        this.note = 'S';
        return temp;
    }

    public String getNameTag() {
        return this.nameTag;
    }

    public void setNameTag(String NameTag) {
        this.nameTag = NameTag;
    }

    public int getTimeElapsedSeconds() {
        return this.timeElapsedSeconds;
    }

    public void setTimeElapsedSeconds(int timeElapsedSeconds) {
        this.timeElapsedSeconds = timeElapsedSeconds;
    }

    public double getTimeElapsedMinutes() {
        return this.timeElapsedMinutes;
    }

    public void setTimeElapsedMinutes(double timeElapsedMinutes) {
        this.timeElapsedMinutes = timeElapsedMinutes;
    }

    public int getHelpUsed() {
        return this.helpUsed;
    }

    public void setHelpUsed(int helpUsed) {
        this.helpUsed = helpUsed;
    }

    public int getVerificationsUsed() {
        return this.verificationsUsed;
    }

    public void setVerificationsUsed(int verificationsUsed) {
        this.verificationsUsed = verificationsUsed;
    }

    public int getScore() {
        return this.score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getNbClics() {
        return this.nbClics;
    }

    public void setNbClics(int nbClics) {
        this.nbClics = nbClics;
    }

    public char getNote() {
        return this.note;
    }

    public void setNote(char note) {
        this.note = note;
    }

    public HBox affichagePerformance(){
        HBox temp = new HBox(20);
        Label nameTagLabel = new Label(this.nameTag);
        
        // Calcul simple et propre du temps
        int minutes = this.timeElapsedSeconds / 60;
        int seconds = this.timeElapsedSeconds % 60;
        Label timeLabel = new Label(minutes + "m " + seconds + "s");

        Label helpUsedLabel = new Label("2");
        Label checkUsedLabel = new Label("3");
        Label scoreLabel = new Label("" + this.score);
        Label clicsLabel = new Label("4");
        Label noteLabel = new Label("" + this.note);

        temp.setAlignment(Pos.CENTER);
        temp.getChildren().addAll(nameTagLabel, timeLabel, helpUsedLabel, checkUsedLabel, scoreLabel, clicsLabel, noteLabel);
        return temp;
    }

    @Override
    public String toString() {
        return "=== Performance de : " + this.nameTag+ " ===\n" +
            "  Note globale      : " + this.note + "\n" +
            "  Score             : " + this.score + " pts\n" +
            "  Temps écoulé      : " + this.timeElapsedSeconds + "s (" + this.timeElapsedMinutes + " min)\n" +
            "  Aides / Vérifs    : " + this.helpUsed + " / " + this.verificationsUsed + "\n" +
            "  Nombre de clics   : " + this.nbClics + "\n" +
            "================================";
    }
    


}
package org.parmentier.game;

import javafx.scene.media.AudioClip;
import java.net.URL;

public class Audio {
    private static AudioClip addBridgeSound;
    private static AudioClip removeBridgeSound;
    private static AudioClip clickSound;
    private static double volume = 0.5;

    static {
        URL resAdd = Audio.class.getResource("/sounds/bridge_add.wav");
        URL resRemove = Audio.class.getResource("/sounds/bridge_remove.wav");
        URL resClick = Audio.class.getResource("/sounds/clic.wav");

        addBridgeSound = new AudioClip(resAdd.toExternalForm());
        removeBridgeSound = new AudioClip(resRemove.toExternalForm());
        clickSound = new AudioClip(resClick.toExternalForm());
        
    }

    public static void playAddBridgeSound() {
        if (addBridgeSound != null) {
            addBridgeSound.setVolume(volume);
            addBridgeSound.play();
        }
    }

    public static void playRemoveBridgeSound() {
        if (removeBridgeSound != null) {
            removeBridgeSound.setVolume(volume);
            removeBridgeSound.play();
        }
    }

    public static void playClickSound() {
        if (clickSound != null) {
            clickSound.setVolume(volume);
            clickSound.play();
        }
    }

    public static void setVolume(double newVolume) {
        volume = newVolume;
        addBridgeSound.setVolume(newVolume);
        removeBridgeSound.setVolume(newVolume);
        clickSound.setVolume(newVolume);
    }

    public static double getVolume() {
        return volume;
    }
}
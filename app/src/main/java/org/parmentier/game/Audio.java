package org.parmentier.game;

import javafx.scene.media.AudioClip;
import java.net.URL;

public class Audio {
    private static AudioClip addBridgeSound;
    private static AudioClip removeBridgeSound;
    private static AudioClip clickSound;
    private static double volume = 0.5;

    static {
        URL resource = Audio.class.getResource("/sounds/bridge_add.wav");
        if (resource != null) {
            addBridgeSound = new AudioClip(resource.toExternalForm());
        }
        URL resRemove = Audio.class.getResource("/sounds/bridge_remove.wav");
        if (resRemove != null) {
            removeBridgeSound = new AudioClip(resRemove.toExternalForm());
        }
        URL resClick = Audio.class.getResource("/sounds/clic.wav");
        if (resClick != null) {
            clickSound = new AudioClip(resClick.toExternalForm());
        }
    }

    public static void playAddBridgeSound() {
        if (addBridgeSound != null) {
            addBridgeSound.play(volume);
        }
    }

    public static void playRemoveBridgeSound() {
        if (removeBridgeSound != null) {
            removeBridgeSound.play(volume);
        }
    }

    public static void playClickSound() {
        if (clickSound != null) {
            clickSound.play(volume);
        }
    }

    public static void setVolume(double newVolume) {
        volume = newVolume;
    }
}
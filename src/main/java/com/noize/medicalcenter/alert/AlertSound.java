package com.noize.medicalcenter.alert;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

import static com.noize.medicalcenter.alert.Sound.CONFIRM;
import static com.noize.medicalcenter.alert.Sound.INVALID;

public class AlertSound {
    private Media media;
    private MediaPlayer mediaPlayer;

    public void checkSounds(Sound sound) {
        try {
            URL resource;
            switch (sound) {
                case INVALID:
                    resource = getClass().getResource("/asset/sounds/wrongAnswer.mp3");
                    System.out.println("Resource URL: " + resource);

                    if (resource == null) {
                        throw new RuntimeException("Sound file not found: /sounds/wrongAnswer.mp3");
                    }

                    media = new Media(resource.toExternalForm());
                    stopPreviousSound();
                    mediaPlayer = new MediaPlayer(media);
                    mediaPlayer.play();
                    break;

                case CONFIRM:
                    resource = getClass().getResource("/asset/sounds/successAnswer.mp3");
                    System.out.println("Resource URL: " + resource);

                    if (resource == null) {
                        throw new RuntimeException("Sound file not found: /sounds/wrongAnswer.mp3");
                    }

                    media = new Media(resource.toExternalForm());
                    stopPreviousSound();
                    mediaPlayer = new MediaPlayer(media);
                    mediaPlayer.play();
                    break;

                default:
                    System.out.println("Invalid sound selection.");
            }
        } catch (Exception e) {
            System.err.println("Error loading sound: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Stops any previous sound from playing, if applicable
    private void stopPreviousSound() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }
}

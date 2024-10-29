package com.noize.medicalcenter.notification;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import lombok.AllArgsConstructor;
import org.controlsfx.control.Notifications;

@AllArgsConstructor

public class AlertNotification extends Thread {
    private String title;
    private String text;
    private String image;
    private String actione;

    public void run() {
        Image img = new Image(AlertNotification.class.getResource("/asset/icon/" + image).toExternalForm());
        ImageView imageView = new ImageView(img);
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);

        Notifications notification = Notifications.create()
                .title(title)
                .text(text)
                .graphic(imageView)
                .hideAfter(Duration.seconds(5))
                .position(Pos.BOTTOM_RIGHT)
                .onAction(event -> System.out.println(actione));

        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(10.0);
        dropShadow.setOffsetX(5.0);
        dropShadow.setOffsetY(5.0);
        dropShadow.setColor(Color.color(0.0, 0.0, 0.0, 0.6));
        imageView.setEffect(dropShadow);

        notification.darkStyle();
        Platform.runLater(() -> notification.show());
    }
}

package com.unidawgs.le5.clubdawgs;
import javafx.animation.TranslateTransition;
import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.util.Duration;

public class Minigame1SubScene extends SubScene {


    private boolean isHidden = true;

    public Minigame1SubScene() {
        super(new AnchorPane(), 600, 400);
        prefWidth(600);
        prefHeight(400);
        BackgroundImage image = new BackgroundImage(new Image(getClass().getResource("mg-box.png").toExternalForm(), 600, 400, false, true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, null);
        AnchorPane root2 = (AnchorPane) this.getRoot();
        root2.setBackground(new Background(image));

        setLayoutX(1024);
        setLayoutY(180);
    }

    public void moveSubScene() {
        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.seconds(0.5));
        transition.setNode(this);
        if(isHidden) {
            transition.setToX(-676);
            isHidden = false;
        } else {
            transition.setToX(0);
            isHidden = true;
        }
        transition.play();
    }
    public AnchorPane getPane() {
        return (AnchorPane) this.getRoot();
    }

}
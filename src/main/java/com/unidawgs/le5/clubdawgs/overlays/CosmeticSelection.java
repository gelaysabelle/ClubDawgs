package com.unidawgs.le5.clubdawgs.overlays;

import java.util.ArrayList;
import java.util.Arrays;

import com.unidawgs.le5.clubdawgs.Firebase;
import com.unidawgs.le5.clubdawgs.Game;
import com.unidawgs.le5.clubdawgs.Main;
import com.unidawgs.le5.clubdawgs.Settings;
import com.unidawgs.le5.clubdawgs.events.CosmeticEvent;
import com.unidawgs.le5.clubdawgs.objects.*;
import com.unidawgs.le5.clubdawgs.rooms.Room;

import javafx.event.Event;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;

public class CosmeticSelection implements Overlay {
    private ArrayList<Integer> all = new ArrayList<>();
    private ArrayList<CosmeticBtn> buttons = new ArrayList<>();
    private ImgBtn closeBtn = new ImgBtn(800, 20, 50, 50, new Image(Main.class.getResource("exitButton.png").toString()));
    private double yOffset;
    private double btnHeight;
    private final ArrayList<Image> leftSprite = new ArrayList<>(Arrays.asList(
            new Image(Main.class.getResource("dog-sprite-left-idle.png").toString()),
            new Image(Main.class.getResource("dog-sprite-left-walk1.png").toString()),
            new Image(Main.class.getResource("dog-sprite-left-walk2.png").toString())
    ));
    private int animationCounter = 0;
    private int curCosmetic = 0;
    private Image cosmeticImg;
    private CategoryBtn ownedBtn = new CategoryBtn(430, 85, 110, 50, "Owned", Color.BLACK);
    private CategoryBtn fiveBtn = new CategoryBtn(430, 155, 110, 50, "Five *", Color.BLACK);
    private CategoryBtn fourBtn = new CategoryBtn(560, 85, 110, 50, "Four *", Color.BLACK);
    private CategoryBtn threeBtn = new CategoryBtn(560, 155, 110, 50, "Three *", Color.BLACK);
    private Image bgImage = new Image(Main.class.getResource("cosmeticSelectionContainer.png").toString());
    private Image bgImageUpper = new Image(Main.class.getResource("cosmeticSelectionContainerUpper.png").toString());
    private Image previewBgImage = new Image(Main.class.getResource("characterView.png").toString());
    private ArrayList<Integer> cosmetics;

    public CosmeticSelection() {
        User user = Main.getUser();
        this.curCosmetic = Firebase.getCosmetic(user.getLocalId(), user.getIdToken());
        if (this.curCosmetic != 0) {
            this.cosmeticImg = new Image(Main.class.getResource("cosmetics/sprite accessories-"+ this.curCosmetic +".png").toString());
        }
        this.cosmetics = Firebase.getCosmetics(user.getLocalId(), user.getIdToken());
        if (this.cosmetics == null) {
            System.out.println("Problem getting cosmetics!");
            return;
        }
        for (int i = 1; i < 35; i++) {
            this.all.add(i);
        }

        filterCosmetics(this.all, this.cosmetics);
    }

    public void filterCosmetics(ArrayList<Integer> filter, ArrayList<Integer> cosmetics) {
        int row = 0;
        int col = 0;
        this.buttons.clear();
        for (int i = 1; i < 35; i++) {
            if (filter.contains(i)) {
                Image cosmeticImage = new Image(Main.class.getResource("cosmetics/sprite accessories-"+ i +".png").toString());
                this.buttons.add(new CosmeticBtn(row, col, cosmeticImage, i, cosmetics.contains(i)));
                col++;
                if (col == 5) {
                    row++;
                    col = 0;
                }
            }
        }
        this.btnHeight = (row + 1) * 110;
    }

    public void mouseMoveHandler(MouseEvent mouse) {
        closeBtn.enters(mouse);
        closeBtn.exits(mouse);

        ownedBtn.enters(mouse);
        ownedBtn.exits(mouse);
        fiveBtn.enters(mouse);
        fiveBtn.exits(mouse);
        fourBtn.enters(mouse);
        fourBtn.exits(mouse);
        threeBtn.enters(mouse);
        threeBtn.exits(mouse);

        for (CosmeticBtn btn : buttons) {
            btn.enters(mouse);
            btn.exits(mouse);
        }
    }

    public void mouseClickHandler(MouseEvent mouse) {
        Room room = (Room) mouse.getSource();
        for (CosmeticBtn btn : buttons) {
            if (btn.isClicked(mouse)) {
                this.curCosmetic = btn.getCosmetic();
                this.cosmeticImg = btn.getCosmeticImg();
            }
        }
        if (closeBtn.isClicked(mouse)) {
            Firebase.updateCosmetic(Main.getUser().getLocalId(), Main.getUser().getIdToken(), this.curCosmetic);
            room.fireEvent(new CosmeticEvent(Game.CHANGE_COSMETIC, this.curCosmetic));
            room.fireEvent(new Event(Game.HIDE_OVERLAY));
        } else if (this.ownedBtn.isClicked(mouse)) {
            this.filterCosmetics(this.cosmetics, this.cosmetics);
            this.yOffset = 0;
        }else if (this.fiveBtn.isClicked(mouse)) {
            this.filterCosmetics(Settings.fiveStars, this.cosmetics);
            this.yOffset = 0;
        }else if (this.fourBtn.isClicked(mouse)) {
            this.filterCosmetics(Settings.fourStars, this.cosmetics);
            this.yOffset = 0;
        }else if (this.threeBtn.isClicked(mouse)) {
            this.filterCosmetics(Settings.threeStars, this.cosmetics);
            this.yOffset = 0;
        }
    }

    public void scrollHandler(ScrollEvent scroll) {
        if (this.yOffset <= 0 && this.yOffset >= 330 - this.btnHeight) {
            this.yOffset += scroll.getDeltaY();
        }
    }

    public void draw(GraphicsContext gc) {
        if (this.yOffset > 0) {
            this.yOffset -= 2;
        }else if (this.yOffset < 330 - this.btnHeight && this.btnHeight > 330) {
            this.yOffset += 2;
        }

        gc.setFill(Color.rgb(0, 0, 0, 0.5));
        gc.fillRect(0, 0, Settings.gameWidth, Settings.gameHeight);

        gc.drawImage(this.bgImage, 125, 0);
        for (CosmeticBtn btn : buttons) {
            btn.draw(gc, this.yOffset);
        }
        gc.drawImage(this.bgImageUpper, 125, 0);

        this.animationCounter++;
        if (this.animationCounter == (7 * 3)) {
            this.animationCounter = 0;
        }

        gc.drawImage(this.previewBgImage, 210, 50, 190, 190);
        gc.drawImage(this.leftSprite.get(this.animationCounter/7), 255, 95, 120, 120);
        if (this.curCosmetic != 0) {
            gc.drawImage(this.cosmeticImg, 220, 68 - (2.5*(this.animationCounter/14.0)), 185, 185);
        }

        ownedBtn.draw(gc);
        fiveBtn.draw(gc);
        fourBtn.draw(gc);
        threeBtn.draw(gc);
        closeBtn.draw(gc);
    }
}

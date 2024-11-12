package com.unidawgs.le5.clubdawgs.rooms;

import com.unidawgs.le5.clubdawgs.*;
import com.unidawgs.le5.clubdawgs.events.RoomEvent;
import com.unidawgs.le5.clubdawgs.objects.DrawableEntity;
import com.unidawgs.le5.clubdawgs.objects.Player;
import com.unidawgs.le5.clubdawgs.objects.WalkSpace;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Backyard extends Room {
    private WalkSpace houseEntry = new WalkSpace(150, 200, 65, 30);
    private ArrayList<WalkSpace> barriers = new ArrayList<>(List.of(
            new WalkSpace(560, 0, 40, 25),
            new WalkSpace(590, 25, 10, 60),
            new WalkSpace(620, 85, 10, 60),
            new WalkSpace(630, 127, 80, 20),
            new WalkSpace(710, 147, 10, 60),
            new WalkSpace(720, 187, 150, 20),
            new WalkSpace(825, 360, 45, 15),
            new WalkSpace(790, 395, 10, 50),
            new WalkSpace(740, 450, 55, 30),
            new WalkSpace(700, 485, 30, 30),
            new WalkSpace(615, 512, 85, 30),
            new WalkSpace(580, 547, 30, 73)
    ));
    //private WalkSpace mainLobbyEntry = new WalkSpace();

    public Backyard(double width, double height, String roomId) {
        super(width, height, roomId, new Image(Main.class.getResource("backyard layout.png").toString()));
    }

    @Override
    public void drawRoom(Player player) {
        ArrayList<DrawableEntity> entities = new ArrayList<>(this.dropBoxes);
        entities.addAll(this.players);
        entities.add(player);
        this.getGraphicsContext2D().drawImage(this.bgImg, 0, 0, this.getWidth(), this.getHeight());

        entities.sort(Comparator.comparingDouble(DrawableEntity::getTop));

        for (DrawableEntity entity : entities) {
            entity.draw(this.getGraphicsContext2D());
        }
    }

    @Override
    public void collisionHandler(Player player) {
        super.collisionHandler(player);
        if (this.houseEntry.isHit(player) && this.roomId.contains(player.getUserName())) {
            this.fireEvent(new RoomEvent(Game.ROOM_TRANSITION, Main.getUser().getUsername() + "-pr"));
        }
        for (WalkSpace barrier : this.barriers) {
            barrier.isHit(player);
        }
    }
}

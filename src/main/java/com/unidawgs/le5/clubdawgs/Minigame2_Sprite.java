package com.unidawgs.le5.clubdawgs;

import javafx.scene.image.Image;

public class Minigame2_Sprite {
    private int x, y;
    private final int width, height;
    private final Image dogImage;
    private final Image airplaneImage;
    private final int startX;
    private final int startY;

    public Minigame2_Sprite(Image dogImage, Image airplaneImage, int startX, int startY, int width, int height) {
        this.dogImage = dogImage;
        this.airplaneImage = airplaneImage;
        this.x = startX;
        this.y = startY;
        this.width = width;
        this.height = height;
        this.startX = startX;
        this.startY = startY;
    }

    public Image getDogImage() { return dogImage; }
    public Image getAirplaneImage() { return airplaneImage; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public void move(double velocityY, int boardHeight) {
        y += velocityY;
        y = Math.max(y, 0);
        if (y > boardHeight) y = boardHeight;
    }

    public boolean collidesWith(Minigame2_Pipe pipe) {
        double airplaneScaleFactor = 0.1; // same scaling factor as used in the draw method
        int scaledAirplaneHeight = (int) (airplaneImage.getHeight() * airplaneScaleFactor);
        int combinedHeight = height + scaledAirplaneHeight;

        return x < pipe.getX() + pipe.getWidth() && x + width > pipe.getX()
                && y < pipe.getY() + pipe.getHeight() && y + combinedHeight > pipe.getY();
    }

    // Reset dog position to the start values
    public void reset(int startX, int startY) {
        this.x = startX;
        this.y = startY;
    }

}
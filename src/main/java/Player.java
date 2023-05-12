package main.java;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Player {
    private final Image[] movingImagesR;
    private Image movingCurImageR;
    private int movingImageIdxR;

    private  final Image[] movingImagesL;
    private Image movingCurImageL;
    private int movingImageIdxL;

    private  final Image[] movingImagesUp;
    private Image movingCurImageUp;
    private int movingImageIdxUp;

    private  final Image[] movingImagesDown;
    private Image movingCurImageDown;
    private int movingImageIdxDown;

    private double x;
    private double y;
    private boolean[] directions;
    private double playerSize;
    private double screenHeight;
    private double screenWidth;
    private int playerCenterX;
    private int playerCenterY;

    public Player(boolean[] directions, double size, double screenHeight, double screenWidth) {
        this.playerSize = size;

        movingImagesR = new Image[4];
        for (int i = 0; i < 4; i++)
            movingImagesR[i] = new Image(getClass().getResource("/player/MovingRight_" + i + ".png").toString(), playerSize, playerSize, false, false);
        movingCurImageR = movingImagesR[0];

        movingImagesL = new Image[4];
        for (int i = 0; i < 4; i++)
            movingImagesL[i] = new Image(getClass().getResource("/player/MovingLeft_" + i + ".png").toString(), playerSize, playerSize, false, false);
        movingCurImageL = movingImagesL[0];

        movingImagesUp = new Image[4];
        for (int i = 0; i < 4; i++)
            movingImagesUp[i] = new Image(getClass().getResource("/player/MovingUp_" + i + ".png").toString(), playerSize, playerSize, false, false);
        movingCurImageUp = movingImagesUp[0];

        movingImagesDown = new Image[4];
        for (int i = 0; i < 4; i++)
            movingImagesDown[i] = new Image(getClass().getResource("/player/MovingDown_" + i + ".png").toString(), playerSize, playerSize, false, false);
        movingCurImageDown = movingImagesDown[0];

        this.directions = directions;
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
        playerCenterX = (int) (screenWidth - playerSize) / 2;
        playerCenterY = (int) (screenHeight - playerSize) / 2;
    }

    public void nextImage() {
        if(movingImageIdxR == 4) movingImageIdxR = 0;
        movingCurImageR = movingImagesR[movingImageIdxR++];

        if(movingImageIdxL == 4) movingImageIdxL = 0;
        movingCurImageL = movingImagesL[movingImageIdxL++];

        if(movingImageIdxUp == 4) movingImageIdxUp = 0;
        movingCurImageUp = movingImagesUp[movingImageIdxUp++];

        if(movingImageIdxDown == 4) movingImageIdxDown = 0;
        movingCurImageDown = movingImagesDown[movingImageIdxDown++];
    }

    public double wMove() {
       return y = y-3;
    }

    public double aMove() {
        return x = x-3;
    }

    public double sMove() {
        return y = y+3;
    }

    public double dMove() {
        return x = x+3;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
    
    public void render(GraphicsContext gc) {
        if (directions[1]) {
            gc.drawImage(movingCurImageL, playerCenterX, playerCenterY);
        } else if (directions[3]) {
            gc.drawImage(movingCurImageR, playerCenterX, playerCenterY);
        } else if (directions[0]) {
            gc.drawImage(movingCurImageUp, playerCenterX, playerCenterY);
        } else {
            gc.drawImage(movingCurImageDown, playerCenterX, playerCenterY);
        }

    }
}

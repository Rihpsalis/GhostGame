package main;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.File;

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
    private int playerSizeX;
    private int playerSizeY;


    public Player(boolean[] directions, int size) {
        playerSizeX = size;
        playerSizeY = size;

        movingImagesR = new Image[4];
        for (int i = 0; i < 4; i++)
            movingImagesR[i] = new Image(new File("resources/player/MovingRight_" + i + ".png").toString(), playerSizeX, playerSizeY, false, false);
        movingCurImageR = movingImagesR[0];

        movingImagesL = new Image[4];
        for (int i = 0; i < 4; i++)
            movingImagesL[i] = new Image(new File("resources/player/MovingLeft_" + i + ".png").toString(), playerSizeX, playerSizeY, false, false);
        movingCurImageL = movingImagesL[0];

        movingImagesUp = new Image[4];
        for (int i = 0; i < 4; i++)
            movingImagesUp[i] = new Image(new File("resources/player/MovingUp_" + i + ".png").toString(), playerSizeX, playerSizeY, false, false);
        movingCurImageUp = movingImagesUp[0];

        movingImagesDown = new Image[4];
        for (int i = 0; i < 4; i++)
            movingImagesDown[i] = new Image(new File("resources/player/MovingDown_" + i + ".png").toString(), playerSizeX, playerSizeY, false, false);
        movingCurImageDown = movingImagesDown[0];

        this.directions = directions;
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
    
    public void render(GraphicsContext gcFg) {
        if (directions[1]) {
            gcFg.drawImage(movingCurImageL, 1280, 720);
        } else if (directions[3]) {
            gcFg.drawImage(movingCurImageR, 1280, 720);
        } else if (directions[0]) {
            gcFg.drawImage(movingCurImageUp, 1280, 720);
        } else {
            gcFg.drawImage(movingCurImageDown, 1280, 720);
        }
    }
}

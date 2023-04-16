package main;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

import java.io.File;

public class Gridmap { // txt einlesen und ränder hinzufügen
    private Image gridmap;
    private PixelReader pixelReader;
    private Image water, dirt, grass;
    private int terrainSize;
    private int gridmapSizeX;
    private int gridmapSizeY;
    private Player player;
    private double playerX;
    private double playerY;
    public Gridmap(int size, Player player){
        gridmap = new Image("resources/terrain/floor/Gridmap.png");
        terrainSize = size / 4;
        gridmapSizeX = (int) gridmap.getWidth() * terrainSize;
        gridmapSizeY = (int) gridmap.getHeight() * terrainSize;
        pixelReader = gridmap.getPixelReader();
        gridmap = new Image(new File("resources/terrain/floor/Gridmap.png").toString(), gridmapSizeX, gridmapSizeY, false, false);
        this.player = player;
    }
    public void render(GraphicsContext gcBg) {
        gcBg.drawImage(gridmap, playerX, playerY);
    }
    public void update() {
        playerX = - player.getX();
        playerY = - player.getY();
    }
}
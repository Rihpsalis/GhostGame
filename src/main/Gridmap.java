package main;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;

import java.io.*;
import java.sql.SQLOutput;
import java.util.ArrayList;

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
    private FileReader fileReader;
    private BufferedReader bufferedReader;
    private char[][] gridmapChar;
    public Gridmap(int size, Player player) {
        gridmap = new Image("resources/terrain/floor/Gridmap3.png");
        terrainSize = size / 4;
        this.player = player;
        gridmapSizeX = (int) gridmap.getWidth();
        gridmapSizeY = (int) gridmap.getHeight();
        gridmapChar = new char[gridmapSizeX] [gridmapSizeY];
        System.out.println("Xsize: " + gridmapSizeX + "  Ysize: " + gridmapSizeY);

        try {
            fileReader = new FileReader("src/resources/terrain/floor/Gridmap3_values.txt");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        bufferedReader = new BufferedReader(fileReader);

        for (int x = 0; x < gridmapSizeX; x++) {
            for (int y = 0; y < gridmapSizeY; y++) {
                try {
                    gridmapChar[x][y] = (char) bufferedReader.read();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            try {
                bufferedReader.skip(1);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        for (int x = 0; x < gridmapSizeX; x++) {
            for (int y = 0; y < gridmapSizeY; y++) {
                System.out.print(gridmapChar[x][y]);
            }
            System.out.println();
        }
    }
    public void render(GraphicsContext gcBg) {
        gcBg.drawImage(gridmap, playerX, playerY);
    }
    public void update() {
        playerX = - player.getX();
        playerY = - player.getY();
    }
}
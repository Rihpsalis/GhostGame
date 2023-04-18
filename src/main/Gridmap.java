package main;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;

import java.io.*;

public class Gridmap { // txt einlesen und ränder hinzufügen
    private Image gridmap;
    private PixelReader pixelReader;
    private Image water, dirt, grass;
    private int terrainSize;
    private int gridmapHeight;
    private int gridmapWidth;
    private Player player;
    private double playerX;
    private double playerY;
    private FileReader fileReader;
    private BufferedReader bufferedReader;
    private char[][] gridmapChar2D;
    private char[] gridmapChar1D;
    private String buffer;
    public Gridmap(int size, Player player) {
        gridmap = new Image("resources/terrain/floor/Gridmap.png");
        terrainSize = size / 4;
        this.player = player;
        gridmapWidth = (int) gridmap.getWidth();
        gridmapHeight = (int) gridmap.getHeight();
        gridmapChar2D = new char[gridmapWidth][gridmapHeight];
        try {
            fileReader = new FileReader("src/resources/terrain/floor/Gridmap_values.txt");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        bufferedReader = new BufferedReader(fileReader);
        System.out.println("Height: " + gridmapHeight + " Width: " + gridmapWidth);
        for (int i = 0; i < gridmapHeight; i++) {
            try {
                buffer = bufferedReader.readLine();
                gridmapChar1D = buffer.toCharArray();
                for (int j = 0; j < gridmapWidth; j++) {
                    gridmapChar2D[j][i] = gridmapChar1D[j];
                    System.out.print(gridmapChar2D[j][i]);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
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
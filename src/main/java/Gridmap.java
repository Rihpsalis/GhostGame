package main.java;

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

    private Image[] grassToDirt;
    private Image[] grassToWater;
    private Image[] dirtToWater;
    private int spriteBorder;
    private String gridmapInUse;
    private double screenHeight;
    private double screenWidth;
    private final int SIZE;

    //Defines if Test or Main
    private String programStatus;

    // Tiles visible on Screen
    private int onScreenX;
    private int onScreenY;
    

    public Gridmap(int SIZE, Player player, double screenHeight, double screenWidth) {
        programStatus = "main";
        gridmapInUse = "Gridmap";
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
        gridmap = new Image(programStatus + "/resources/terrain/gridmap/" + gridmapInUse + ".png");
        this.SIZE = SIZE;
        spriteBorder = terrainSize / 2;
        this.player = player;/*
        water = new Image(new File("main/resources/terrain/floor/Water.png").toString(), terrainSize, terrainSize, false, false);
        dirt = new Image(new File("main/resources/terrain/floor/Dirt.png").toString(), terrainSize, terrainSize, false, false);
        grass = new Image(new File("main/resources/terrain/floor/Grass.png").toString(), terrainSize, terrainSize, false, false);
        */ //Debugging
        water = new Image(new File("test/resources/terrain/floor/Debug.png").toString(), SIZE, SIZE, false, false);
        dirt = new Image(new File("test/resources/terrain/floor/Debug.png").toString(), SIZE, SIZE, false, false);
        grass = new Image(new File("test/resources/terrain/floor/Debug.png").toString(), SIZE, SIZE, false, false);

        grassToDirt = new Image[4];
            grassToDirt[0] = new Image(new File("main/resources/terrain/floor/Grass_Dirt_0.png").toString(), SIZE, terrainSize, false, false);
            grassToDirt[1] = new Image(new File("main/resources/terrain/floor/Grass_Dirt_1.png").toString(), terrainSize, terrainSize, false, false);
            grassToDirt[2] = new Image(new File("main/resources/terrain/floor/Grass_Dirt_2.png").toString(), terrainSize, terrainSize, false, false);
            grassToDirt[3] = new Image(new File("main/resources/terrain/floor/Grass_Dirt_3.png").toString(), terrainSize, terrainSize, false, false);
        grassToWater = new Image[4];
            grassToWater[0] = new Image(new File("main/resources/terrain/floor/Grass_Water_0.png").toString(), terrainSize, terrainSize, false, false);
            grassToWater[1] = new Image(new File("main/resources/terrain/floor/Grass_Water_1.png").toString(), terrainSize, terrainSize, false, false);
            grassToWater[2] = new Image(new File("main/resources/terrain/floor/Grass_Water_2.png").toString(), terrainSize, terrainSize, false, false);
            grassToWater[3] = new Image(new File("main/resources/terrain/floor/Grass_Water_3.png").toString(), terrainSize, terrainSize, false, false);
        dirtToWater = new Image[4];
            dirtToWater[0] = new Image(new File("main/resources/terrain/floor/Dirt_Water_0.png").toString(), terrainSize, terrainSize, false, false);
            dirtToWater[1] = new Image(new File("main/resources/terrain/floor/Dirt_Water_1.png").toString(), terrainSize, terrainSize, false, false);
            dirtToWater[2] = new Image(new File("main/resources/terrain/floor/Dirt_Water_2.png").toString(), terrainSize, terrainSize, false, false);
            dirtToWater[3] = new Image(new File("main/resources/terrain/floor/Dirt_Water_3.png").toString(), terrainSize, terrainSize, false, false);

        gridmapWidth = (int) gridmap.getWidth();
        gridmapHeight = (int) gridmap.getHeight();
        gridmapChar2D = new char[gridmapWidth][gridmapHeight];
        try {
            fileReader = new FileReader("src/" + programStatus + "/resources/terrain/gridmap/" + gridmapInUse + "_values.txt");
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
        System.out.println("X: " + (int) playerX/terrainSize + " Y: " + (int) playerY/terrainSize);
        for (int i = 0; i < gridmapHeight; i++) {
            for (int j = 0; j < gridmapWidth; j++) {
                if (gridmapChar2D[j][i] == 'g') {
                    gcBg.drawImage(grass,j*terrainSize + playerX,i*SIZE + playerY);
                } else if (gridmapChar2D[j][i] == 'w') {
                    gcBg.drawImage(water,j*terrainSize + playerX,i*terrainSize + playerY);
                } else if (gridmapChar2D[j][i] == 'd') {
                    gcBg.drawImage(dirt,j*terrainSize + playerX,i*terrainSize + playerY);
                }
            }
        }
/*
        for (int i = 0; i < gridmapHeight; i++) {
            for (int j = 0; j < gridmapWidth; j++) {
                //System.out.println("i: " + i + " j: " + j);
                switch (gridmapChar2D[j][i]) {
                    case 'g' -> {
                        //Oben
                        if (i != 0) {
                            switch (gridmapChar2D[j][i - 1]) {
                                case 'd' -> {
                                    gcBg.drawImage(grassToDirt[0], j * terrainSize, terrainSize * i - spriteBorder);
                                }
                                case 'w' -> {
                                    gcBg.drawImage(grassToWater[0], j * terrainSize, terrainSize * i - spriteBorder);
                                }
                            }
                        }

                        //Links
                        if (j != 0) {
                            switch (gridmapChar2D[j - 1][i]) {
                                case 'd' -> {
                                    gcBg.drawImage(grassToDirt[1], terrainSize * j - spriteBorder, i * terrainSize);
                                }
                                case 'w' -> {
                                    gcBg.drawImage(grassToWater[1], terrainSize * j - spriteBorder, i * terrainSize);
                                }
                            }
                        }

                        //Unten
                        if (i != gridmapHeight - 1) {
                            switch (gridmapChar2D[j][i + 1]) {
                                case 'd' -> {
                                    gcBg.drawImage(grassToDirt[2], j * terrainSize, spriteBorder + i * terrainSize);
                                }
                                case 'w' -> {
                                    gcBg.drawImage(grassToWater[2], j * terrainSize, spriteBorder + i * terrainSize);
                                }
                            }
                        }

                        //Rechts
                        if (j != gridmapWidth - 1) {
                            switch (gridmapChar2D[j + 1][i]) {
                                case 'd' -> {
                                    gcBg.drawImage(grassToDirt[3], spriteBorder + j * terrainSize, i * terrainSize);
                                }
                                case 'w' -> {
                                    gcBg.drawImage(grassToWater[3], spriteBorder + j * terrainSize, i * terrainSize);
                                }
                            }
                        }
                    }
                    case 'd' -> {
                        //Oben
                        if (i != 0) {
                            if (gridmapChar2D[j][i - 1] == 'w') {
                                gcBg.drawImage(dirtToWater[0], j * terrainSize, terrainSize * i - spriteBorder);
                            }
                        }

                        //Links
                        if (j != 0) {
                            if (gridmapChar2D[j - 1][i] == 'w') {
                                gcBg.drawImage(dirtToWater[1], terrainSize * j - spriteBorder, i * terrainSize);
                            }
                        }

                        //Unten
                        if (i != gridmapHeight - 1) {
                            if (gridmapChar2D[j][i + 1] == 'w') {
                                gcBg.drawImage(dirtToWater[2], j * terrainSize, spriteBorder + i * terrainSize);
                            }
                        }

                        //Rechts
                        if (j != gridmapWidth - 1) {
                            if (gridmapChar2D[j + 1][i] == 'w') {
                                gcBg.drawImage(dirtToWater[3], spriteBorder + j * terrainSize, i * terrainSize);
                            }
                        }
                    }
                }
            }
        }
        */

    }
    public void update() {
        playerX = - player.getX();
        playerY = - player.getY();
        onScreenX = (int) Math.ceil(screenWidth/terrainSize) ;
        onScreenY = (int) Math.ceil(screenHeight/terrainSize) ;
    }
}
package ghostgame;

import static ghostgame.ResourceLoader.tile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;

public class Gridmap { // txt einlesen und ränder hinzufügen
	private Image gridmap;
	private PixelReader pixelReader;
	private Image water, dirt, grass;
	private int gridmapHeight;
	private int gridmapWidth;
	private Player player;
	private double playerX;
	private double playerY;
	private InputStreamReader reader;
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
	private final int terrainSize;

	// Tiles visible on Screen
	private int onScreenX;
	private int onScreenY;

	public Gridmap(int size, Player player, double screenHeight, double screenWidth, String gridmapInUse) {
		this.gridmapInUse = gridmapInUse;
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		gridmap = ResourceLoader.image("terrain/gridmap/" + gridmapInUse + ".png");
		this.terrainSize = size / 4;
		spriteBorder = this.terrainSize / 2;
		this.player = player;
		water = tile("terrain/floor/Water.png", terrainSize);
		dirt = tile("terrain/floor/Dirt.png", terrainSize);
		grass = tile("terrain/floor/Grass.png", terrainSize);
		/*
		 * //Debugging water = new Image(new File("test/resources/terrain/floor/Debug.png").toString(), terrainSize,
		 * terrainSize, false, false); dirt = new Image(new File("test/resources/terrain/floor/Debug.png").toString(),
		 * terrainSize, terrainSize, false, false); grass = new Image(new
		 * File("test/resources/terrain/floor/Debug.png").toString(), terrainSize, terrainSize, false, false);
		 */
		grassToDirt = new Image[4];
		for (int i = 0; i < 4; i++) {
			grassToDirt[i] = tile("terrain/floor/Grass_Dirt_" + i + ".png", terrainSize);
		}
		grassToWater = new Image[4];
		for (int i = 0; i < 4; i++) {
			grassToWater[0] = tile("terrain/floor/Grass_Water_" + i + ".png", terrainSize);
		}
		dirtToWater = new Image[4];
		for (int i = 0; i < 4; i++) {
			dirtToWater[0] = tile("terrain/floor/Dirt_Water_" + i + ".png", terrainSize);
		}
		gridmapWidth = (int) gridmap.getWidth();
		gridmapHeight = (int) gridmap.getHeight();
		gridmapChar2D = new char[gridmapWidth][gridmapHeight];
		try {
			reader = new InputStreamReader(
					ResourceLoader.urlFromRelPath("terrain/gridmap/" + gridmapInUse + "_values.txt").openStream());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		bufferedReader = new BufferedReader(reader);
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

	public void render(GraphicsContext gc) {
//		System.out.println("X: " + (int) playerX / terrainSize + " Y: " + (int) playerY / terrainSize);
		for (int i = 0; i < gridmapHeight; i++) {
			for (int j = 0; j < gridmapWidth; j++) {
				if (gridmapChar2D[j][i] == 'g') {
					gc.drawImage(grass, j * terrainSize + playerX, i * terrainSize + playerY);
				} else if (gridmapChar2D[j][i] == 'w') {
					gc.drawImage(water, j * terrainSize + playerX, i * terrainSize + playerY);
				} else if (gridmapChar2D[j][i] == 'd') {
					gc.drawImage(dirt, j * terrainSize + playerX, i * terrainSize + playerY);
				}
			}
		}
		/*
		 * for (int i = 0; i < gridmapHeight; i++) { for (int j = 0; j < gridmapWidth; j++) { //System.out.println("i: " + i
		 * + " j: " + j); switch (gridmapChar2D[j][i]) { case 'g' -> { //Oben if (i != 0) { switch (gridmapChar2D[j][i - 1])
		 * { case 'd' -> { gc.drawImage(grassToDirt[0], j * terrainSize, terrainSize * i - spriteBorder); } case 'w' -> {
		 * gc.drawImage(grassToWater[0], j * terrainSize, terrainSize * i - spriteBorder); } } }
		 * 
		 * //Links if (j != 0) { switch (gridmapChar2D[j - 1][i]) { case 'd' -> { gc.drawImage(grassToDirt[1], terrainSize *
		 * j - spriteBorder, i * terrainSize); } case 'w' -> { gc.drawImage(grassToWater[1], terrainSize * j - spriteBorder,
		 * i * terrainSize); } } }
		 * 
		 * //Unten if (i != gridmapHeight - 1) { switch (gridmapChar2D[j][i + 1]) { case 'd' -> {
		 * gc.drawImage(grassToDirt[2], j * terrainSize, spriteBorder + i * terrainSize); } case 'w' -> {
		 * gc.drawImage(grassToWater[2], j * terrainSize, spriteBorder + i * terrainSize); } } }
		 * 
		 * //Rechts if (j != gridmapWidth - 1) { switch (gridmapChar2D[j + 1][i]) { case 'd' -> {
		 * gc.drawImage(grassToDirt[3], spriteBorder + j * terrainSize, i * terrainSize); } case 'w' -> {
		 * gc.drawImage(grassToWater[3], spriteBorder + j * terrainSize, i * terrainSize); } } } } case 'd' -> { //Oben if
		 * (i != 0) { if (gridmapChar2D[j][i - 1] == 'w') { gc.drawImage(dirtToWater[0], j * terrainSize, terrainSize * i -
		 * spriteBorder); } }
		 * 
		 * //Links if (j != 0) { if (gridmapChar2D[j - 1][i] == 'w') { gc.drawImage(dirtToWater[1], terrainSize * j -
		 * spriteBorder, i * terrainSize); } }
		 * 
		 * //Unten if (i != gridmapHeight - 1) { if (gridmapChar2D[j][i + 1] == 'w') { gc.drawImage(dirtToWater[2], j *
		 * terrainSize, spriteBorder + i * terrainSize); } }
		 * 
		 * //Rechts if (j != gridmapWidth - 1) { if (gridmapChar2D[j + 1][i] == 'w') { gc.drawImage(dirtToWater[3],
		 * spriteBorder + j * terrainSize, i * terrainSize); } } } } } }
		 */

	}

	public void update() {
		playerX = -player.getX();
		playerY = -player.getY();
		onScreenX = (int) Math.ceil(screenWidth / terrainSize);
		onScreenY = (int) Math.ceil(screenHeight / terrainSize);
	}
}
package ghostgame;

import static ghostgame.ResourceLoader.tile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Gridmap { // txt einlesen und ränder hinzufügen

	private Image mapImage;

	private Image tileWater;
	private Image tileDirt;
	private Image tileGrass;
	private final Image[] tilesGrassToDirt = new Image[4];
	private final Image[] tilesGrassToWater = new Image[4];
	private final Image[] tilesDirtToWater = new Image[4];

	private int gridmapHeight;
	private int gridmapWidth;

	private Player player;
	private double playerX;
	private double playerY;

	private char[][] content2D;

	private double screenHeight;
	private double screenWidth;
	private final int terrainSize;

	// Sollte die "Welt" wirklich mit Screenkoordinaten/-größen arbeiten?
	public Gridmap(int size, Player player, double screenHeight, double screenWidth, String gridmapInUse) {
		this.player = player;

		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;

		terrainSize = size / 4;

		mapImage = ResourceLoader.image("terrain/gridmap/" + gridmapInUse + ".png");
		var mapDataURL = ResourceLoader.urlFromRelPath("terrain/gridmap/" + gridmapInUse + "_values.txt");

		// Tiles
		tileWater = tile("terrain/floor/Water.png", terrainSize);
		tileDirt = tile("terrain/floor/Dirt.png", terrainSize);
		tileGrass = tile("terrain/floor/Grass.png", terrainSize);
		for (int i = 0; i < 4; i++) {
			tilesGrassToDirt[i] = tile("terrain/floor/Grass_Dirt_" + i + ".png", terrainSize);
		}
		for (int i = 0; i < 4; i++) {
			tilesGrassToWater[0] = tile("terrain/floor/Grass_Water_" + i + ".png", terrainSize);
		}
		for (int i = 0; i < 4; i++) {
			tilesDirtToWater[0] = tile("terrain/floor/Dirt_Water_" + i + ".png", terrainSize);
		}

		// Verstehe ich nicht:
		gridmapWidth = (int) mapImage.getWidth();
		gridmapHeight = (int) mapImage.getHeight();

		content2D = new char[gridmapWidth][gridmapHeight];

		// Hier sollte man ein "try with resources" verwenden, siehe https://www.baeldung.com/java-try-with-resources
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(mapDataURL.openStream()));
			// Hier sollte man besser über das Einlesen der Zeilen iterieren
			for (int y = 0; y < gridmapHeight; y++) {
				var line = reader.readLine().toCharArray();
				for (int x = 0; x < gridmapWidth; x++) {
					content2D[x][y] = line[x];
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException x) {
					throw new RuntimeException(x);
				}
			}
		}

		// Debugausgabe, Logger verwenden
		printContent(System.out);
	}

	private void printContent(PrintStream out) {
		for (int x = 0; x < gridmapWidth; x++) {
			for (int y = 0; y < gridmapHeight; y++) {
				out.print(content2D[x][y]);
			}
			out.println();
		}
		out.println("Grid rows=" + gridmapHeight + ", cols=" + gridmapWidth + ", tile size=" + terrainSize);
	}

	public void render(GraphicsContext gc) {
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, screenWidth, screenHeight);

		// Keine Debugausgabe hier, denn die Methode wird 60-mal pro Sekunde ausgeführt
//		System.out.println("X: " + (int) playerX / terrainSize + " Y: " + (int) playerY / terrainSize);

		// Auch hier: Was Du wohl möchtest ist, dass der aktuelle Ausschnitt der Welt, den die Kamera definiert,
		// gezeichnet wird
		for (int x = 0; x < gridmapWidth; x++) {
			for (int y = 0; y < gridmapHeight; y++) {
				if (content2D[x][y] == 'g') {
					gc.drawImage(tileGrass, x * terrainSize + playerX, y * terrainSize + playerY);
				} else if (content2D[x][y] == 'w') {
					gc.drawImage(tileWater, x * terrainSize + playerX, y * terrainSize + playerY);
				} else if (content2D[x][y] == 'd') {
					gc.drawImage(tileDirt, x * terrainSize + playerX, y * terrainSize + playerY);
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

	// Diesen Code verstehe ich nicht.
	public void update() {
		playerX = player.getX();
		playerY = -player.getY();
	}
}
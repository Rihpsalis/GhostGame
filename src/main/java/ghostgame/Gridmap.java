package ghostgame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Die Weltkarte.
 */
public class Gridmap {

	private Image mapImage;

	private int terrainSize;
	private Image tileWater;
	private Image tileDirt;
	private Image tileGrass;
	private final Image[] tilesGrassToDirt = new Image[4];
	private final Image[] tilesGrassToWater = new Image[4];
	private final Image[] tilesDirtToWater = new Image[4];

	private int gridmapHeight;
	private int gridmapWidth;
	private char[][] content2D;

	public Gridmap(int size, String mapContentPath, String mapImagePath) {
		terrainSize = size / 4; // ???

		mapImage = ResourceLoader.image(mapImagePath);
		tileWater = ResourceLoader.tile("terrain/floor/Water.png", terrainSize);
		tileDirt = ResourceLoader.tile("terrain/floor/Dirt.png", terrainSize);
		tileGrass = ResourceLoader.tile("terrain/floor/Grass.png", terrainSize);
		for (int i = 0; i < 4; i++) {
			tilesGrassToDirt[i] = ResourceLoader.tile("terrain/floor/Grass_Dirt_" + i + ".png", terrainSize);
		}
		for (int i = 0; i < 4; i++) {
			tilesGrassToWater[0] = ResourceLoader.tile("terrain/floor/Grass_Water_" + i + ".png", terrainSize);
		}
		for (int i = 0; i < 4; i++) {
			tilesDirtToWater[0] = ResourceLoader.tile("terrain/floor/Dirt_Water_" + i + ".png", terrainSize);
		}

		// Verstehe ich nicht:
		gridmapWidth = (int) mapImage.getWidth();
		gridmapHeight = (int) mapImage.getHeight();

		content2D = new char[gridmapWidth][gridmapHeight];

		// Hier sollte man ein "try with resources" verwenden, siehe https://www.baeldung.com/java-try-with-resources
		BufferedReader reader = null;
		try {
			var contentURL = ResourceLoader.urlFromRelPath(mapContentPath);
			reader = new BufferedReader(new InputStreamReader(contentURL.openStream()));
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
	}

	public void printContent(PrintStream out) {
		for (int x = 0; x < gridmapWidth; x++) {
			for (int y = 0; y < gridmapHeight; y++) {
				out.print(content2D[x][y]);
			}
			out.println();
		}
		out.println("Grid rows=" + gridmapHeight + ", cols=" + gridmapWidth + ", tile size=" + terrainSize);
	}

	private Image tileImageAt(int x, int y) {
		return switch (content2D[x][y]) {
		case 'g' -> tileGrass;
		case 'w' -> tileWater;
		case 'd' -> tileDirt;
		default -> null;
		};
	}

	public void render(GraphicsContext gc) {
		for (int x = 0; x < gridmapWidth; x++) {
			for (int y = 0; y < gridmapHeight; y++) {
				var image = tileImageAt(x, y);
				if (image != null) {
					gc.drawImage(image, x * terrainSize, y * terrainSize);
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
}
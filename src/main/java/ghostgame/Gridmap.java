package ghostgame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Die Weltkarte.
 */
public class Gridmap {

	private Image tileWater;
	private Image tileDirt;
	private Image tileGrass;
	private final Image[] tilesGrassToDirt = new Image[4];
	private final Image[] tilesGrassToWater = new Image[4];
	private final Image[] tilesDirtToWater = new Image[4];

	private int tileSize;
	private int numRows = 90;
	private int numCols = 160;
	private char[][] content;

	public Gridmap(int tileSize, String mapContentPath) {
		this.tileSize = tileSize;
		content = new char[numCols][numRows];
		readMapContent(ResourceLoader.url(mapContentPath));

		tileWater = tile("terrain/floor/Water.png");
		tileDirt = tile("terrain/floor/Dirt.png");
		tileGrass = tile("terrain/floor/Grass.png");
		for (int i = 0; i < 4; i++) {
			tilesGrassToDirt[i] = tile("terrain/floor/Grass_Dirt_" + i + ".png");
		}
		for (int i = 0; i < 4; i++) {
			tilesGrassToWater[0] = tile("terrain/floor/Grass_Water_" + i + ".png");
		}
		for (int i = 0; i < 4; i++) {
			tilesDirtToWater[0] = tile("terrain/floor/Dirt_Water_" + i + ".png");
		}
	}

	private Image tile(String path) {
		return ResourceLoader.image(path, tileSize, tileSize, false, false);
	}

	private void readMapContent(URL contentURL) {
		try (var reader = new BufferedReader(new InputStreamReader(contentURL.openStream()))) {
			// Hier sollte man besser über das Einlesen der Zeilen iterieren
			for (int y = 0; y < numRows; y++) {
				var line = reader.readLine().toCharArray();
				for (int x = 0; x < numCols; x++) {
					content[x][y] = line[x];
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public int getTileSize() {
		return tileSize;
	}

	public int getNumCols() {
		return numCols;
	}

	public int getNumRows() {
		return numRows;
	}

	public void printContent(PrintStream out) {
		for (int x = 0; x < numCols; x++) {
			for (int y = 0; y < numRows; y++) {
				out.print(content[x][y]);
			}
			out.println();
		}
		out.println("Grid rows=" + numRows + ", cols=" + numCols + ", tile size=" + tileSize);
	}

	private Image tileImageAt(int x, int y) {
		return switch (content[x][y]) {
		case 'g' -> tileGrass;
		case 'w' -> tileWater;
		case 'd' -> tileDirt;
		default -> null;
		};
	}

	public void render(GraphicsContext gc) {
		for (int x = 0; x < numCols; x++) {
			for (int y = 0; y < numRows; y++) {
				var image = tileImageAt(x, y);
				if (image != null) {
					gc.drawImage(image, x * tileSize, y * tileSize);
				}
			}
		}

		/*
		 * for (int i = 0; i < gridmapHeight; i++) { for (int j = 0; j < gridmapWidth; j++) { //System.out.println("i: " + i
		 * + " j: " + j); switch (gridmapChar2D[j][i]) { case 'g' -> { //Oben if (i != 0) { switch (gridmapChar2D[j][i - 1])
		 * { case 'd' -> { gc.drawImage(grassToDirt[0], j * tileSize, tileSize * i - spriteBorder); } case 'w' -> {
		 * gc.drawImage(grassToWater[0], j * tileSize, tileSize * i - spriteBorder); } } }
		 * 
		 * //Links if (j != 0) { switch (gridmapChar2D[j - 1][i]) { case 'd' -> { gc.drawImage(grassToDirt[1], tileSize * j
		 * - spriteBorder, i * tileSize); } case 'w' -> { gc.drawImage(grassToWater[1], tileSize * j - spriteBorder, i *
		 * tileSize); } } }
		 * 
		 * //Unten if (i != gridmapHeight - 1) { switch (gridmapChar2D[j][i + 1]) { case 'd' -> {
		 * gc.drawImage(grassToDirt[2], j * tileSize, spriteBorder + i * tileSize); } case 'w' -> {
		 * gc.drawImage(grassToWater[2], j * tileSize, spriteBorder + i * tileSize); } } }
		 * 
		 * //Rechts if (j != gridmapWidth - 1) { switch (gridmapChar2D[j + 1][i]) { case 'd' -> {
		 * gc.drawImage(grassToDirt[3], spriteBorder + j * tileSize, i * tileSize); } case 'w' -> {
		 * gc.drawImage(grassToWater[3], spriteBorder + j * tileSize, i * tileSize); } } } } case 'd' -> { //Oben if (i !=
		 * 0) { if (gridmapChar2D[j][i - 1] == 'w') { gc.drawImage(dirtToWater[0], j * tileSize, tileSize * i -
		 * spriteBorder); } }
		 * 
		 * //Links if (j != 0) { if (gridmapChar2D[j - 1][i] == 'w') { gc.drawImage(dirtToWater[1], tileSize * j -
		 * spriteBorder, i * tileSize); } }
		 * 
		 * //Unten if (i != gridmapHeight - 1) { if (gridmapChar2D[j][i + 1] == 'w') { gc.drawImage(dirtToWater[2], j *
		 * tileSize, spriteBorder + i * tileSize); } }
		 * 
		 * //Rechts if (j != gridmapWidth - 1) { if (gridmapChar2D[j + 1][i] == 'w') { gc.drawImage(dirtToWater[3],
		 * spriteBorder + j * tileSize, i * tileSize); } } } } } }
		 */

	}
}
package ghostgame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

/**
 * World map.
 */
public class Gridmap {

	public static final char DIRT = 'd';
	public static final char GRASS = 'g';
	public static final char WATER = 'w';

	private int numRows = 90;
	private int numCols = 160;
	private char[][] content;

	public Gridmap(String mapContentPath) {
		content = new char[numCols][numRows];
		var url = ResourceLoader.url(mapContentPath);
		try (var reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
			// Hier sollte man besser über die eingelesenen Zeilen iterieren
			for (int y = 0; y < numRows; y++) {
				var line = reader.readLine().toCharArray();
				for (int x = 0; x < numCols; x++) {
					content[x][y] = line[x];
				}
			}
		} catch (IOException e) {
			// TODO log error and use specific exception
			throw new RuntimeException(e);
		}
	}

	public int getNumCols() {
		return numCols;
	}

	public int getNumRows() {
		return numRows;
	}

	public char content(int x, int y) {
		return content[x][y];
	}

	public void printContent(PrintStream out) {
		for (int x = 0; x < numCols; x++) {
			for (int y = 0; y < numRows; y++) {
				out.print(content[x][y]);
			}
			out.println();
		}
		out.println("Grid rows=" + numRows + ", cols=" + numCols);
	}

	/*
	 * for (int i = 0; i < gridmapHeight; i++) { for (int j = 0; j < gridmapWidth; j++) { //System.out.println("i: " + i +
	 * " j: " + j); switch (gridmapChar2D[j][i]) { case 'g' -> { //Oben if (i != 0) { switch (gridmapChar2D[j][i - 1]) {
	 * case 'd' -> { gc.drawImage(grassToDirt[0], j * tileSize, tileSize * i - spriteBorder); } case 'w' -> {
	 * gc.drawImage(grassToWater[0], j * tileSize, tileSize * i - spriteBorder); } } }
	 * 
	 * //Links if (j != 0) { switch (gridmapChar2D[j - 1][i]) { case 'd' -> { gc.drawImage(grassToDirt[1], tileSize * j -
	 * spriteBorder, i * tileSize); } case 'w' -> { gc.drawImage(grassToWater[1], tileSize * j - spriteBorder, i *
	 * tileSize); } } }
	 * 
	 * //Unten if (i != gridmapHeight - 1) { switch (gridmapChar2D[j][i + 1]) { case 'd' -> { gc.drawImage(grassToDirt[2],
	 * j * tileSize, spriteBorder + i * tileSize); } case 'w' -> { gc.drawImage(grassToWater[2], j * tileSize,
	 * spriteBorder + i * tileSize); } } }
	 * 
	 * //Rechts if (j != gridmapWidth - 1) { switch (gridmapChar2D[j + 1][i]) { case 'd' -> { gc.drawImage(grassToDirt[3],
	 * spriteBorder + j * tileSize, i * tileSize); } case 'w' -> { gc.drawImage(grassToWater[3], spriteBorder + j *
	 * tileSize, i * tileSize); } } } } case 'd' -> { //Oben if (i != 0) { if (gridmapChar2D[j][i - 1] == 'w') {
	 * gc.drawImage(dirtToWater[0], j * tileSize, tileSize * i - spriteBorder); } }
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
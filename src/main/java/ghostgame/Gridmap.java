package ghostgame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * World map.
 */
public class Gridmap {

	public static final byte DIRT = 0;
	public static final byte GRASS = 1;
	public static final byte WATER = 2;

	private static byte byteValue(char c) {
		if (c == 'd')
			return DIRT;
		if (c == 'g')
			return GRASS;
		if (c == 'w')
			return WATER;
		throw new IllegalArgumentException("Unknown content: " + c);
	}

	private int numRows;
	private int numCols;
	private byte[][] content; // Note: a single *char* is stored in 2 bytes!

	public Gridmap(URL url) {
		var lines = new ArrayList<String>();
		try (var reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
			String line;
			do {
				line = reader.readLine();
				if (line != null) {
					lines.add(line);
				}
			} while (line != null);
		} catch (IOException x) {
			// TODO log error and use more specific exception
			throw new RuntimeException(x);
		}
		if (lines.isEmpty()) {
			throw new IllegalArgumentException("No map data, url=" + url);
		}
		numRows = lines.size();
		numCols = lines.get(0).length();
		content = new byte[numCols][numRows];
		for (int y = 0; y < lines.size(); ++y) {
			var line = lines.get(y);
			if (line.length() != numCols) {
				throw new IllegalArgumentException(
						String.format("Line %d in map data has length %d, should be %d", y, line.length(), numCols));
			}
			for (int x = 0; x < numCols; ++x) {
				content[x][y] = byteValue(line.charAt(x));
			}
		}
	}

	public int getNumCols() {
		return numCols;
	}

	public int getNumRows() {
		return numRows;
	}

	public byte content(int x, int y) {
		return content[x][y];
	}

	public void printContent(PrintStream out) {
		for (int x = 0; x < numCols; x++) {
			for (int y = 0; y < numRows; y++) {
				out.print(content[x][y]);
			}
			out.println();
		}
		out.println("Map rows=" + numRows + ", cols=" + numCols);
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
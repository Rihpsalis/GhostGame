package ghostgame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
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

	public String content() {
		var sb = new StringBuilder();
		sb.append("Map rows: ");
		sb.append(numRows);
		sb.append(" cols: ");
		sb.append(numCols);
		sb.append("\n");
		for (int x = 0; x < numCols; x++) {
			for (int y = 0; y < numRows; y++) {
				sb.append(content[x][y]);
			}
			sb.append("\n");
		}
		return sb.toString();
	}
}
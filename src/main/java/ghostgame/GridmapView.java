/**
 * 
 */
package ghostgame;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * World map view.
 */
public class GridmapView {

	public static boolean debug = true;

	public final IntegerProperty tileSizeProperty = new SimpleIntegerProperty(8) {
		@Override
		protected void invalidated() {
			loadTileImages();
		}
	};

	private Image tileWater;
	private Image tileDirt;
	private Image tileGrass;
	private final Image[] tilesGrassToDirt = new Image[4];
	private final Image[] tilesGrassToWater = new Image[4];
	private final Image[] tilesDirtToWater = new Image[4];

	private final Gridmap map;

	public GridmapView(Gridmap map) {
		this.map = map;
		loadTileImages();
	}

	private void loadTileImages() {
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

	public void render(GraphicsContext g) {
		var ts = tileSizeProperty.get();
		for (int x = 0; x < map.getNumCols(); x++) {
			for (int y = 0; y < map.getNumRows(); y++) {
				var image = tileImageAt(x, y);
				if (image != null) {
					g.drawImage(image, x * ts, y * ts);
				}
			}
		}
		if (debug) {
			drawGridLines(g);
		}
	}

	private void drawGridLines(GraphicsContext g) {
		var ts = tileSizeProperty.get();
		g.setStroke(Color.gray(0.8));
		g.setLineWidth(0.3);
		for (int y = 0; y < map.getNumRows(); y++) {
			g.strokeLine(0, y * ts, map.getNumCols() * ts, y * ts);
		}
		for (int x = 0; x < map.getNumCols(); x++) {
			g.strokeLine(x * ts, 0, x * ts, map.getNumRows() * ts);
		}
	}

	private Image tile(String path) {
		var ts = tileSizeProperty.get();
		return ResourceLoader.image(path, ts, ts, false, false);
	}

	private Image tileImageAt(int x, int y) {
		switch (map.content(x, y)) {
		case Gridmap.GRASS:
			return tileGrass;
		case Gridmap.WATER:
			return tileWater;
		case Gridmap.DIRT:
			return tileDirt;
		default:
			return null;
		}
	}
}
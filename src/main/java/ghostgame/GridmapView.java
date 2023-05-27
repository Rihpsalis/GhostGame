/**
 * 
 */
package ghostgame;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * World map view.
 */
public class GridmapView {

	public static boolean debug = true;

	private Image tileWater;
	private Image tileDirt;
	private Image tileGrass;
	private final Image[] tilesGrassToDirt = new Image[4];
	private final Image[] tilesGrassToWater = new Image[4];
	private final Image[] tilesDirtToWater = new Image[4];

	private final Gridmap map;

	public GridmapView(Gridmap map) {
		this.map = map;

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
		for (int x = 0; x < map.getNumCols(); x++) {
			for (int y = 0; y < map.getNumRows(); y++) {
				var image = tileImageAt(x, y);
				if (image != null) {
					g.drawImage(image, x * App.TILESIZE, y * App.TILESIZE);
				}
			}
		}
		if (debug) {
			drawGridLines(g);
		}
	}

	private void drawGridLines(GraphicsContext g) {
		var ts = App.TILESIZE;
		g.setStroke(Color.gray(0.8));
		for (int y = 0; y < map.getNumRows(); y++) {
			g.strokeLine(0, y * ts, map.getNumCols() * ts, y * ts);
		}
		for (int x = 0; x < map.getNumCols(); x++) {
			g.strokeLine(x * ts, 0, x * ts, map.getNumRows() * ts);
		}
	}

	private Image tile(String path) {
		return ResourceLoader.image(path, App.TILESIZE, App.TILESIZE, false, false);
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
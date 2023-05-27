/**
 * 
 */
package ghostgame;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * World map view.
 */
public class GridmapView {

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
					g.drawImage(image, x * map.getTileSize(), y * map.getTileSize());
				}
			}
		}
	}

	private Image tile(String path) {
		return ResourceLoader.image(path, map.getTileSize(), map.getTileSize(), false, false);
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
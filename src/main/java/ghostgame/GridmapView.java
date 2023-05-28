package ghostgame;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * World map view.
 */
public class GridmapView {

	public final BooleanProperty debugProperty = new SimpleBooleanProperty(false);

	public final IntegerProperty tileSizeProperty = new SimpleIntegerProperty(8) {
		@Override
		protected void invalidated() {
			reloadImages();
		}
	};

	private Image tileDirt;
	private Image tileGrass;
	private Image tileWater;

	private final Image[] tilesDirtToWater = new Image[4];
	private final Image[] tilesGrassToDirt = new Image[4];
	private final Image[] tilesGrassToWater = new Image[4];

	private final Gridmap map;
	private Font font;

	public GridmapView(Gridmap map) {
		this.map = map;
		reloadImages();
	}

	private void reloadImages() {
		tileDirt = tile("terrain/floor/Dirt.png");
		tileGrass = tile("terrain/floor/Grass.png");
		tileWater = tile("terrain/floor/Water.png");
		for (int i = 0; i < 4; i++) {
			tilesDirtToWater[i] = tile("terrain/floor/Dirt_Water_" + i + ".png");
			tilesGrassToDirt[i] = tile("terrain/floor/Grass_Dirt_" + i + ".png");
			tilesGrassToWater[i] = tile("terrain/floor/Grass_Water_" + i + ".png");
		}
		font = Font.font("Monospaced", tileSizeProperty.get() * 0.75);
	}

	public void render(GraphicsContext g) {
		var ts = tileSizeProperty.get();
		for (int x = 0; x < map.getNumCols(); x++) {
			for (int y = 0; y < map.getNumRows(); y++) {
				var img = tileImageAt(x, y);
				if (x > 0 && x < map.getNumCols() - 1 && y > 0 && y < map.getNumRows() - 1) {
					byte c = map.content(x, y);

					// das geht bestimmt auch kürzer, aber erst korrekt, dann schön machen!

					byte n = map.content(x, y - 1);
					byte s = map.content(x, y + 1);
					byte e = map.content(x + 1, y);
					byte w = map.content(x - 1, y);

					if (c == Gridmap.DIRT && n == Gridmap.WATER) {
						img = tilesDirtToWater[0];
					} else if (c == Gridmap.DIRT && w == Gridmap.WATER) {
						img = tilesDirtToWater[1];
					} else if (c == Gridmap.DIRT && s == Gridmap.WATER) {
						img = tilesDirtToWater[2];
					} else if (c == Gridmap.DIRT && e == Gridmap.WATER) {
						img = tilesDirtToWater[3];
					}

					else if (c == Gridmap.GRASS && n == Gridmap.DIRT) {
						img = tilesGrassToDirt[0];
					} else if (c == Gridmap.GRASS && w == Gridmap.DIRT) {
						img = tilesGrassToDirt[1];
					} else if (c == Gridmap.GRASS && s == Gridmap.DIRT) {
						img = tilesGrassToDirt[2];
					} else if (c == Gridmap.GRASS && e == Gridmap.DIRT) {
						img = tilesGrassToDirt[3];
					}

					else if (c == Gridmap.GRASS && n == Gridmap.WATER) {
						img = tilesGrassToWater[0];
					} else if (c == Gridmap.GRASS && w == Gridmap.WATER) {
						img = tilesGrassToWater[1];
					} else if (c == Gridmap.GRASS && s == Gridmap.WATER) {
						img = tilesGrassToWater[2];
					} else if (c == Gridmap.GRASS && e == Gridmap.WATER) {
						img = tilesGrassToWater[3];
					}
				}
				g.drawImage(img, x * ts, y * ts);
			}
		}
		if (debugProperty.get()) {
			drawGridDebugInfo(g, font);
		}
	}

	private void drawGridDebugInfo(GraphicsContext g, Font font) {
		var ts = tileSizeProperty.get();
		g.setStroke(Color.gray(0.8));
		g.setLineWidth(0.3);
		for (int y = 0; y < map.getNumRows(); y++) {
			g.strokeLine(0, y * ts, map.getNumCols() * ts, y * ts);
		}
		for (int x = 0; x < map.getNumCols(); x++) {
			g.strokeLine(x * ts, 0, x * ts, map.getNumRows() * ts);
		}
		if (font.getSize() > 6) {
			g.setFill(Color.gray(0.75));
			g.setFont(font);
			for (int x = 0; x < map.getNumCols(); x++) {
				for (int y = 0; y < map.getNumRows(); ++y) {
					byte c = map.content(x, y);
					String text = "?";
					if (c == Gridmap.DIRT) {
						text = "d";
					} else if (c == Gridmap.GRASS) {
						text = "g";
					} else if (c == Gridmap.WATER) {
						text = "w";
					}
					g.fillText(text, x * ts + 0.25 * ts, y * ts + 0.6 * ts);
				}
			}
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
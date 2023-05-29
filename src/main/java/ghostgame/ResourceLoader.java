package ghostgame;

import java.net.URL;
import java.util.MissingResourceException;
import java.util.Objects;

import javafx.scene.image.Image;

public class ResourceLoader {
	/* The path under your src/main/resources folder where you store your images... */
	private static final String ROOT = "/";

	/**
	 * @param path path to resource
	 * @return URL of resource addressed by this path. Never returns <code>null</code>!
	 */
	public static URL url(String path) {
		Objects.requireNonNull(path);
		if (!path.startsWith("/")) {
			path = ROOT + path;
		}
		URL url = ResourceLoader.class.getResource(path);
		if (url == null) {
			throw new MissingResourceException("Missing resource, path=" + path, "", path);
		}
		return url;
	}

	/**
	 * @param path relative path (without leading slash) starting from resource root directory
	 * @return image loaded from resource addressed by this path.
	 */
	public static Image image(String path) {
		var url = url(path);
		return new Image(url.toExternalForm());
	}

	public static Image image(String path, double width, double height, boolean preserveRatio, boolean smooth) {
		var url = url(path);
		return new Image(url.toExternalForm(), width, height, preserveRatio, smooth);
	}

	public static Image sprite(String path, double spriteSize) {
		return image(path, spriteSize, spriteSize, false, true);
	}
}
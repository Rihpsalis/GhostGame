package ghostgame;

import java.net.URL;
import java.util.MissingResourceException;
import java.util.Objects;

import javafx.scene.image.Image;

public class ResourceLoader {
	/* The path under your src/main/resources folder where you store your images... */
	private static final String ROOT = "/";

	/**
	 * @param relativePath relative path (without leading slash) starting from resource root directory
	 * @return full path to resource including path to resource root directory
	 */
	private static String toFullPath(String relativePath) {
		return ROOT + relativePath;
	}

	/**
	 * @param relPath relative path (without leading slash) starting from resource root directory
	 * @return URL of resource addressed by this path. Never returns <code>null</code>!
	 */
	public static URL urlFromRelPath(String relPath) {
		return url(toFullPath(relPath));
	}

	/**
	 * @param fullPath full path to resource including path to resource root directory
	 * @return URL of resource addressed by this path. Never returns <code>null</code>!
	 */
	public static URL url(String fullPath) {
		Objects.requireNonNull(fullPath);
		URL url = ResourceLoader.class.getResource(fullPath);
		if (url == null) {
			throw new MissingResourceException("Missing resource, path=" + fullPath, "", fullPath);
		}
		return url;
	}

	/**
	 * @param path relative path (without leading slash) starting from resource root directory
	 * @return image loaded from resource addressed by this path.
	 */
	public static Image image(String path) {
		var url = urlFromRelPath(path);
		return new Image(url.toExternalForm());
	}

	public static Image image(String path, double width, double height, boolean preserveRatio, boolean smooth) {
		var url = urlFromRelPath(path);
		return new Image(url.toExternalForm(), width, height, preserveRatio, smooth);
	}

	public static Image tile(String path, double size) {
		return image(path, size, size, false, false);
	}
}
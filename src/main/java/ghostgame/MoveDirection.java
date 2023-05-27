/**
 * 
 */
package ghostgame;

import javafx.geometry.Point2D;

/**
 * Possible player move directions or none if standing still.
 */
public enum MoveDirection {

	NONE(0, 0), N(0, -1), S(0, 1), E(1, 0), W(-1, 0), NW(-1, -1), NE(1, -1), SW(-1, 1), SE(1, 1);

	public final Point2D vector;

	private MoveDirection(double dx, double dy) {
		vector = new Point2D(dx, dy);
	}
}
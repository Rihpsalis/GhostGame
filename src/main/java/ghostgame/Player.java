package ghostgame;

import javafx.geometry.Point2D;

/**
 * The ghost.
 */
public class Player {

	private Point2D center;
	private MoveDirection moveDir;
	private double speed;

	public Player() {
		moveDir = MoveDirection.NONE;
		center = Point2D.ZERO;
		speed = 1.0;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public void move() {
		var velocity = moveDir.vector.multiply(speed);
		center = center.add(velocity);
	}

	public Point2D center() {
		return center;
	}

	public void setCenter(Point2D p) {
		center = p;
	}

	public void setCenter(double x, double y) {
		center = new Point2D(x, y);
	}

	public void setMoveDir(MoveDirection dir) {
		this.moveDir = dir;
	}

	public MoveDirection getMoveDir() {
		return moveDir;
	}
}
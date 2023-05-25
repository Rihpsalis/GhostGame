package ghostgame;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

/**
 * Der Geist.
 */
public class Player {

	public static boolean debug = true;

	public static final Point2D DIRECTION_NONE = new Point2D(0, 0);

	public static final Point2D DIRECTION_N = new Point2D(0, -1);
	public static final Point2D DIRECTION_S = new Point2D(0, 1);
	public static final Point2D DIRECTION_E = new Point2D(-1, 0);
	public static final Point2D DIRECTION_W = new Point2D(1, 0);

	public static final Point2D DIRECTION_NW = DIRECTION_N.add(DIRECTION_W);
	public static final Point2D DIRECTION_NE = DIRECTION_N.add(DIRECTION_E);
	public static final Point2D DIRECTION_SW = DIRECTION_S.add(DIRECTION_W);
	public static final Point2D DIRECTION_SE = DIRECTION_S.add(DIRECTION_E);

	private final SpriteAnimation animationMovingRight;
	private final SpriteAnimation animationMovingLeft;
	private final SpriteAnimation animationMovingUp;
	private final SpriteAnimation animationMovingDown;
	private final SpriteAnimation animationStandingStill;

	// In welchem Koordinatensystem?
	private double x;
	private double y;

	private Point2D moveDirection;

	private double speed = 3.0;

	// Was genau ist diese "Size", die Größe des Sprite?
	private double size;

	public Player(double size, double screenHeight, double screenWidth) {
		this.size = size;

		animationMovingRight = new SpriteAnimation(Duration.millis(100), //
				sprite("player/MovingRight_0.png"), //
				sprite("player/MovingRight_1.png"), //
				sprite("player/MovingRight_2.png"), //
				sprite("player/MovingRight_3.png"));

		animationMovingLeft = new SpriteAnimation(Duration.millis(100), //
				sprite("player/MovingLeft_0.png"), //
				sprite("player/MovingLeft_1.png"), //
				sprite("player/MovingLeft_2.png"), //
				sprite("player/MovingLeft_3.png"));

		animationMovingDown = new SpriteAnimation(Duration.millis(100), //
				sprite("player/MovingDown_0.png"), //
				sprite("player/MovingDown_1.png"), //
				sprite("player/MovingDown_2.png"), //
				sprite("player/MovingDown_3.png"));

		animationMovingUp = new SpriteAnimation(Duration.millis(100), //
				sprite("player/MovingUp_0.png"), //
				sprite("player/MovingUp_1.png"), //
				sprite("player/MovingUp_2.png"), //
				sprite("player/MovingUp_3.png"));

		animationStandingStill = new SpriteAnimation(Duration.millis(500), //
				sprite("player/StandingStill_0.png"), //
				sprite("player/StandingStill_1.png"), //
				sprite("player/StandingStill_2.png"), //
				sprite("player/StandingStill_3.png"));

		moveDirection = DIRECTION_NONE;
	}

	private Image sprite(String path) {
		return ResourceLoader.image(path, size, size, false, false);
	}

	public double getSize() {
		return size;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public void move() {
		var velocity = moveDirection.multiply(speed);
		x += velocity.getX();
		y += velocity.getY();
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public void setMoveDirection(Point2D moveDirection) {
		this.moveDirection = moveDirection;
	}

	public Point2D getMoveDirection() {
		return moveDirection;
	}

	public void startAnimations() {
		animationMovingRight.start();
		animationMovingLeft.start();
		animationMovingUp.start();
		animationMovingDown.start();
		animationStandingStill.start();
	}

	// TODO Sollte nicht der Player an seiner eigenen Position gezeichnet werden und die Kamera der
	// Spielszene dafür sorgen, dass er immer in der Mitte des Fensters erscheint?
	public void render(GraphicsContext gc, double screenX, double screenY) {
		var animation = selectAnimation();
		gc.drawImage(animation.currentSprite(), screenX, screenY);
		if (debug) {
			drawAnimationInfo(gc, animation, screenX, screenY);
		}
	}

	// Bei diagonalen Richtungen man sich halt entscheiden, welche Animation man nimmt.
	private SpriteAnimation selectAnimation() {
		if (moveDirection.equals(DIRECTION_N)) {
			return animationMovingUp;
		}
		if (moveDirection.equals(DIRECTION_W) || moveDirection.equals(DIRECTION_NW) || moveDirection.equals(DIRECTION_SW)) {
			return animationMovingLeft;
		}
		if (moveDirection.equals(DIRECTION_S)) {
			return animationMovingDown;
		}
		if (moveDirection.equals(DIRECTION_E) || moveDirection.equals(DIRECTION_NE) || moveDirection.equals(DIRECTION_SE)) {
			return animationMovingRight;
		}
		return animationStandingStill;
	}

	private void drawAnimationInfo(GraphicsContext gc, SpriteAnimation animation, double screenX, double screenY) {
		var animationName = "Animation";
		if (animation == animationMovingDown) {
			animationName = "Moving Down";
		} else if (animation == animationMovingUp) {
			animationName = "Moving Up";
		} else if (animation == animationMovingLeft) {
			animationName = "Moving Left";
		} else if (animation == animationMovingRight) {
			animationName = "Moving Right";
		} else if (animation == animationStandingStill) {
			animationName = "Standing Still";
		}
		var animationText = "%s (%s ,frame %d)".formatted(animationName, animation.getDuration(), animation.getFrame());
		gc.setFill(Color.BLUE);
		gc.setFont(Font.font("Sans", FontWeight.BLACK, 16));
		gc.fillText(animationText, screenX, screenY - 5);
	}
}
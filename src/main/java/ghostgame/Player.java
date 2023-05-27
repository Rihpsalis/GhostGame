package ghostgame;

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

	private final SpriteAnimation animationMovingRight;
	private final SpriteAnimation animationMovingLeft;
	private final SpriteAnimation animationMovingUp;
	private final SpriteAnimation animationMovingDown;
	private final SpriteAnimation animationStandingStill;

	private double x;
	private double y;
	private MoveDirection moveDir;
	private double speed = 3.0;
	private double size;

	public Player(double size) {
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

		moveDir = MoveDirection.NONE;
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
		var velocity = moveDir.vector.multiply(speed);
		x += velocity.getX();
		y += velocity.getY();
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setMoveDir(MoveDirection dir) {
		this.moveDir = dir;
	}

	public MoveDirection getMoveDir() {
		return moveDir;
	}

	public void startAnimations() {
		animationMovingRight.start();
		animationMovingLeft.start();
		animationMovingUp.start();
		animationMovingDown.start();
		animationStandingStill.start();
	}

	public void stopAnimations() {
		animationMovingRight.stop();
		animationMovingLeft.stop();
		animationMovingUp.stop();
		animationMovingDown.stop();
		animationStandingStill.stop();
	}

	public void render(GraphicsContext gc) {
		var animation = selectAnimation();
		var sprite = animation.currentSprite();
		gc.drawImage(sprite, x - 0.5 * sprite.getWidth(), y - 0.5 * sprite.getHeight());
		if (debug) {
			drawPlayerInfo(gc, animation);
		}
	}

	// Bei diagonalen Richtungen man sich halt entscheiden, welche Animation man nimmt.
	private SpriteAnimation selectAnimation() {
		if (moveDir.equals(MoveDirection.N)) {
			return animationMovingUp;
		}
		if (moveDir.equals(MoveDirection.W) || moveDir.equals(MoveDirection.NW) || moveDir.equals(MoveDirection.SW)) {
			return animationMovingLeft;
		}
		if (moveDir.equals(MoveDirection.S)) {
			return animationMovingDown;
		}
		if (moveDir.equals(MoveDirection.E) || moveDir.equals(MoveDirection.NE) || moveDir.equals(MoveDirection.SE)) {
			return animationMovingRight;
		}
		return animationStandingStill;
	}

	private void drawPlayerInfo(GraphicsContext gc, SpriteAnimation animation) {

		String infoText = String.format("Ghost: x=%.2f y=%.2f%nmoveDir=%s", x, y, moveDir);
		var animationName = "Animation: ";
		if (animation == animationMovingDown) {
			animationName += "Moving Down";
		} else if (animation == animationMovingUp) {
			animationName += "Moving Up";
		} else if (animation == animationMovingLeft) {
			animationName += "Moving Left";
		} else if (animation == animationMovingRight) {
			animationName += "Moving Right";
		} else if (animation == animationStandingStill) {
			animationName += "Standing Still";
		}
		var animationText = String.format("%s (%s, frame %d)", animationName, animation.getDuration(),
				animation.getFrame());

		infoText += "\n";
		infoText += animationText;
		gc.setFill(Color.BLUE);
		gc.setFont(Font.font("Sans", FontWeight.BLACK, 16));
		gc.fillText(infoText, x + 40, y - 20);
	}
}
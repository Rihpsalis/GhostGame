package ghostgame;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

/**
 * @author Armin Reichert
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

	// user interface part

	public final BooleanProperty debugProperty = new SimpleBooleanProperty(false);

	public final IntegerProperty tileSizeProperty = new SimpleIntegerProperty(8) {
		@Override
		protected void invalidated() {
			moveAnimation.setSpriteSize(spriteSizeInTiles * get());
			standingAnimation.setSpriteSize(spriteSizeInTiles * get());
		}
	};

	private final PlayerMoveAnimation moveAnimation = new PlayerMoveAnimation(this, Duration.millis(100));
	private final PlayerStandingAnimation standingAnimation = new PlayerStandingAnimation(Duration.millis(500));

	private double spriteSizeInTiles = 3.0;

	public void startAnimations() {
		moveAnimation.start();
		standingAnimation.start();
	}

	public void stopAnimations() {
		moveAnimation.stop();
		standingAnimation.stop();
	}

	public int getTileSize() {
		return tileSizeProperty.get();
	}

	public void setTileSize(int size) {
		tileSizeProperty.set(size);
	}

	public void setSpriteSizeInTiles(double numTiles) {
		if (this.spriteSizeInTiles != numTiles) {
			this.spriteSizeInTiles = numTiles;
			moveAnimation.setSpriteSize(numTiles * getTileSize());
			standingAnimation.setSpriteSize(numTiles * getTileSize());
		}
	}

	public void render(GraphicsContext g) {
		var animation = getMoveDir() == MoveDirection.NONE ? standingAnimation : moveAnimation;
		var sprite = animation.currentSprite();

		// Draw sprite centered over player center position
		var spritePos = center.subtract(sprite.getWidth() / 2, sprite.getHeight() / 2);
		g.drawImage(sprite, spritePos.getX(), spritePos.getY());

		if (debugProperty.get()) {
			var ts = getTileSize();
			var tilesPerSecond = speed * 60; // assume 60Hz
			var animationText = String.format("%s: (%.2f ms/frame, frame %d|%d)", animation.name(),
					animation.frameDuration().toMillis(), animation.currentFrame() + 1, animation.numFrames());
			var infoText = String.format("center=(%.2f, %.2f)%nmoveDir=%s%nspeed=%.2f (%.2f tiles(sec @ 60Hz)%n%s",
					center.getX(), center.getY(), moveDir, speed, tilesPerSecond, animationText);
			g.setFill(Color.ORANGE);
			g.setFont(Font.font("Sans", FontWeight.BLACK, 16));
			g.fillText(infoText, center.getX() + ts, center.getY() - ts);
		}
	}
}
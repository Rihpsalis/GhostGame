package ghostgame;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

/**
 * Visual representation of player.
 */
public class PlayerView {

	public static boolean debug = true;

	public final IntegerProperty tileSizeProperty = new SimpleIntegerProperty(8) {
		@Override
		protected void invalidated() {
			stopAnimations();
			createAnimations(spriteSizeInTiles * get());
			startAnimations();
		}
	};

	private final Player player;

	private final double spriteSizeInTiles;
	private SpriteAnimation animationMovingRight;
	private SpriteAnimation animationMovingLeft;
	private SpriteAnimation animationMovingUp;
	private SpriteAnimation animationMovingDown;
	private SpriteAnimation animationStandingStill;

	public PlayerView(Player player, double spriteSizeInTiles) {
		this.player = player;
		this.spriteSizeInTiles = spriteSizeInTiles;
		createAnimations(spriteSizeInTiles * tileSizeProperty.get());
	}

	private void createAnimations(double spriteSize) {
		animationMovingRight = new SpriteAnimation(Duration.millis(100), //
				sprite("player/MovingRight_0.png", spriteSize), //
				sprite("player/MovingRight_1.png", spriteSize), //
				sprite("player/MovingRight_2.png", spriteSize), //
				sprite("player/MovingRight_3.png", spriteSize));

		animationMovingLeft = new SpriteAnimation(Duration.millis(100), //
				sprite("player/MovingLeft_0.png", spriteSize), //
				sprite("player/MovingLeft_1.png", spriteSize), //
				sprite("player/MovingLeft_2.png", spriteSize), //
				sprite("player/MovingLeft_3.png", spriteSize));

		animationMovingDown = new SpriteAnimation(Duration.millis(100), //
				sprite("player/MovingDown_0.png", spriteSize), //
				sprite("player/MovingDown_1.png", spriteSize), //
				sprite("player/MovingDown_2.png", spriteSize), //
				sprite("player/MovingDown_3.png", spriteSize));

		animationMovingUp = new SpriteAnimation(Duration.millis(100), //
				sprite("player/MovingUp_0.png", spriteSize), //
				sprite("player/MovingUp_1.png", spriteSize), //
				sprite("player/MovingUp_2.png", spriteSize), //
				sprite("player/MovingUp_3.png", spriteSize));

		animationStandingStill = new SpriteAnimation(Duration.millis(500), //
				sprite("player/StandingStill_0.png", spriteSize), //
				sprite("player/StandingStill_1.png", spriteSize), //
				sprite("player/StandingStill_2.png", spriteSize), //
				sprite("player/StandingStill_3.png", spriteSize));
	}

	private static Image sprite(String path, double spriteSize) {
		return ResourceLoader.image(path, spriteSize, spriteSize, false, false);
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

	public void render(GraphicsContext g) {
		var animation = selectAnimation();
		var sprite = animation.currentSprite();
		// Nur zur Verdeutlichung (ohne Point2D Instanzen zu erzeugen, wäre es natürlich effizienter).
		var spriteSize = new Point2D(sprite.getWidth(), sprite.getHeight());
		var spritePosition = player.center().subtract(spriteSize.multiply(0.5));
		g.drawImage(sprite, spritePosition.getX(), spritePosition.getY());
		if (debug) {
			drawPlayerInfo(g, animation);
		}
	}

	// Bei diagonalen Richtungen man sich halt entscheiden, welche Animation man nimmt.
	private SpriteAnimation selectAnimation() {
		if (player.getMoveDir().equals(MoveDirection.N)) {
			return animationMovingUp;
		}
		if (player.getMoveDir().equals(MoveDirection.W) || player.getMoveDir().equals(MoveDirection.NW)
				|| player.getMoveDir().equals(MoveDirection.SW)) {
			return animationMovingLeft;
		}
		if (player.getMoveDir().equals(MoveDirection.S)) {
			return animationMovingDown;
		}
		if (player.getMoveDir().equals(MoveDirection.E) || player.getMoveDir().equals(MoveDirection.NE)
				|| player.getMoveDir().equals(MoveDirection.SE)) {
			return animationMovingRight;
		}
		return animationStandingStill;
	}

	private void drawPlayerInfo(GraphicsContext gc, SpriteAnimation animation) {
		var ts = tileSizeProperty.get();

		String infoText = String.format("Ghost: center=(%.2f, %.2f)%nmoveDir=%s", player.center().getX(),
				player.center().getY(), player.getMoveDir());
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
		gc.fillText(infoText, player.center().getX() + 2 * ts, player.center().getY() - ts);
	}
}
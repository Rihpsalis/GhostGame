package ghostgame;

import java.util.EnumMap;
import java.util.Map;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
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

	public final BooleanProperty debugProperty = new SimpleBooleanProperty(false);

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
	private final Map<MoveDirection, SpriteAnimation> moveAnimations = new EnumMap<>(MoveDirection.class);
//	private SpriteAnimation animationMovingRight;
//	private SpriteAnimation animationMovingLeft;
//	private SpriteAnimation animationMovingUp;
//	private SpriteAnimation animationMovingDown;
//	private SpriteAnimation animationStandingStill;

	public PlayerView(Player player, double spriteSizeInTiles) {
		this.player = player;
		this.spriteSizeInTiles = spriteSizeInTiles;
		createAnimations(spriteSizeInTiles * tileSizeProperty.get());
	}

	private void createAnimations(double spriteSize) {
		moveAnimations.put(MoveDirection.E, new SpriteAnimation(Duration.millis(100), //
				sprite("player/MovingRight_0.png", spriteSize), //
				sprite("player/MovingRight_1.png", spriteSize), //
				sprite("player/MovingRight_2.png", spriteSize), //
				sprite("player/MovingRight_3.png", spriteSize)));

		moveAnimations.put(MoveDirection.W, new SpriteAnimation(Duration.millis(100), //
				sprite("player/MovingLeft_0.png", spriteSize), //
				sprite("player/MovingLeft_1.png", spriteSize), //
				sprite("player/MovingLeft_2.png", spriteSize), //
				sprite("player/MovingLeft_3.png", spriteSize)));

		moveAnimations.put(MoveDirection.S, new SpriteAnimation(Duration.millis(100), //
				sprite("player/MovingDown_0.png", spriteSize), //
				sprite("player/MovingDown_1.png", spriteSize), //
				sprite("player/MovingDown_2.png", spriteSize), //
				sprite("player/MovingDown_3.png", spriteSize)));

		moveAnimations.put(MoveDirection.N, new SpriteAnimation(Duration.millis(100), //
				sprite("player/MovingUp_0.png", spriteSize), //
				sprite("player/MovingUp_1.png", spriteSize), //
				sprite("player/MovingUp_2.png", spriteSize), //
				sprite("player/MovingUp_3.png", spriteSize)));

		moveAnimations.put(MoveDirection.NONE, new SpriteAnimation(Duration.millis(500), //
				sprite("player/StandingStill_0.png", spriteSize), //
				sprite("player/StandingStill_1.png", spriteSize), //
				sprite("player/StandingStill_2.png", spriteSize), //
				sprite("player/StandingStill_3.png", spriteSize)));
	}

	private static Image sprite(String path, double spriteSize) {
		return ResourceLoader.image(path, spriteSize, spriteSize, false, false);
	}

	public void startAnimations() {
		moveAnimations.values().forEach(SpriteAnimation::start);
	}

	public void stopAnimations() {
		moveAnimations.values().forEach(SpriteAnimation::stop);
	}

	public void render(GraphicsContext g) {
		var animation = selectAnimation();
		var sprite = animation.currentSprite();
		// Nur zur Verdeutlichung (ohne Point2D Instanzen zu erzeugen, wäre es natürlich effizienter).
		var spriteSize = new Point2D(sprite.getWidth(), sprite.getHeight());
		var spritePosition = player.center().subtract(spriteSize.multiply(0.5));
		g.drawImage(sprite, spritePosition.getX(), spritePosition.getY());
		if (debugProperty.get()) {
			drawPlayerInfo(g, animation);
		}
	}

	// Bei diagonalen Richtungen man sich halt entscheiden, welche Animation man nimmt.
	private SpriteAnimation selectAnimation() {
		switch (player.getMoveDir()) {
		case NW:
		case SW:
			return moveAnimations.get(MoveDirection.W);
		case NE:
		case SE:
			return moveAnimations.get(MoveDirection.E);
		default:
			return moveAnimations.get(player.getMoveDir());
		}
	}

	private void drawPlayerInfo(GraphicsContext gc, SpriteAnimation animation) {
		var ts = tileSizeProperty.get();
		var tilesPerSecond = player.getSpeed() * 60; // assume 60Hz
		String infoText = String.format("center=(%.2f, %.2f)%nmoveDir=%s%nspeed=%.2f (%.2f tiles(sec @ 60Hz)",
				player.center().getX(), player.center().getY(), player.getMoveDir(), player.getSpeed(), tilesPerSecond);
		String animationName = "???";
		if (animation == moveAnimations.get(MoveDirection.S)) {
			animationName = "Moving Down";
		} else if (animation == moveAnimations.get(MoveDirection.N)) {
			animationName = "Moving Up";
		} else if (animation == moveAnimations.get(MoveDirection.W)) {
			animationName = "Moving Left";
		} else if (animation == moveAnimations.get(MoveDirection.E)) {
			animationName = "Moving Right";
		} else if (animation == moveAnimations.get(MoveDirection.NONE)) {
			animationName = "Standing Still";
		}
		var animationText = String.format("Animation: %s (%s, frame %d)", animationName, animation.getDuration(),
				animation.currentFrame());

		infoText += "\n" + animationText;
		gc.setFill(Color.BLUE);
		gc.setFont(Font.font("Sans", FontWeight.BLACK, 16));
		gc.fillText(infoText, player.center().getX() + 2 * ts, player.center().getY() - 2 * ts);
	}
}
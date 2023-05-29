package ghostgame;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Visual representation of player.
 */
public class PlayerView {

	public final BooleanProperty debugProperty = new SimpleBooleanProperty(false);

	public final IntegerProperty tileSizeProperty = new SimpleIntegerProperty(8) {
		@Override
		protected void invalidated() {
			moveAnimation.stop();
			moveAnimation.setSpriteSize(spriteSizeInTiles * get());
			moveAnimation.start();
			standingAnimation.stop();
			standingAnimation.setSpriteSize(spriteSizeInTiles * get());
			standingAnimation.start();
		}
	};

	private final Player player;
	private final PlayerMoveAnimation moveAnimation;
	private final PlayerStandingAnimation standingAnimation;
	private double spriteSizeInTiles = 3.0;

	public PlayerView(Player player) {
		this.player = player;
		moveAnimation = new PlayerMoveAnimation(player);
		standingAnimation = new PlayerStandingAnimation();
	}

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
			moveAnimation.setSpriteSize(numTiles * tileSizeProperty.get());
			standingAnimation.setSpriteSize(numTiles * tileSizeProperty.get());
		}
	}

	public void render(GraphicsContext g) {
		var currentAnimation = player.getMoveDir() == MoveDirection.NONE ? standingAnimation : moveAnimation;
		var sprite = currentAnimation.currentSprite();

		// Draw sprite centered over player center position
		var spritePos = player.center().subtract(sprite.getWidth() / 2, sprite.getHeight() / 2);
		g.drawImage(sprite, spritePos.getX(), spritePos.getY());

		if (debugProperty.get()) {
			var ts = tileSizeProperty.get();
			var tilesPerSecond = player.getSpeed() * 60; // assume 60Hz
			var animationText = String.format("%s: (%.2f ms, frame %d)", currentAnimation.name(),
					currentAnimation.frameDuration().toMillis(), currentAnimation.currentFrame());
			var infoText = String.format("center=(%.2f, %.2f)%nmoveDir=%s%nspeed=%.2f (%.2f tiles(sec @ 60Hz)%n%s",
					player.center().getX(), player.center().getY(), player.getMoveDir(), player.getSpeed(), tilesPerSecond,
					animationText);
			g.setFill(Color.ORANGE);
			g.setFont(Font.font("Sans", FontWeight.BLACK, 16));
			g.fillText(infoText, player.center().getX() + ts, player.center().getY() - ts);
		}
	}
}
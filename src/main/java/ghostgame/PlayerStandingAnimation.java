package ghostgame;

import java.util.List;

import javafx.scene.image.Image;
import javafx.util.Duration;

/**
 * @author Armin Reichert
 */
public class PlayerStandingAnimation extends SpriteAnimation {

	private List<Image> sprites = List.of();

	public PlayerStandingAnimation(Duration frameDuration) {
		super(frameDuration);
	}

	public void setSpriteSize(double spriteSize) {
		boolean restart = getStatus() == Status.RUNNING;
		stop();
		sprites = spriteList("player/StandingStill_%d.png", 4, spriteSize);
		App.log("Sprites for animation '%s' created, sprite size: %.2f", name(), spriteSize);
		if (restart) {
			restart();
		}
	}

	@Override
	public int numFrames() {
		return sprites.size();
	}

	@Override
	public Image currentSprite() {
		return sprites.get(frame);
	}
}
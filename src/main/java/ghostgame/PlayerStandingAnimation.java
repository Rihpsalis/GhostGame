package ghostgame;

import java.util.List;

import javafx.animation.Animation.Status;
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
		boolean restart = transition.getStatus() == Status.RUNNING;
		stop();
		sprites = spriteList("player/StandingStill_%d.png", 4, spriteSize);
		App.log("Sprite animation '%s' created, sprite size: %.2f", name(), spriteSize);
		if (restart) {
			transition.playFromStart();
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
package ghostgame;

import static ghostgame.ResourceLoader.sprite;

import java.util.List;

import javafx.scene.image.Image;
import javafx.util.Duration;

public class PlayerStandingAnimation extends SpriteAnimation {

	private static final Duration FRAME_DURATION = Duration.millis(500);

	private List<Image> sprites = List.of();

	public PlayerStandingAnimation() {
	}

	public void setSpriteSize(double spriteSize) {
		sprites = List.of( //
				sprite("player/StandingStill_0.png", spriteSize), //
				sprite("player/StandingStill_1.png", spriteSize), //
				sprite("player/StandingStill_2.png", spriteSize), //
				sprite("player/StandingStill_3.png", spriteSize));

		createTransition(FRAME_DURATION);
		App.log("Animation '%s' created", name());
	}

	@Override
	public int numFrames() {
		return sprites.size();
	}

	@Override
	public Duration frameDuration() {
		return FRAME_DURATION;
	}

	@Override
	public Image currentSprite() {
		return sprites.get(frame);
	}
}
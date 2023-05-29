package ghostgame;

import static ghostgame.ResourceLoader.sprite;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.scene.image.Image;
import javafx.util.Duration;

/**
 * @author Armin Reichert
 */
public class PlayerMoveAnimation extends SpriteAnimation {

	static final Duration FRAME_DURATION = Duration.millis(100);

	private final Player player;
	private double spriteSize = 8;
	private final Map<MoveDirection, List<Image>> spritesByDir = new EnumMap<>(MoveDirection.class);

	public PlayerMoveAnimation(Player player) {
		this.player = player;
	}

	public void setSpriteSize(double spriteSize) {
		if (this.spriteSize == spriteSize) {
			return;
		}
		spritesByDir.clear();
		spritesByDir.put(MoveDirection.E, List.of( //
				sprite("player/MovingRight_0.png", spriteSize), //
				sprite("player/MovingRight_1.png", spriteSize), //
				sprite("player/MovingRight_2.png", spriteSize), //
				sprite("player/MovingRight_3.png", spriteSize)));

		spritesByDir.put(MoveDirection.W, List.of( //
				sprite("player/MovingLeft_0.png", spriteSize), //
				sprite("player/MovingLeft_1.png", spriteSize), //
				sprite("player/MovingLeft_2.png", spriteSize), //
				sprite("player/MovingLeft_3.png", spriteSize)));

		spritesByDir.put(MoveDirection.S, List.of( //
				sprite("player/MovingDown_0.png", spriteSize), //
				sprite("player/MovingDown_1.png", spriteSize), //
				sprite("player/MovingDown_2.png", spriteSize), //
				sprite("player/MovingDown_3.png", spriteSize)));

		spritesByDir.put(MoveDirection.N, List.of( //
				sprite("player/MovingUp_0.png", spriteSize), //
				sprite("player/MovingUp_1.png", spriteSize), //
				sprite("player/MovingUp_2.png", spriteSize), //
				sprite("player/MovingUp_3.png", spriteSize)));

		spritesByDir.put(MoveDirection.NONE, List.of( //
				sprite("player/StandingStill_0.png", spriteSize), //
				sprite("player/StandingStill_1.png", spriteSize), //
				sprite("player/StandingStill_2.png", spriteSize), //
				sprite("player/StandingStill_3.png", spriteSize)));

		App.log("Player move sprites created, sprite size: %.2f", spriteSize);

		transition = new Transition() {
			{
				setCycleCount(Animation.INDEFINITE);
				setCycleDuration(Duration.millis(100));
			}

			@Override
			protected void interpolate(double t) {
				if (t == 1) {
					frame += 1;
					if (frame == sprites().size()) {
						frame = 0;
					}
				}
			}
		};
		App.log("Animation '%s' created", name());

	}

	private List<Image> sprites() {
		return spritesByDir.get(currentAnimationDirection());
	}

	private MoveDirection currentAnimationDirection() {
		switch (player.getMoveDir()) {
		case NW:
		case SW:
			return MoveDirection.W;
		case NE:
		case SE:
			return MoveDirection.E;
		default:
			return player.getMoveDir();
		}
	}

	@Override
	public Duration frameDuration() {
		return FRAME_DURATION;
	}

	@Override
	public int numFrames() {
		return sprites().size();
	}

	@Override
	public Image currentSprite() {
		return sprites().get(frame);
	}
}
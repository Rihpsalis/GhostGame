package ghostgame;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javafx.scene.image.Image;
import javafx.util.Duration;

/**
 * @author Armin Reichert
 */
public class PlayerMoveAnimation extends SpriteAnimation {

	private final Player player;
	private final Map<MoveDirection, List<Image>> spritesByDir = new EnumMap<>(MoveDirection.class);

	public PlayerMoveAnimation(Player player, Duration frameDuration) {
		super(frameDuration);
		this.player = player;
	}

	public void setSpriteSize(double spriteSize) {
		spritesByDir.clear();
		spritesByDir.put(MoveDirection.E, spriteList("player/MovingRight_%d.png", 4, spriteSize));
		spritesByDir.put(MoveDirection.W, spriteList("player/MovingLeft_%d.png", 4, spriteSize));
		spritesByDir.put(MoveDirection.S, spriteList("player/MovingDown_%d.png", 4, spriteSize));
		spritesByDir.put(MoveDirection.N, spriteList("player/MovingUp_%d.png", 4, spriteSize));
		spritesByDir.put(MoveDirection.NONE, spriteList("player/StandingStill_%d.png", 4, spriteSize));
		App.log("Sprite animation '%s' created, sprite size: %.2f", name(), spriteSize);
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
	public int numFrames() {
		return sprites().size();
	}

	@Override
	public Image currentSprite() {
		return sprites().get(frame);
	}
}
package ghostgame;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.scene.image.Image;
import javafx.util.Duration;

/**
 * @author Armin Reichert
 */
public abstract class SpriteAnimation extends Transition {

	protected static List<Image> spriteList(String pattern, int n, double spriteSize) {
		var result = new ArrayList<Image>(n);
		for (int i = 0; i < n; ++i) {
			result.add(ResourceLoader.sprite(String.format(pattern, i), spriteSize));
		}
		return result;
	}

	protected int frame;
	protected String name;

	protected SpriteAnimation(Duration duration) {
		name = getClass().getSimpleName();
		setCycleCount(Animation.INDEFINITE);
		setCycleDuration(duration);
		setInterpolator(Interpolator.LINEAR);
	}

	protected SpriteAnimation(String name, Duration duration) {
		this.name = name;
		setCycleCount(Animation.INDEFINITE);
		setCycleDuration(duration);
		setInterpolator(Interpolator.LINEAR);
	}

	public String name() {
		return name;
	}

	@Override
	protected void interpolate(double t) {
		if (t == 1) {
			frame += 1;
			if (frame == numFrames()) {
				frame = 0;
			}
		}
	}

	public void restart() {
		frame = 0;
		super.playFromStart();
		App.log("Animation '%s' restarted", name());
	}

	public void start() {
		super.play();
		App.log("Animation '%s' started", name());
	}

	@Override
	public void stop() {
		super.stop();
		App.log("Animation '%s' stopped", name());
	}

	public int currentFrame() {
		return frame;
	}

	public Duration frameDuration() {
		return super.getCycleDuration();
	}

	public abstract int numFrames();

	public abstract Image currentSprite();
}
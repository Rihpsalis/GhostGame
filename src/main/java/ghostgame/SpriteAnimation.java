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
public abstract class SpriteAnimation {

	protected static List<Image> spriteList(String pattern, int n, double spriteSize) {
		var result = new ArrayList<Image>(n);
		for (int i = 0; i < n; ++i) {
			result.add(ResourceLoader.sprite(String.format(pattern, i), spriteSize));
		}
		return result;
	}

	protected final Transition transition;
	protected int frame;

	protected SpriteAnimation(Duration duration) {
		transition = new Transition() {
			{
				setCycleCount(Animation.INDEFINITE);
				setCycleDuration(duration);
				setInterpolator(Interpolator.LINEAR);
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
		};
	}

	public void restart() {
		frame = 0;
		transition.playFromStart();
		App.log("Animation '%s' restarted", name());
	}

	public void start() {
		transition.play();
		App.log("Animation '%s' started", name());
	}

	public void stop() {
		transition.stop();
		App.log("Animation '%s' stopped", name());
	}

	public int currentFrame() {
		return frame;
	}

	public Duration frameDuration() {
		return transition.getCycleDuration();
	}

	public abstract int numFrames();

	public abstract Image currentSprite();

	public String name() {
		return getClass().getSimpleName();
	}
}
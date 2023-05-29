package ghostgame;

import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.scene.image.Image;
import javafx.util.Duration;

public abstract class SpriteAnimation {

	protected Transition transition;
	protected int frame;

	protected SpriteAnimation() {
	}

	protected void createTransition(Duration duration) {
		frame = 0;
		transition = new Transition() {
			{
				setCycleCount(Animation.INDEFINITE);
				setCycleDuration(duration);
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

	public void start() {
		if (transition != null) {
			transition.play();
		}
	}

	public void stop() {
		if (transition != null) {
			transition.stop();
		}
	}

	public int currentFrame() {
		return frame;
	}

	public abstract Duration frameDuration();

	public abstract int numFrames();

	public abstract Image currentSprite();

	public String name() {
		return getClass().getSimpleName();
	}
}
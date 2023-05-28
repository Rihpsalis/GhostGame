package ghostgame;

import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.scene.image.Image;
import javafx.util.Duration;

/**
 * Das ist nur eine der Möglichkeiten, eine Spriteanimation zu implementieren: für jeden "Frame" eine eigene Bilddatei.
 * <p>
 * Effizienter wäre es wohl, ein Spritesheet zu erstellen, das alle Sprites enthält, und sich in der
 * SpriteAnimation-Klasse den zum aktuellen Frame gehörenden Bildausschnitt zu merken. In JavaFX kann man (im Gegensatz
 * zu Swing) einen beliebigen Ausschnitt aus einem Bild zeichnen bzw. darauf ein ImageView positionieren.
 * 
 * @author Armin Reichert
 */
public class SpriteAnimation {

	private final Transition animation;
	private final Image[] sprites;
	private int frame;

	/**
	 * @param duration duration of one frame
	 * @param sprites  images of the animation
	 */
	public SpriteAnimation(Duration duration, Image... sprites) {
		this.sprites = sprites;
		frame = 0;
		animation = new Transition() {
			{
				setCycleCount(Animation.INDEFINITE);
				setCycleDuration(duration);
			}

			@Override
			protected void interpolate(double t) {
				if (t == 1) {
					frame += 1;
					if (frame == sprites.length) {
						frame = 0;
					}
				}
			}
		};
	}

	public void start() {
		animation.play();
	}

	public void stop() {
		animation.stop();
	}

	public Duration getDuration() {
		return animation.getCycleDuration();
	}

	public int currentFrame() {
		return frame;
	}

	public Image currentSprite() {
		return sprites[frame];
	}
}
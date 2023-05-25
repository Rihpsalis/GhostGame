/*
MIT License

Copyright (c) 2023 Armin Reichert

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package ghostgame;

import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.scene.image.Image;
import javafx.util.Duration;

/**
 * Das ist nur eine der Möglichkeiten, eine Spriteanimation zu implementieren: für jeden "Frame" eine eigene Bilddatei.
 * <p>
 * Effizienter wäre es wohl, ein großes Bild (Spritesheet) zu erstellen, die alle Sprites enthält, und sich in der
 * SpriteAnimation-Klasse den zum aktuellen Frame gehörenden Bildausschnitt zu merken. In JavaFX kann man (im Gegensatz
 * zu Swing) einen beliebigen Ausschnitt aus einem Bild zeichnen bzw. darauf ein ImageView positionieren.
 * 
 * @author Armin Reichert
 */
public class SpriteAnimation {

	private final Transition animation;
	private final Image[] sprites;
	private int frame;

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

	public int getFrame() {
		return frame;
	}

	public Image currentSprite() {
		return sprites[frame];
	}
}
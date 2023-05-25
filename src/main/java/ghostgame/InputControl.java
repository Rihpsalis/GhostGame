package ghostgame;

import java.util.BitSet;

import javafx.geometry.Point2D;
import javafx.scene.Scene;

public class InputControl {

	private static final int LEFT = 0;
	private static final int RIGHT = 1;
	private static final int UP = 2;
	private static final int DOWN = 3;

	private final Player player;
	private final BitSet pressed = new BitSet(4);

	public InputControl(Scene scene, Player player) {
		this.player = player;
		scene.setOnKeyPressed(e -> {
			switch (e.getCode()) {
			case LEFT, A -> pressed.set(LEFT);
			case RIGHT, D -> pressed.set(RIGHT);
			case UP, W -> pressed.set(UP);
			case DOWN, S -> pressed.set(DOWN);
			default -> {
				// ignore
			}
			}
		});
		scene.setOnKeyReleased(e -> {
			switch (e.getCode()) {
			case LEFT, A -> pressed.clear(LEFT);
			case RIGHT, D -> pressed.clear(RIGHT);
			case UP, W -> pressed.clear(UP);
			case DOWN, S -> pressed.clear(DOWN);
			default -> {
				// ignore
			}
			}
		});
	}

	public void steerPlayer() {
		player.setMoveDirection(computeMoveDirection());
		player.move();
	}

	// Hier bildet man die Kombinationen der gedrückten Keys auf Richtungen ab
	private Point2D computeMoveDirection() {
		int numKeysPressed = pressed.cardinality();
		if (numKeysPressed > 2) {
			return Player.DIRECTION_NONE;
		}
		if (pressed.get(UP) && pressed.get(LEFT)) {
			return Player.DIRECTION_NW;
		} else if (pressed.get(UP) && pressed.get(RIGHT)) {
			return Player.DIRECTION_NE;
		} else if (pressed.get(DOWN) && pressed.get(LEFT)) {
			return Player.DIRECTION_SW;
		} else if (pressed.get(DOWN) && pressed.get(RIGHT)) {
			return Player.DIRECTION_SE;
		} else if (pressed.get(UP)) {
			return Player.DIRECTION_N;
		} else if (pressed.get(DOWN)) {
			return Player.DIRECTION_S;
		} else if (pressed.get(LEFT)) {
			return Player.DIRECTION_W;
		} else if (pressed.get(RIGHT)) {
			return Player.DIRECTION_E;
		}
		return Player.DIRECTION_NONE;
	}
}
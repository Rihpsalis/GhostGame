package ghostgame;

import java.util.BitSet;

import javafx.geometry.Point2D;
import javafx.scene.Scene;

public class PlayerControl {

	private static final int LEFT = 0;
	private static final int RIGHT = 1;
	private static final int UP = 2;
	private static final int DOWN = 3;

	private final BitSet pressed = new BitSet(4);

	public PlayerControl(Scene scene) {
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

	public void steer(Player player) {
		player.setMoveDirection(computeMoveDirection());
	}

	// Kombination der gedrückten Tasten auf Richtung abbilden
	private Point2D computeMoveDirection() {
		// Mehr als 2 Tasten gleichzeitig gedrückt?
		if (pressed.cardinality() > 2) {
			return Player.DIRECTION_NONE;
		}
		// 2-Tasten-Kombinationen
		if (pressed.get(UP) && pressed.get(LEFT)) {
			return Player.DIRECTION_NW;
		}
		if (pressed.get(UP) && pressed.get(RIGHT)) {
			return Player.DIRECTION_NE;
		}
		if (pressed.get(DOWN) && pressed.get(LEFT)) {
			return Player.DIRECTION_SW;
		}
		if (pressed.get(DOWN) && pressed.get(RIGHT)) {
			return Player.DIRECTION_SE;
		}
		// Einzeltasten
		if (pressed.get(UP)) {
			return Player.DIRECTION_N;
		}
		if (pressed.get(DOWN)) {
			return Player.DIRECTION_S;
		}
		if (pressed.get(LEFT)) {
			return Player.DIRECTION_W;
		}
		if (pressed.get(RIGHT)) {
			return Player.DIRECTION_E;
		}
		return Player.DIRECTION_NONE;
	}
}
package ghostgame;

import java.util.BitSet;

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
			case LEFT:
			case A:
				pressed.set(LEFT);
				break;
			case RIGHT:
			case D:
				pressed.set(RIGHT);
				break;
			case UP:
			case W:
				pressed.set(UP);
				break;
			case DOWN:
			case S:
				pressed.set(DOWN);
				break;
			default:
				break;
			}
		});
		scene.setOnKeyReleased(e -> {
			switch (e.getCode()) {
			case LEFT:
			case A:
				pressed.clear(LEFT);
				break;
			case RIGHT:
			case D:
				pressed.clear(RIGHT);
				break;
			case UP:
			case W:
				pressed.clear(UP);
				break;
			case DOWN:
			case S:
				pressed.clear(DOWN);
				break;
			default:
				break;
			}
		});
	}

	public void steer(Player player) {
		player.setMoveDir(computeMoveDirection());
	}

	// Kombination der gedrückten Tasten auf Richtung abbilden
	private MoveDirection computeMoveDirection() {
		// Mehr als 2 Tasten gleichzeitig gedrückt?
		if (pressed.cardinality() > 2) {
			return MoveDirection.NONE;
		}
		// 2-Tasten-Kombinationen
		if (pressed.get(UP) && pressed.get(LEFT)) {
			return MoveDirection.NW;
		}
		if (pressed.get(UP) && pressed.get(RIGHT)) {
			return MoveDirection.NE;
		}
		if (pressed.get(DOWN) && pressed.get(LEFT)) {
			return MoveDirection.SW;
		}
		if (pressed.get(DOWN) && pressed.get(RIGHT)) {
			return MoveDirection.SE;
		}
		// Einzeltasten
		if (pressed.get(UP)) {
			return MoveDirection.N;
		}
		if (pressed.get(DOWN)) {
			return MoveDirection.S;
		}
		if (pressed.get(LEFT)) {
			return MoveDirection.W;
		}
		if (pressed.get(RIGHT)) {
			return MoveDirection.E;
		}
		return MoveDirection.NONE;
	}
}
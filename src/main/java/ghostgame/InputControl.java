package ghostgame;

import javafx.scene.Scene;

public class InputControl {
	private Scene scene;
	private boolean[] directions; // up, left, down, right
	private Player player;

	public InputControl(Scene scene, Player player, boolean[] directions) {
		this.scene = scene;
		this.directions = directions;
		this.player = player;

		scene.setOnKeyPressed(keyEvent -> {
			switch (keyEvent.getCode()) {
			case UP, W -> directions[0] = true; // up
			case LEFT, A -> directions[1] = true; // left
			case DOWN, S -> directions[2] = true; // down
			case RIGHT, D -> directions[3] = true; // right
			default -> {
			}
			}
		});

		scene.setOnKeyReleased(keyEvent -> {
			switch (keyEvent.getCode()) {
			case UP, W -> directions[0] = false; // up
			case LEFT, A -> directions[1] = false; // left
			case DOWN, S -> directions[2] = false; // down
			case RIGHT, D -> directions[3] = false; // right
			default -> {
			}
			}
		});
	}

	// setPlayerCords needed because with this Method Player can move in 2 Directions at the same time.
	public void setPlayerCords() {
		if (directions[2])
			player.sMove();

		if (directions[0])
			player.wMove();

		if (directions[1])
			player.aMove();

		if (directions[3])
			player.dMove();

	}
}

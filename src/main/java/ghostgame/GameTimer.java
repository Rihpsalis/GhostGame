package ghostgame;

import javafx.animation.AnimationTimer;

public class GameTimer extends AnimationTimer {
	private InputControl inputControl;
	private Panel panel;
	private Gridmap gridmap;

	public GameTimer(InputControl inputControl, Panel panel, Gridmap gridmap) {
		this.inputControl = inputControl;
		this.panel = panel;
		this.gridmap = gridmap;
	}

	@Override
	public void handle(long l) {
		inputControl.steerPlayer();
		gridmap.update();
		panel.render();
	}
}

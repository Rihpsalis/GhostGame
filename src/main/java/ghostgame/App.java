package ghostgame;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class App extends Application {
	// Was genau ist das?
	private static final int SIZE = 200;

	// Als Programmparameter übergeben?
	private String gridmapInUse = "Gridmap";

	private Player player;
	private AnimationTimer gameClock;

	@Override
	public void start(Stage stage) {
		// Noch mal überdenken:
		var screenBounds = Screen.getPrimary().getBounds();
		var screenHeight = screenBounds.getHeight();
		var screenWidth = screenBounds.getWidth();

		var gridmap = new Gridmap(SIZE, screenHeight, screenWidth, gridmapInUse);
		player = new Player(SIZE, screenHeight, screenWidth);
		player.setSpeed(8.0);

		var rootPane = new BorderPane();
		var scene = new Scene(rootPane, 800, 600, Color.DARKGRAY);
		var canvas = new Canvas(screenWidth, screenHeight);
		var gc = canvas.getGraphicsContext2D();

		rootPane.setCenter(canvas);

		stage.setTitle("Ghost Game");
		stage.setScene(scene);
		stage.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
			if (e.getCode() == KeyCode.F11) {
				stage.setFullScreen(true);
			}
		});
		stage.show();

		var playerControl = new PlayerControl(scene);
		gameClock = new AnimationTimer() {
			@Override
			public void handle(long now) {
				playerControl.steerPlayer(player);
				gridmap.update(player);
				gridmap.render(gc);
				var playerScreenX = (int) (scene.getWidth() - player.getSize()) / 2;
				var playerScreenY = (int) (scene.getHeight() - player.getSize()) / 2;
				player.render(gc, playerScreenX, playerScreenY);
			}
		};

		player.startAnimations();
		gameClock.start();
	}

	@Override
	public void stop() throws Exception {
		player.stopAnimations();
		gameClock.stop();
		System.out.println("Bye.");
	}
}
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
	private static final String MAP_NAME = "GridMap";
	private static final String MAP_IMAGE_PATH = "terrain/gridmap/%s.png".formatted(MAP_NAME);
	private static final String MAP_CONTENT_PATH = "terrain/gridmap/%s_values.txt".formatted(MAP_NAME);

	private Player player;
	private AnimationTimer gameClock;

	@Override
	public void start(Stage stage) {
		// Noch mal überdenken:
		var screenBounds = Screen.getPrimary().getBounds();
		var screenHeight = screenBounds.getHeight();
		var screenWidth = screenBounds.getWidth();

		var gridmap = new Gridmap(SIZE, MAP_CONTENT_PATH, MAP_IMAGE_PATH);
		player = new Player(SIZE, screenHeight, screenWidth);
		player.setSpeed(8.0);

		var rootPane = new BorderPane();
		var scene = new Scene(rootPane, 800, 600, Color.DARKGRAY);

		var canvas = new Canvas(screenWidth, screenHeight);
		var gc = canvas.getGraphicsContext2D();

		// Soll der Canvas sich an die Größe der Scene anpassen?
		canvas.widthProperty().bind(scene.widthProperty());
		canvas.heightProperty().bind(scene.heightProperty());

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
				// Update
				playerControl.steer(player);
				player.move();

				// Canvas löschen
				gc.setFill(scene.getFill());
				gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

				// Das soll wohl eine Kamera simulieren, die den Player im Zentrum der Szene focussiert?
				gc.save();
				gc.translate(-player.getX(), -player.getY());
				gridmap.render(gc);
				gc.restore();
				var centerX = (int) (scene.getWidth() - player.getSize()) / 2;
				var centerY = (int) (scene.getHeight() - player.getSize()) / 2;
				player.render(gc, centerX, centerY);
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
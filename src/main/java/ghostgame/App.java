package ghostgame;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class App extends Application {

	private static final int TILESIZE = 32;

	// Als Programmparameter übergeben?
	private static final String MAP_NAME = "GridMap";

	private Player player;
	private AnimationTimer gameClock;

	@Override
	public void start(Stage stage) {
		var gridmap = new Gridmap(TILESIZE, "terrain/gridmap/%s_values.txt".formatted(MAP_NAME));
		player = new Player(4 * TILESIZE);
		player.setX(30);
		player.setY(20);
		player.setSpeed(0.2);

		var rootPane = new BorderPane();
		var scene = new Scene(rootPane, 1280, 800, Color.gray(0.2));

		var canvas = new Canvas(scene.getWidth(), scene.getHeight());
		canvas.widthProperty().bind(scene.widthProperty());
		canvas.heightProperty().bind(scene.heightProperty());
		var gc = canvas.getGraphicsContext2D();

		rootPane.setCenter(canvas);

		stage.setTitle("Ghost Game");
		stage.setScene(scene);
		stage.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
			switch (e.getCode()) {
			case F11 -> stage.setFullScreen(true);
			case DIGIT0 -> { // Links oben
				player.setX(0);
				player.setY(0);
			}
			case C -> { // Mitte
				player.setX(gridmap.getNumCols() * 0.5);
				player.setY(gridmap.getNumRows() * 0.5);
			}
			case Z -> { // Rechts unten
				player.setX(gridmap.getNumCols());
				player.setY(gridmap.getNumRows());
			}
			default -> {
				// ignore
			}
			}
		});

		var playerControl = new PlayerControl(scene);
		gameClock = new AnimationTimer() {
			@Override
			public void handle(long now) {
				playerControl.steer(player);
				player.move();

				gc.setFill(scene.getFill());
				gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

				// Simulate a camera that keeps the ghost centered on the screen
				double sw = scene.getWidth();
				double sh = scene.getHeight();
				double px = player.getX() * TILESIZE;
				double py = player.getY() * TILESIZE;

				gc.save();
				gc.translate((-px + 0.5 * sw), (-py + 0.5 * sh));
				gridmap.render(gc);
				gc.restore();

				gc.save();
				gc.translate(0.5 * sw - player.getX(), 0.5 * sh - player.getY());
				player.render(gc);
				gc.restore();
			}
		};

		stage.show();
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
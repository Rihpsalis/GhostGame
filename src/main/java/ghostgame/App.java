package ghostgame;

import java.io.IOException;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class App extends Application {

	// Was genau ist das?
	private static final int SIZE = 200;

	private Gridmap gridmap;
	private String gridmapInUse = "Gridmap";
	private Player player;

	private AnimationTimer gameTimer;
	private PlayerControl playerControl;
	private Scene scene;
	private Canvas canvas;
	private GraphicsContext gc;
	private Rectangle2D screenBounds;

	private double screenHeight, screenWidth;

	@Override
	public void init() throws IOException {
		screenBounds = Screen.getPrimary().getBounds();
		screenHeight = screenBounds.getHeight();
		screenWidth = screenBounds.getWidth();
	}

	@Override
	public void start(Stage stage) {
		var rootPane = new BorderPane();
		scene = new Scene(rootPane, 800, 600, Color.DARKGRAY);
		player = new Player(SIZE, screenHeight, screenWidth);
		player.setSpeed(8.0);
		playerControl = new PlayerControl(scene, player);
		gridmap = new Gridmap(SIZE, player, screenHeight, screenWidth, gridmapInUse);
		canvas = new Canvas(screenWidth, screenHeight);
		gc = canvas.getGraphicsContext2D();

		rootPane.setCenter(canvas);

		gameTimer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				playerControl.steerPlayer();
				gridmap.update();
				gridmap.render(gc);
				var playerScreenX = (int) (scene.getWidth() - player.getSize()) / 2;
				var playerScreenY = (int) (scene.getHeight() - player.getSize()) / 2;
				player.render(gc, playerScreenX, playerScreenY);
			}
		};

		stage.setTitle("Ghost Game");
		stage.setScene(scene);
		stage.setFullScreen(true);
		stage.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
			if (e.getCode() == KeyCode.F11) {
				stage.setFullScreen(true);
			}
		});
		stage.setOnCloseRequest(e -> System.exit(0));
		stage.show();

		player.startAnimations();
		gameTimer.start();
	}
}
package ghostgame;

import java.io.IOException;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class App extends Application {

	// ???
	private static final int SIZE = 200;

	// World/Levels
	private Gridmap gridmap;
	private String gridmapInUse = "Gridmap";
	private Player player;

	// Timers
	private AnimationTimer gameTimer;
//	private Timer animationTimer;

	private InputControl inputControl;

	// User interface
	private Scene scene;
	private Canvas canvas;
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
		scene = new Scene(rootPane, 800, 600);
		player = new Player(SIZE, screenHeight, screenWidth);
		player.setSpeed(8.0);
		inputControl = new InputControl(scene, player);
		gridmap = new Gridmap(SIZE, player, screenHeight, screenWidth, gridmapInUse);
		canvas = new Canvas(screenWidth, screenHeight);

		rootPane.setCenter(canvas);

//		animationTimer = new Timer(player);

		gameTimer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				inputControl.steerPlayer();
				gridmap.update();
				gridmap.render(canvas.getGraphicsContext2D());
				player.render(canvas.getGraphicsContext2D());
			}
		};

		scene.setFill(Color.DARKGRAY);

		stage.setTitle("Ghost Game");
		stage.setScene(scene);
		stage.setFullScreen(false);
		stage.setOnCloseRequest(e -> System.exit(0));
		stage.show();

		player.startAnimations();
		gameTimer.start();

		System.out.println(screenWidth);
		System.out.println(screenHeight);
	}
}
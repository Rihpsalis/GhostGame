package ghostgame;

import java.io.IOException;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class App extends Application {
	private Panel panel;
	private Timer animationTimer;
	private Player player;
	private InputControl inputControl;
	private BorderPane bp;
	private Scene scene;
	private Rectangle2D screenBounds;
	private double screenHeight, screenWidth;
	private GameTimer gameTimer;
	private boolean[] directions;
	private Gridmap gridmap;

	// Skalierungs Größe
	private final int SIZE = 200;

	private Group group;
	private String gridmapInUse = "Gridmap";

	@Override
	public void init() throws IOException {
		screenBounds = Screen.getPrimary().getBounds();
		screenHeight = screenBounds.getHeight();
		screenWidth = screenBounds.getWidth();
	}

	@Override
	public void start(Stage stage) {

		directions = new boolean[4];
		bp = new BorderPane();
		group = new Group(bp);
		scene = new Scene(group, 800, 600);
		player = new Player(directions, SIZE, screenHeight, screenWidth);
		inputControl = new InputControl(scene, player, directions);
		gridmap = new Gridmap(SIZE, player, screenHeight, screenWidth, gridmapInUse);
		panel = new Panel(player, screenWidth, screenHeight, gridmap);
		animationTimer = new Timer(player);
		gameTimer = new GameTimer(inputControl, panel, gridmap);

		scene.setFill(Color.DARKGRAY);
		bp.setCenter(panel);

		stage.setTitle("Ghost Game");
		stage.setScene(scene);
		stage.setFullScreenExitHint("Drücke Esc-Taste, um Vollbildmodus zu verlassen");
		stage.setFullScreen(true);
		stage.setOnCloseRequest(e -> System.exit(0));
		stage.show();

		animationTimer.start();
		gameTimer.start();

		System.out.println(screenWidth);
		System.out.println(screenHeight);
	}
}

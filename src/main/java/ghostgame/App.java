package ghostgame;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class App extends Application {

	// Als Programmparameter übergeben?
	private static final String MAP_NAME = "Gridmap";

	// Model
	private Gridmap gridmap;
	private Player player;

	private Stage stage;
	private Scene scene;
	private Canvas canvas;
	private GridmapView mapView;
	private PlayerView playerView;

	private AnimationTimer gameClock;

	public static final int TILESIZE = 32;

	@Override
	public void start(Stage stage) {
		this.stage = stage;

		// model
		gridmap = new Gridmap(String.format("terrain/gridmap/%s_values.txt", MAP_NAME));
		player = new Player();
		player.setCenter(10, 10);
		player.setSpeed(0.2);

		// user interface
		var rootPane = new BorderPane();
		var screenSize = Screen.getPrimary().getBounds();
		scene = new Scene(rootPane, 0.8 * screenSize.getWidth(), 0.8 * screenSize.getHeight(), Color.gray(0.2));

		canvas = new Canvas(scene.getWidth(), scene.getHeight());
		canvas.widthProperty().bind(scene.widthProperty());
		canvas.heightProperty().bind(scene.heightProperty());
		rootPane.setCenter(canvas);

		stage.setTitle("Ghost Game (Press F11 for Fullscreen)");
		stage.setScene(scene);
		stage.addEventHandler(KeyEvent.KEY_PRESSED, this::handleKeyPressed);

		playerView = new PlayerView(player, 4 * App.TILESIZE);
		var playerControl = new PlayerControl(scene);
		mapView = new GridmapView(gridmap);

		gameClock = new AnimationTimer() {
			@Override
			public void handle(long now) {
				playerControl.steer(player);
				player.move();
				renderScene();
			}
		};

		stage.show();
		playerView.startAnimations();
		gameClock.start();
	}

	private void renderScene() {
		var g = canvas.getGraphicsContext2D();
		g.setFill(scene.getFill());
		g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

		// Simulate a camera that keeps the ghost centered on the screen

		Point2D sceneCenter = new Point2D(scene.getWidth(), scene.getHeight()).multiply(0.5);
		Point2D playerViewCenter = player.center().multiply(App.TILESIZE);

		g.save();
		Point2D moveGridRelativeToPlayer = sceneCenter.subtract(playerViewCenter);
		g.translate(moveGridRelativeToPlayer.getX(), moveGridRelativeToPlayer.getY());
		mapView.render(g);
		g.restore();

		g.save();
		Point2D makePlayerCentered = sceneCenter.subtract(player.center());
		g.translate(makePlayerCentered.getX(), makePlayerCentered.getY());
		playerView.render(g);
		g.restore();
	}

	@Override
	public void stop() throws Exception {
		playerView.stopAnimations();
		gameClock.stop();
		System.out.println("Bye.");
	}

	private void handleKeyPressed(KeyEvent e) {
		switch (e.getCode()) {
		case F11:
			stage.setFullScreen(true);
			break;
		case DIGIT0: { // Links oben
			player.setCenter(Point2D.ZERO);
			break;
		}
		case C: { // Mitte
			player.setCenter(gridmap.getNumCols() * 0.5, gridmap.getNumRows() * 0.5);
			break;
		}
		case Z: { // Rechts unten
			player.setCenter(gridmap.getNumCols(), gridmap.getNumRows());
			break;
		}
		default:
			break;
		}
	}
}
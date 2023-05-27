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

	private static final int TILESIZE = 32;

	// Als Programmparameter übergeben?
	private static final String MAP_NAME = "Gridmap";

	// Model
	private Gridmap gridmap;
	private Player player;

	private Stage stage;
	private Scene scene;
	private Canvas canvas;
	private GridmapView mapView;

	private AnimationTimer gameClock;

	@Override
	public void start(Stage stage) {
		this.stage = stage;

		// model
		gridmap = new Gridmap(TILESIZE, String.format("terrain/gridmap/%s_values.txt", MAP_NAME));
		player = new Player(4 * TILESIZE);
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
		player.startAnimations();
		gameClock.start();
	}

	private void renderScene() {
		var g = canvas.getGraphicsContext2D();
		g.setFill(scene.getFill());
		g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

		// Simulate a camera that keeps the ghost centered on the screen
		double centerX = 0.5 * scene.getWidth();
		double centerY = 0.5 * scene.getHeight();

		g.save();
		g.translate(centerX - player.center().getX() * TILESIZE, centerY - player.center().getY() * TILESIZE);
		mapView.render(g);
		g.restore();

		g.save();
		g.translate(centerX - player.center().getX(), centerY - player.center().getY());
		player.render(g);
		g.restore();
	}

	@Override
	public void stop() throws Exception {
		player.stopAnimations();
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
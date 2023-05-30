package ghostgame;

import java.util.MissingResourceException;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class App extends Application {

	public static final BooleanProperty DEBUG_PROPERTY = new SimpleBooleanProperty(false);

	// TODO use real logger
	public static void log(String message, Object... args) {
		System.out.println(String.format(message, args));
	}

	private final IntegerProperty tileSizeProperty = new SimpleIntegerProperty();

	private String mapFileName = "Gridmap_values.txt";
	private Gridmap map;
	private Player player;
	private PlayerControl playerControl;
	private Stage stage;
	private Scene scene;
	private Canvas canvas;
	private GridmapView mapView;
	private InfoPanel infoPanel;

	private AnimationTimer gameClock;

	@Override
	public void init() throws Exception {
		var params = getParameters().getNamed();
		if (params.containsKey("map")) {
			mapFileName = params.get("map");
		}
		// create/load the map
		map = new Gridmap();
		var path = "terrain/gridmap/" + mapFileName;
		try {
			map.readContent(ResourceLoader.url(path));
			log(map.content());
		} catch (MissingResourceException x) {
			log("No map found at resource path '%s'. Exit application.", path);
			Platform.exit();
		}
	}

	@Override
	public void start(Stage stage) {
		this.stage = stage;

		var screenSize = Screen.getPrimary().getBounds();

		var rootPane = new StackPane();
		scene = new Scene(rootPane, 0.8 * screenSize.getWidth(), 0.8 * screenSize.getHeight(), Color.gray(0.2));

		canvas = new Canvas(400, 300);
		canvas.widthProperty().bind(scene.widthProperty());
		canvas.heightProperty().bind(scene.heightProperty());

		stage.setTitle("Ghost Game (F11=fullscreen, +/- tile size, i=info)");
		stage.setScene(scene);
		stage.addEventHandler(KeyEvent.KEY_PRESSED, this::handleKeyPressed);

		player = new Player();
		player.setCenter(map.getNumCols() * 0.5, map.getNumRows() * 0.5);
		player.setSpeed(0.4);
		player.debugProperty.bind(DEBUG_PROPERTY);

		playerControl = new PlayerControl(scene);

		mapView = new GridmapView(map);
		mapView.debugProperty.bind(DEBUG_PROPERTY);

		infoPanel = new InfoPanel();
		StackPane.setAlignment(infoPanel, Pos.TOP_LEFT);
		StackPane.setMargin(infoPanel, new Insets(20));

		rootPane.getChildren().add(new BorderPane(canvas));
		rootPane.getChildren().add(infoPanel);

		// Note that it is *not* guaranteed that this timer "ticks" with 60Hz!
		// See https://edencoding.com/javafxanimation-transitions-timelines-and-animation-timers/
		// See https://stackoverflow.com/questions/50337303/how-do-i-change-the-speed-of-an-animationtimer-in-javafx
		// So either you have to pass the "delta time" to the update() methods of the moving entities or use
		// a timer with guaranteed frame rate, e.g. a Timeline
		gameClock = new AnimationTimer() {
			@Override
			public void handle(long now) {
				update();
				renderScene();
			}
		};

		tileSizeProperty.set(32);
		mapView.tileSizeProperty.bind(tileSizeProperty);
		player.tileSizeProperty.bind(tileSizeProperty);

		stage.show();
		player.startAnimations();
		gameClock.start();
	}

	private void update() {
		playerControl.steer(player);
		player.move();
		infoPanel.update();
	}

	private void renderScene() {
		var g = canvas.getGraphicsContext2D();
		g.setFill(scene.getFill());
		g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

		// Simulate a camera that keeps the ghost centered on the screen

		Point2D sceneCenter = new Point2D(scene.getWidth(), scene.getHeight()).multiply(0.5);
		Point2D playerViewCenter = player.center().multiply(tileSizeProperty.get());

		g.save();
		Point2D moveGridRelativeToPlayer = sceneCenter.subtract(playerViewCenter);
		g.translate(moveGridRelativeToPlayer.getX(), moveGridRelativeToPlayer.getY());
		mapView.render(g);
		g.restore();

		g.save();
		Point2D makePlayerCentered = sceneCenter.subtract(player.center());
		g.translate(makePlayerCentered.getX(), makePlayerCentered.getY());
		player.render(g);
		g.restore();
	}

	@Override
	public void stop() throws Exception {
		player.stopAnimations();
		gameClock.stop();
		log("Application stopped.");
	}

	private void handleKeyPressed(KeyEvent e) {
		switch (e.getCode()) {
		case F11:
			stage.setFullScreen(true);
			break;
		case I: {
			DEBUG_PROPERTY.set(!DEBUG_PROPERTY.get());
			break;
		}
		case DIGIT0: { // Links oben
			player.setCenter(Point2D.ZERO);
			break;
		}
		case C: { // Mitte
			player.setCenter(map.getNumCols() * 0.5, map.getNumRows() * 0.5);
			break;
		}
		case Z: { // Rechts unten
			player.setCenter(map.getNumCols(), map.getNumRows());
			break;
		}
		case PLUS: {
			if (tileSizeProperty.get() <= 128) {
				tileSizeProperty.set(tileSizeProperty.get() * 2);
			}
			break;
		}
		case MINUS: {
			if (tileSizeProperty.get() >= 2) {
				tileSizeProperty.set(tileSizeProperty.get() / 2);
			}
			break;
		}
		default:
			break;
		}
	}

	// InfoPanel

	private class InfoPanel extends VBox {

		private Text infoText;

		public InfoPanel() {
			infoText = new Text();
			infoText.setFont(Font.font("Sans", 20));
			infoText.setFill(Color.WHITE);
			getChildren().add(infoText);
			visibleProperty().bind(DEBUG_PROPERTY);
			setBackground(new Background(new BackgroundFill(Color.gray(0.66, 0.75), new CornerRadii(10), null)));
			setPadding(new Insets(20));
			setMaxSize(1, 1);
		}

		private void update() {
			var text = new StringBuilder();
			text.append(String.format("Map: %d rows %d cols", map.getNumRows(), map.getNumCols()));
			text.append("\n");
			text.append(String.format("Tile Size: %d", tileSizeProperty.get()));
			text.append("\n");
			text.append(String.format("Scene Size: %.0f x %.0f", scene.getWidth(), scene.getHeight()));
			text.append("\n");
			text.append(String.format("Canvas Size: %.0f x %.0f", canvas.getWidth(), canvas.getHeight()));
			infoText.setText(text.toString());
		}
	}
}
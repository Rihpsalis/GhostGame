package main;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;


public class App extends Application {
    private Panel panel;
    private AnimationTimer animationTimer;
    private Player player;
    private InputControl inputControl;
    private BorderPane bp;
    private Scene scene;
    private Rectangle2D screenBounds;
    private double screenHeight;
    private double screenWidth;
    private GameTimer gameTimer;
    private boolean[] directions;
    private Gridmap gridmap;
    private int size;
    private Group group;

    public void init() {
        screenBounds = Screen.getPrimary().getBounds();
        screenHeight = screenBounds.getHeight();
        screenWidth = screenBounds.getWidth();
        size = 150;
        directions = new boolean[4];
        bp = new BorderPane();
        group = new Group(bp);
        scene = new Scene(group);
        player = new Player(directions, size);
        inputControl = new InputControl(scene, player, directions);
        gridmap = new Gridmap(size, player);
        panel = new Panel(player, screenWidth, screenHeight, gridmap);
        animationTimer = new AnimationTimer(player);
        gameTimer = new GameTimer(inputControl, panel, gridmap);

    }

    @Override
    public void start(Stage stage) {

        bp.setCenter(panel);
        stage.setScene(scene);
        stage.setMinHeight(screenHeight);
        stage.setMinWidth(screenWidth);
        stage.setResizable(false);
        stage.setFullScreen(true);
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        scene.setFill(Color.DARKGRAY);

        stage.show();
        animationTimer.start();
        gameTimer.start();
        System.out.println(screenWidth);
        System.out.println(screenHeight);
    }
}

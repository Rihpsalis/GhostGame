package main.java;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class Panel extends Canvas {
    private final GraphicsContext gc;
    private final Player player;
    private Gridmap gridmap;

    public Panel(Player player, double screenWidth, double screenHeight, Gridmap gridmap) {
        super(screenWidth,screenHeight); // screenWidth,screenHeight implement
        gc = this.getGraphicsContext2D();
        this.player = player;
        this.gridmap = gridmap;


    }

    public void render() {
        gridmap.render(gc);
        player.render(gc);


    }

}

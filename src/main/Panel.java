package main;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Panel extends Canvas {
    //Grapics Context für Hintergrund
    private final GraphicsContext gcBg;
    //Grapics Context für Vordergrund
    private final GraphicsContext gcFg;
    private final Player player;
    private Gridmap gridmap;

    public Panel(Player player, double screenWidth, double screenHeight, Gridmap gridmap) {
        super(screenWidth,screenHeight); // screenWidth,screenHeight implement
        gcBg = this.getGraphicsContext2D();
        gcFg = this.getGraphicsContext2D();
        this.player = player;
        this.gridmap = gridmap;


    }

    public void render() {
        gridmap.render(gcBg);
        player.render(gcFg);


    }

}

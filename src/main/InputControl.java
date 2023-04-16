package main;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;

public class InputControl {
    private Scene scene;
    private boolean[] directions;
    private Player player;

    public InputControl(Scene scene, Player player, boolean[] directions) {
        this.scene = scene;
        this.directions = directions;
        this.player = player;

        scene.setOnKeyPressed(keyEvent -> {
            switch (keyEvent.getCode()) {
                case W -> directions[0] = true;
                case A -> directions[1] = true;
                case S -> directions[2] = true;
                case D -> directions[3] = true;

            }
        });

        scene.setOnKeyReleased(keyEvent -> {
            switch (keyEvent.getCode()) {
                case W -> directions[0] = false;
                case A -> directions[1] = false;
                case S -> directions[2] = false;
                case D -> directions[3] = false;

            }
        });
    }
   // setPlayerCords needed because with this Method Player can move in 2 Directions at the same time.
    public void setPlayerCords() {
        if (directions[2]) player.sMove();

        if (directions[0]) player.wMove();

        if (directions[1]) player.aMove();

        if (directions[3]) player.dMove();

    }
}

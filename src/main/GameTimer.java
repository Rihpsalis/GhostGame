package main;

public class GameTimer extends Thread{
    private InputControl inputControl;
    private Panel panel;
    private Gridmap gridmap;

    public GameTimer(InputControl inputControl, Panel panel, Gridmap gridmap) {
        this.inputControl = inputControl;
        this.panel = panel;
        this.gridmap = gridmap;
    }

    @Override
    public void run() {
        while (true) {
            inputControl.setPlayerCords();
            panel.render();
            gridmap.update();


            try {
                GameTimer.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

package main;

public class AnimationTimer extends Thread{
    private final Player player;

    public AnimationTimer(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        while (true) {

            try {
                AnimationTimer.sleep(150);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            player.nextImage();
        }
    }
}

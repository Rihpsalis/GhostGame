package ghostgame;

public class Timer extends Thread {
	private final Player player;

	public Timer(Player player) {
		this.player = player;
	}

	@Override
	public void run() {
		while (true) {

			try {
				Timer.sleep(150);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			player.nextImage();
		}
	}
}

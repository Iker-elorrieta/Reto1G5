package modelo;

public class Tiempos extends Thread{
	private boolean running;

	public Tiempos() {
		this.running = false;
	}
	public void run(int duracion) {
		running = true;
		while (running && duracion > 0) {
			try {
				Thread.sleep(1000);
				duracion--;
			} catch (InterruptedException e) {
				detener();
			}
		}
	}
	public void detener() {
		running = false;
	}
}

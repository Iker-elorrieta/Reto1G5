package modelo;

public class Cronometro extends Thread {
	private int segundos;
	private boolean running;

	public Cronometro() {
		this.segundos = 0;
		this.running = false;
	}

	public void run() {
		running = true;
		while (running) {
			try {
				Thread.sleep(1000);
				segundos++;
			} catch (InterruptedException e) {
				detener();
			}
		}
	}

	public void detener() {
		running = false;
	}

	public int getSegundos() {
		return segundos;
	}

	public void reiniciar() {
		segundos = 0;
	}

}

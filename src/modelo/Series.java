package modelo;

public class Series {

	private String nombre;
	private int repeticiones;
	private int duracion;
	private Ejercicios ejercicio;
	
	 public Series(String nombre, int repeticiones, int duracion, Ejercicios ejercicio) {
		this.nombre = nombre;
		this.repeticiones = repeticiones;
		this.duracion = duracion;
		this.ejercicio = ejercicio;
	}
	 
	 public String getNombre() {
		return nombre;
	}
	 public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	 public int getRepeticiones() {
		return repeticiones;
	}
	 public void setRepeticiones(int repeticiones) {
		this.repeticiones = repeticiones;
	}
	 public int getDuracion() {
		return duracion;
	}
	 public void setDuracion(int duracion) {
		this.duracion = duracion;
	}
	 public Ejercicios getEjercicio() {
		return ejercicio;
	}
	 public void setEjercicio(Ejercicios ejercicio) {
		this.ejercicio = ejercicio;
	}
}
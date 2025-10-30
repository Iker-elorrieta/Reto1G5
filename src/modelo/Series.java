package modelo;

public class Series {

	private String nombre;
	private int repeticiones;
	private int duracion;
	
	 public Series(String nombre, int repeticiones, int duracion) {
		this.nombre = nombre;
		this.repeticiones = repeticiones;
		this.duracion = duracion;
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
	
}
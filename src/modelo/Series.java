package modelo;

public class Series {

	private String nombre;
	private int repeticiones;
	private int duracion;
	private String imagen;
	
	 public Series(String nombre, int repeticiones, int duracion) {
		this.nombre = nombre;
		this.repeticiones = repeticiones;
		this.duracion = duracion;
	}
	 
	 public Series(String nombre, int repeticiones, int duracion, String imagen) {
			this.nombre = nombre;
			this.repeticiones = repeticiones;
			this.duracion = duracion;
			this.imagen = imagen;
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

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}
	 
	
}
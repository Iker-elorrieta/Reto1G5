package modelo;

import java.util.List;

public class Ejercicios {

	private int idEjercicio;
	private String nombre;
	private String descripcion;
	private String img;
	private int nivel;
	private int tiempoDescanso;
	private List<Series> series;
	
	
	
	public Ejercicios(int idEjercicio, String nombre, String descripcion, String img, int nivel, int tiempoDescanso,
			List<Series> series) {
		this.idEjercicio = idEjercicio;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.img = img;
		this.nivel = nivel;
		this.tiempoDescanso = tiempoDescanso;
		this.series = series;
	}
	
	//Por si queremos que el id se genere en la base de datos
	public Ejercicios(String nombre, String descripcion, String img, int nivel, int tiempoDescanso,
			List<Series> series) {
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.img = img;
		this.nivel = nivel;
		this.tiempoDescanso = tiempoDescanso;
		this.series = series;
	}

	public int getIdEjercicio() {
		return idEjercicio;
	}

	public void setIdEjercicio(int idEjercicio) {
		this.idEjercicio = idEjercicio;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public int getNivel() {
		return nivel;
	}

	public void setNivel(int nivel) {
		this.nivel = nivel;
	}

	public int getTiempoDescanso() {
		return tiempoDescanso;
	}

	public void setTiempoDescanso(int tiempoDescanso) {
		this.tiempoDescanso = tiempoDescanso;
	}

	public List<Series> getSeries() {
		return series;
	}

	public void setSeries(List<Series> series) {
		this.series = series;
	}
	
	
	
}

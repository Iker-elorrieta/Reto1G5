package modelo;

import java.util.ArrayList;

public class Ejercicios {

	private String nombre;
	private String descripcion;
	private String img;
	private int nivel;
	private int tiempoDescanso;
	private ArrayList<Series> series;

	public Ejercicios(String nombre, String descripcion, String img, int nivel, int tiempoDescanso,
			ArrayList<Series> series) {
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.img = img;
		this.nivel = nivel;
		this.tiempoDescanso = tiempoDescanso;
		this.series = series;
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

	public ArrayList<Series> getSeries() {
		return series;
	}

	public void setSeries(ArrayList<Series> series) {
		this.series = series;
	}

}

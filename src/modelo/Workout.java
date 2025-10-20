package modelo;

import java.util.ArrayList;
import java.util.List;

public class Workout {
	private String nombre;
	private String video;
	private int nivel;
	private int numEjers;
	private ArrayList<Ejercicios> ejercicios;
	
	public Workout() {
		this.nombre="";
		this.video="";
		this.nivel=0;
		
	}
	
	public Workout( String nombre, String video, int nivel, int numEjers) {
		this.nombre=nombre;
		this.video=video;
		this.nivel=nivel;
		this.numEjers=numEjers;
	}
	
	public Workout( String nombre, String video, int nivel, int numEjers, ArrayList<Ejercicios> ejercicios) {
		this.nombre=nombre;
		this.video=video;
		this.nivel=nivel;
		this.numEjers=numEjers;
		this.ejercicios=ejercicios;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getVideo() {
		return video;
	}
	public void setVideo(String video) {
		this.video = video;
	}
	public int getNivel() {
		return nivel;
	}
	public void setNivel(int nivel) {
		this.nivel = nivel;
	}
	public int getNumEjers() {
		return numEjers;
	}
	public void setNumEjers(int numEjers) {
		this.numEjers = numEjers;
	}
	public ArrayList<Ejercicios> getEjercicios() {
		return ejercicios;
	}
	public void setEjercicios(ArrayList<Ejercicios> ejercicios) {
		this.ejercicios = ejercicios;
	}
	
	
}

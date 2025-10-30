package modelo;

import java.util.ArrayList;
import java.util.Date;

public class Usuarios {

	private String idUsuario; // Hacemos este ID para recoger el numero de documento del usuario y poder
								// llegar a el más facil.
	private String nombre;
	private String apellido;
	private String email;
	private Date fecNac;
	private String contrasena;
	private int nivel;
	private ArrayList<HistoricoWorkouts> historico = new ArrayList<>();

	public Usuarios() {

	}

	// Como el usuario para cuando lo creamos todavia no existe, no se le ha
	// asignado ningun id, asi que no se lo pasamos al constructor

	public Usuarios(String nombre, String apellido, String email, String contrasena, Date fecNac, int nivel, ArrayList<HistoricoWorkouts> historico) {
		this.nombre = nombre;
		this.apellido = apellido;
		this.email = email;
		this.contrasena = contrasena;
		this.fecNac = fecNac;
		this.nivel=nivel;
		this.historico = historico;

	}

	public String getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getFecNac() {
		return fecNac;
	}

	public void setFecNac(Date fecNac) {
		this.fecNac = fecNac;
	}

	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	public int getNivel() {
		return nivel;
	}

	public void setNivel(int nivel) {
		this.nivel = nivel;
	}

	public ArrayList<HistoricoWorkouts> getHistorico() {
		return historico;
	}

	public void setHistorico(ArrayList<HistoricoWorkouts> historico) {
		this.historico = historico;
	}
	

}

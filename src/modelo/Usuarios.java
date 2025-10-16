package modelo;

import java.util.Date;

public class Usuarios {

	private String idUsuario; //Hacemos este ID para recoger el numero de documento del usuario y poder llegar a el más facil.
	private String nombre;
	private String apellido;
	private String email;
	private Date fecNac;
	private String contraseña;
	
	public Usuarios() {
		
	}
	
	
	// Como el usuario para cuando lo creamos todavia no existe, no se le ha asignado ningun id, asi que no se lo pasamos al constructor
	
	public Usuarios(String nombre, String apellido, String email, String contraseña, Date fecNac ) {
		this.nombre = nombre;
		this.apellido = apellido;
		this.email = email;
		this.contraseña = contraseña;
		this.fecNac = fecNac;
		
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

	public String getContraseña() {
		return contraseña;
	}

	public void setContraseña(String contraseña) {
		this.contraseña = contraseña;
	}
	
	
}

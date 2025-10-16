package controlador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import modelo.Usuarios;
import modelo.UsuariosConexion;

public class ControladorUsuarios {

	private UsuariosConexion gestor = new UsuariosConexion();
	
	public ArrayList<Usuarios> cargarUsuarios() throws IOException, InterruptedException, ExecutionException {
		return gestor.cargarUsuarios();
	}

	public void registrarUsuario(Usuarios usuario) throws IOException, InterruptedException, ExecutionException {
		gestor.registrarUsuario(usuario);
	}

	public boolean login(String nombre, String contraseña, ArrayList<Usuarios> listaUsuarios) {
    	return gestor.login(nombre, contraseña, listaUsuarios);
    }

}

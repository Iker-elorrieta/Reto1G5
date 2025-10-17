package controlador;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import modelo.Usuarios;
import modelo.UsuariosConexion;

public class ControladorUsuarios {

	private UsuariosConexion gestor = new UsuariosConexion();

	public void registrarUsuario(Usuarios usuario) throws IOException, InterruptedException, ExecutionException {
		gestor.registrarUsuario(usuario);
	}

	public boolean login(String usuario, String contraseña)  throws IOException, InterruptedException, ExecutionException {
		return gestor.login(usuario, contraseña);
	}


}

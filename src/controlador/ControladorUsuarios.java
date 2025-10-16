package controlador;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import modelo.Usuarios;
import modelo.UsuariosConexion;

public class ControladorUsuarios {

	UsuariosConexion gestor = new UsuariosConexion();

	public void registrarUsuario(Usuarios usuario) throws IOException, InterruptedException, ExecutionException {
		gestor.registrarUsuario(usuario);
	}

	public static Usuarios login(String email, String contraseña)
			throws IOException, InterruptedException, ExecutionException {
		return UsuariosConexion.login(email, contraseña);
	}

}

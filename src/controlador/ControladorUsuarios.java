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

	public void login(Usuarios usuario) throws IOException {
		gestor.login(usuario);
	}

}

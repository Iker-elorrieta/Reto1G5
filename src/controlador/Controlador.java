package controlador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import modelo.GestorWorkout;
import modelo.Usuarios;
import modelo.UsuariosConexion;
import modelo.Workout;

public class Controlador {

	private UsuariosConexion gestor = new UsuariosConexion();
	private GestorWorkout gestor2 = new GestorWorkout();

	public void registrarUsuario(Usuarios usuario) throws IOException, InterruptedException, ExecutionException {
		gestor.registrarUsuario(usuario);
	}

	public boolean login(String usuario, String contraseña)
			throws IOException, InterruptedException, ExecutionException {
		return gestor.login(usuario, contraseña);
	}

	public ArrayList<Workout> leerWorkoutsBD(int nivelUsuario) throws IOException, InterruptedException, ExecutionException {
		return gestor2.leerWorkoutsBD(nivelUsuario);
	}
}

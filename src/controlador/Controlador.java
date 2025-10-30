package controlador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

//import modelo.GestorBackup;
import modelo.GestorWorkout;
import modelo.HistoricoWorkouts;
import modelo.Usuarios;
import modelo.GestorBackup;
import modelo.GestorUsuarios;
import modelo.Workout;

public class Controlador {

	private GestorUsuarios gestor = new GestorUsuarios();
	private GestorWorkout gestor2 = new GestorWorkout();
	private GestorBackup gestor3 = new GestorBackup();

	public Usuarios obtenerUsuario(String nombre, String contraseña)
			throws InterruptedException, ExecutionException, IOException {
		GestorUsuarios gestor = new GestorUsuarios();
		Usuarios u = gestor.obtenerUsuario(nombre, contraseña);
		return u;
	}

	public void registrarUsuario(Usuarios usuario) throws IOException, InterruptedException, ExecutionException {
		gestor.registrarUsuario(usuario);
	}

	public boolean login(String usuario, String contraseña)
			throws IOException, InterruptedException, ExecutionException {
		return gestor.login(usuario, contraseña);
	}

	public ArrayList<Workout> leerWorkoutsBD(int nivelUsuario)
			throws IOException, InterruptedException, ExecutionException {
		return gestor2.leerWorkoutsBD(nivelUsuario);
	}

	public ArrayList<Workout> leerWorkoutsBDBackups() throws IOException, InterruptedException, ExecutionException {
		return gestor2.leerWorkoutsBDBackups();
	}

	public ArrayList<HistoricoWorkouts> cargarDatos(Usuarios usu)
			throws IOException, InterruptedException, ExecutionException {
		return gestor2.cargarDatos(usu);

	}
	public void llamarBackup() {
		gestor3.llamarBackup();
	}
	public boolean existeUsuario(String nombre, String email)
			throws InterruptedException, ExecutionException, IOException {
		return gestor.existeUsuario(nombre, email);
	}

//	public boolean comprobarConexion() throws IOException {
//		return gestor3.comprobarConexion();
//	}

}

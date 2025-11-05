package controlador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

//import modelo.GestorBackup;
import modelo.GestorWorkout;
import modelo.HistoricoWorkouts;
import modelo.Usuarios;
import modelo.GestorUsuarios;
import modelo.Workout;

public class Controlador {

	private GestorUsuarios gestor = new GestorUsuarios();
	private GestorWorkout gestor2 = new GestorWorkout();

	
	public void subirNivelUsuario(Usuarios usuario) throws IOException, InterruptedException, ExecutionException {
		gestor.subirNivelUsuario(usuario);
	}
	
	public Usuarios obtenerUsuario(String nombre, String contraseña)
			throws InterruptedException, ExecutionException, IOException {
		GestorUsuarios gestor = new GestorUsuarios();
		Usuarios u = gestor.obtenerUsuario(nombre, contraseña);
		return u;
	}

	public void registrarUsuario(Usuarios usuario) throws IOException, InterruptedException, ExecutionException {
		gestor.registrarUsuario(usuario);
	}
	
	public void actualizarUsuario(Usuarios usuario) throws IOException, InterruptedException, ExecutionException {
		gestor.actualizarUsuario(usuario);
	}

	public boolean login(String usuario, String contraseña)
			throws IOException, InterruptedException, ExecutionException {
		return gestor.login(usuario, contraseña);
	}

	public ArrayList<Workout> leerWorkoutsBD(int nivelUsuario)
			throws IOException, InterruptedException, ExecutionException {
		return gestor2.leerWorkoutsBD(nivelUsuario);
	}
	public boolean existeUsuario(String nombre, String email)
			throws InterruptedException, ExecutionException, IOException {
		return gestor.existeUsuario(nombre, email);
	}
    public ArrayList<HistoricoWorkouts> cargarDatos(Usuarios usu) throws IOException, InterruptedException, ExecutionException{
    	return gestor2.cargarDatos(usu);
    }

	public void guardarHistoricoAutomatico(Usuarios usuario, Workout workout, int tiempoTotal,
			double porcentajeCompletado) throws IOException, InterruptedException, ExecutionException {
		gestor2.guardarHistoricoAutomatico(usuario, workout, tiempoTotal, porcentajeCompletado);
	}
	public void guardarHistoricoManual(Usuarios usuario, String nombreWorkout, int tiempoTotal,
			double porcentajeCompletado) throws IOException, InterruptedException, ExecutionException {
		gestor2.guardarHistoricoLocal(usuario, null, tiempoTotal, porcentajeCompletado);;
			
	}
//	public boolean comprobarConexion() throws IOException {
//		return gestor3.comprobarConexion();
//	}

}

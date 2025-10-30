package modelo;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;

public class GestorUsuarios {
	
	private final String coleccion = "usuarios";
	private final String nombre = "Nombre";
	private final String apellido = "Apellido";
	private final String nivel = "nivel";
	private final String contrasena = "Contraseña";
	private final String email = "Email";
	private final String fecha = "FecNac";
	private final String vacio = "";



	public Usuarios obtenerUsuario(String nombreRecogido, String contrasenaRecogida)
			throws InterruptedException, ExecutionException, IOException {

		if (nombreRecogido == null || nombreRecogido.trim().isEmpty() || contrasenaRecogida == null || contrasenaRecogida.trim().isEmpty()) {
			System.out.println("Nombre o contraseña vacíos");
			return null;
		}

		Firestore db = null;
		try {
			db = ConectorFirebase.conectar();
			CollectionReference usuariosCol = db.collection(coleccion);

			QuerySnapshot query = usuariosCol.whereEqualTo(nombre, nombreRecogido).whereEqualTo(contrasena, contrasenaRecogida)
					.get().get();

			if (query == null || query.isEmpty()) {
				System.out.println("No se encontró ningún usuario con esos datos");
				return null;
			}

			for (QueryDocumentSnapshot doc : query.getDocuments()) {
				Usuarios u = crearUsuarioDesdeDocumento(doc);
				if (u != null)
					return u;
			}

			return null;

		} finally {
			if (db != null)
				ConectorFirebase.cerrar(db);
		}
	}

	public void registrarUsuario(Usuarios usuario) throws IOException, InterruptedException, ExecutionException {

		Firestore db = ConectorFirebase.conectar();
		CollectionReference usuarios = db.collection(coleccion);

		Map<String, Object> datos = new HashMap<>();
		datos.put(nombre, usuario.getNombre());
		datos.put(apellido, usuario.getApellido());
		datos.put(email, usuario.getEmail());
		datos.put(contrasena, usuario.getContrasena());
		datos.put(fecha, usuario.getFecNac());

		// Usar ID automático de Firestore
		DocumentReference docRef = usuarios.document();
		docRef.set(datos);
	}

	public boolean login(String usuario, String contrasenaRecogida)
			throws IOException, InterruptedException, ExecutionException {

		Firestore db = ConectorFirebase.conectar();
		ApiFuture<QuerySnapshot> future = db.collection(coleccion).whereEqualTo(nombre, usuario)
				.whereEqualTo(contrasena, contrasenaRecogida).get();

		List<QueryDocumentSnapshot> cliente = future.get().getDocuments();
		if (cliente != null && !cliente.isEmpty()) {
			return true;
		} else {
			return false;
		}

	}

	private Usuarios crearUsuarioDesdeDocumento(DocumentSnapshot doc) {

		if (doc == null) {
			return null;
		}

		Usuarios u = new Usuarios();

		u.setIdUsuario(doc.getId());

		if (doc.getString(nombre) != null) {
			u.setNombre(doc.getString(nombre));
		} else {
			u.setNombre(vacio);
		}

		if (doc.getString(contrasena) != null) {
			u.setContrasena(doc.getString(contrasena));
		} else {
			u.setContrasena(vacio);
		}

		if (doc.getLong(nivel) != null) {
			u.setNivel(doc.getLong(nivel).intValue());
		} else {
			u.setNivel(0);
		}

		if (doc.getString(apellido) != null) {
			u.setApellido(doc.getString(apellido));
		} else {
			u.setApellido(vacio);
		}

		if (doc.getString(email) != null) {
			u.setEmail(doc.getString(email));
		} else {
			u.setEmail(vacio);
		}

		if (doc.getDate(fecha) != null) {
			u.setFecNac(doc.getDate(fecha));
		} else {
			u.setFecNac(null);
		}

		return u;
	}

	public boolean existeUsuario(String nombreRecogido, String emailRecogido)
			throws InterruptedException, ExecutionException, IOException {
		Firestore db = ConectorFirebase.conectar();
		CollectionReference usuariosCol = db.collection(coleccion);

		// Buscamos por nombre o email
		QuerySnapshot queryNombre = usuariosCol.whereEqualTo(nombre, nombreRecogido).get().get();
		QuerySnapshot queryEmail = usuariosCol.whereEqualTo(email, emailRecogido).get().get();
		if (!queryNombre.isEmpty() || !queryEmail.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

}

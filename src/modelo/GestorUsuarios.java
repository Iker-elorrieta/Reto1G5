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

public class GestorUsuarios {

	public Usuarios obtenerUsuario(String nombre, String contraseña) throws InterruptedException, ExecutionException, IOException {
		Firestore db = ConectorFirebase.conectar();
		CollectionReference usuariosCol = db.collection("usuarios");

		QuerySnapshot query = usuariosCol.whereEqualTo("Nombre", nombre).whereEqualTo("Contraseña", contraseña).get()
				.get();

		for (QueryDocumentSnapshot doc : query.getDocuments()) {
			Usuarios u = new Usuarios();
			u.setNombre(doc.getString("Nombre"));
			u.setContraseña(doc.getString("Contraseña"));
			u.setNivel(doc.getDouble("nivel").intValue());
			u.setApellido(doc.getString("Apellido"));
			u.setEmail(doc.getString("Email"));
			u.setFecNac(doc.getDate("FecNac"));
			System.out.println(u);

			return u;
		}

		return null; // no se encontró usuario
	}

	public void registrarUsuario(Usuarios usuario) throws IOException, InterruptedException, ExecutionException {

		Firestore db = ConectorFirebase.conectar();
		CollectionReference usuarios = db.collection("usuarios");

		Map<String, Object> datos = new HashMap<>();
		datos.put("Nombre", usuario.getNombre());
		datos.put("Apellido", usuario.getApellido());
		datos.put("Email", usuario.getEmail());
		datos.put("Contraseña", usuario.getContraseña());
		datos.put("FecNac", usuario.getFecNac());

		DocumentReference docRef = usuarios.document("2"); // Generamos un ID automatico, aunque si os parece lo podemos
															// hacer con el email para no generar numeros random
		docRef.set(datos);
	}

	public boolean login(String usuario, String contraseña)
			throws IOException, InterruptedException, ExecutionException {

		Firestore db = ConectorFirebase.conectar();
		ApiFuture<QuerySnapshot> future = db.collection("usuarios").whereEqualTo("Nombre", usuario)
				.whereEqualTo("Contraseña", contraseña).get();

		List<QueryDocumentSnapshot> cliente = future.get().getDocuments();
		if (cliente != null && !cliente.isEmpty()) {
			return true;
		} else {
			return false;
		}

	}

}

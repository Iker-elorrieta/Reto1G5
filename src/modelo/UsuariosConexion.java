package modelo;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;

public class UsuariosConexion {

//	public ArrayList<Usuarios> cargarUsuarios() throws IOException, InterruptedException, ExecutionException {
//		Firestore db = ConectorFirebase.recogerConexion();
//		QuerySnapshot query = db.collection("usuarios").get().get();
//		ArrayList<Usuarios> usuariosArray = new ArrayList<>();
//
//		List<QueryDocumentSnapshot> usuarios = query.getDocuments();
//		for (QueryDocumentSnapshot usuario : usuarios) {
//			Usuarios meter = new Usuarios();
//			meter.setIdUsuario(usuario.getId());
//			meter.setNombre(usuario.getString("Nombre"));
//			meter.setApellido(usuario.getString("Apellido"));
//			meter.setContraseña(usuario.getString("Contraseña"));
//			meter.setEmail(usuario.getString("Email"));
//			meter.setFecNac(usuario.getDate("FecNac"));
//			usuariosArray.add(meter);
//
//		}
//		return usuariosArray;
//	}

	public void registrarUsuario(Usuarios usuario) throws IOException, InterruptedException, ExecutionException {

		Firestore db = ConectorFirebase.recogerConexion();
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

	public boolean login(Usuarios usuario) throws IOException {
		Firestore db = ConectorFirebase.recogerConexion();
		CollectionReference usuarios = db.collection("usuarios");
		

		usuarios.whereEqualTo("Nombre", usuario.getNombre()).get().addListener(() -> {
			try {
				var querySnapshot = usuarios.whereEqualTo("Nombre", usuario.getNombre()).get().get();
				if (!querySnapshot.isEmpty()) {
					var doc = querySnapshot.getDocuments().get(0);
					String contrasenaBD = doc.getString("Contraseña");
					if (contrasenaBD.equals(usuario.getContraseña())) {
						System.out.println("Login correcto");
					} else {
						System.out.println("Contraseña incorrecta");
					}
				} else {
					System.out.println("Usuario no encontrado");
				}		

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Error de conexión");
			}
		}, Runnable::run);
		return false;
	}
}

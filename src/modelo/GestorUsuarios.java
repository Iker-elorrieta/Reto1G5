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

	public Usuarios obtenerUsuario(String nombre, String contraseña)
	        throws InterruptedException, ExecutionException, IOException {

	    if (nombre == null || nombre.trim().isEmpty() || contraseña == null || contraseña.trim().isEmpty()) {
	        System.out.println("Nombre o contraseña vacíos");
	        return null;
	    }

	    Firestore db = null;
	    try {
	        db = ConectorFirebase.conectar();
	        CollectionReference usuariosCol = db.collection("usuarios");

	        QuerySnapshot query = usuariosCol
	                .whereEqualTo("Nombre", nombre)
	                .whereEqualTo("Contraseña", contraseña)
	                .get().get();

	        if (query == null || query.isEmpty()) {
	            System.out.println("No se encontró ningún usuario con esos datos");
	            return null;
	        }

	        for (QueryDocumentSnapshot doc : query.getDocuments()) {
	            Usuarios u = crearUsuarioDesdeDocumento(doc);
	            if (u != null) return u;
	        }

	        return null;

	    } finally {
	        if (db != null) ConectorFirebase.cerrar(db);
	    }
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
	
	private Usuarios crearUsuarioDesdeDocumento(DocumentSnapshot doc) {
		
	    if (doc == null) {
	    	return null;
	    }

	    Usuarios u = new Usuarios();

	    
	    if (doc.getString("Nombre") != null) {
	        u.setNombre(doc.getString("Nombre"));
	    } else {
	        u.setNombre("");
	    }

	   
	    if (doc.getString("Contraseña") != null) {
	        u.setContraseña(doc.getString("Contraseña"));
	    } else {
	        u.setContraseña("");
	    }

	    
	    if (doc.getLong("nivel") != null) {
	        u.setNivel(doc.getLong("nivel").intValue());
	    } else {
	        u.setNivel(0);
	    }

	 
	    if (doc.getString("Apellido") != null) {
	        u.setApellido(doc.getString("Apellido"));
	    } else {
	        u.setApellido("");
	    }

	    
	    if (doc.getString("Email") != null) {
	        u.setEmail(doc.getString("Email"));
	    } else {
	        u.setEmail("");
	    }

	    
	    if (doc.getDate("FecNac") != null) {
	        u.setFecNac(doc.getDate("FecNac"));
	    } else {
	        u.setFecNac(null);
	    }

	    return u;
	}


}

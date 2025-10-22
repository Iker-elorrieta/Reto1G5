package modelo;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;

public class GestorBackup {

	public boolean comprobarConexion() throws IOException {
		try {
			Firestore db = ConectorFirebase.conectar();

			CollectionReference coleccion = db.collection("Usuarios");
			ApiFuture<QuerySnapshot> future = coleccion.limit(1).get();
			future.get();

			System.out.println("Conexión a Firestore OK");
			return true;

		} catch (IOException | InterruptedException | ExecutionException e) {
			System.out.println("No hay conexión a Firestore: " + e.getMessage());
			return false;
		}
	}

}

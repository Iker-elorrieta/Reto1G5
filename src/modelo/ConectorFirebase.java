package modelo;

import java.io.FileInputStream;
import java.io.IOException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;

public class ConectorFirebase {

	public static Firestore conectar() throws IOException {

		FileInputStream srvcAccnt = new FileInputStream("usuarios.json");

		FirestoreOptions opciones = FirestoreOptions.getDefaultInstance().toBuilder().setProjectId("reto1-9e159")
				.setCredentials(GoogleCredentials.fromStream(srvcAccnt)).build();

		return opciones.getService();

	}

	public static void cerrar(Firestore db) {
		if (db != null) {
			try {
				db.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

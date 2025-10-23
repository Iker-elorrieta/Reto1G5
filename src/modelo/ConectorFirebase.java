package modelo;

import java.io.FileInputStream;
import java.io.IOException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;

public class ConectorFirebase {

    // Singleton instance to reuse the same Firestore across the app
    private static Firestore instance;

    public synchronized static Firestore conectar() throws IOException {
        if (instance == null) {
            FileInputStream srvcAccnt = new FileInputStream("usuarios.json");

            FirestoreOptions opciones = FirestoreOptions.getDefaultInstance().toBuilder()
                    .setProjectId("reto1-9e159")
                    .setCredentials(GoogleCredentials.fromStream(srvcAccnt)).build();

            instance = opciones.getService();
        }
        return instance;

    }

    public static void cerrar(Firestore db) {
        if (db != null) {
            try {
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // Clear the singleton reference if we're closing the active instance
                if (db == instance) {
                    instance = null;
                }
            }
        }
    }
}
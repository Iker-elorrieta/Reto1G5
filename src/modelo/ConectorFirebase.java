package modelo;

import java.io.FileInputStream;
import java.io.IOException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

public class ConectorFirebase {

	private static Firestore conexion;
	
	public static Firestore recogerConexion() throws IOException {
        if (conexion == null) {
            FileInputStream serviceAccount = new FileInputStream("usuarios.json");

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount)) //Aqui le metes las creedenciales, que es basicamente el json de Usuarios
                    .build();

            FirebaseApp.initializeApp(options);
            conexion = FirestoreClient.getFirestore(); //Cogemos la instancia de Firestore y se la metemos a la conexion para poder usarla

            System.out.println("Conectado correctamente a Firestore");
        }
        return conexion;
    }
}

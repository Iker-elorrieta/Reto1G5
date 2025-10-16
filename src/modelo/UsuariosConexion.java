package modelo;

import com.google.cloud.firestore.Firestore;

import java.io.IOException;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;


public class UsuariosConexion {

	
	public void registrarUsuario(Usuarios usuario) throws IOException , InterruptedException , ExecutionException{
       
            Firestore db = ConectorFirebase.recogerConexion();
            CollectionReference usuarios = db.collection("usuarios");

            Map<String, Object> datos = new HashMap<>();
            datos.put("Nombre", usuario.getNombre());
            datos.put("Apellido", usuario.getApellido());
            datos.put("Email", usuario.getEmail());
            datos.put("Contraseña", usuario.getContraseña());
            datos.put("FecNac", usuario.getFecNac());

            DocumentReference docRef = usuarios.document("2"); //Generamos un ID automatico, aunque si os parece lo podemos hacer con el email para no generar numeros random
            docRef.set(datos);
    }

    public void login(Usuarios usuario) throws IOException, InterruptedException, ExecutionException {
        
            Firestore db = ConectorFirebase.recogerConexion();
            CollectionReference usuarios = db.collection("usuarios");

            // Buscamos usuarios por usuario
            var query = usuarios.whereEqualTo("Nombre", usuario.getNombre())
                                .whereEqualTo("Contraseña", usuario.getContraseña())
                                .get().get();

            if (!query.isEmpty()) {
                var doc = query.getDocuments().get(0);
                Date fechaNacimiento = (Date) doc.getDate("FecNac");
                Usuarios u = new Usuarios(
                    doc.getString("Nombre"),
                    doc.getString("Apellido"),
                    doc.getString("Email"),
                    doc.getString("Contraseña"),
                    fechaNacimiento
                );
                u.setIdUsuario(doc.getId());
            } else {
            	System.out.println("El usuario o contraseña estan incorrectos");
            }
        
    }
}

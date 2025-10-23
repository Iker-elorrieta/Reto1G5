package vista;

import javax.swing.SwingUtilities;
import modelo.ConectorFirebase;
import com.google.cloud.firestore.Firestore;

public class Main {

    private static Firestore db; //

    public static void main(String[] args) {
        try {
            // Inicializar Firebase y guardar la conexión
            db = ConectorFirebase.conectar();

            // Mostrar ventana de login
            SwingUtilities.invokeLater(() -> {
                VentanaLogin login = new VentanaLogin();
                login.setVisible(true);
            });

            // Añadir un hook para cerrar Firestore al cerrar la app
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                if (db != null) {
                    try {
                        ConectorFirebase.cerrar(db);
                        System.out.println("Firestore cerrado correctamente.");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
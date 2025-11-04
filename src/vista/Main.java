package vista;

import javax.swing.SwingUtilities;
import modelo.ConectorFirebase;
import modelo.GestorBackup;

import com.google.cloud.firestore.Firestore;

public class Main {

    private static Firestore db; //

    public static void main(String[] args) {
    	GestorBackup gb = new GestorBackup();

        System.out.println("=== PRUEBA LECTURA .DAT ===");
        gb.leerDat(); // intentará leer backup_global.dat o RETOSEGUNDO/backup_global.dat

        System.out.println("\n=== PRUEBA LECTURA .XML ===");
        gb.leerXml(); // intentará leer historico_global.xml o RETOSEGUNDO/historico_global.xml
    
        try {
            // Inicializar Firebase y guardar la conexiónn
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
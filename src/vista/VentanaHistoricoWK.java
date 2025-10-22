package vista;

import modelo.ConectorFirebase;
import modelo.HistoricoWorkouts;
import modelo.Usuarios;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VentanaHistoricoWK extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private Usuarios usu; // ID del usuario logeado

    public VentanaHistoricoWK(Usuarios usuario) {
        usu = usuario;

        setTitle("Histórico de Workouts");
        setSize(865, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // --- Tabla ---
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{
                "Nombre Workout", "Nivel", "Tiempo Total", "Tiempo Previsto", "Fecha", "% Completado"
        });

        // --- Panel inferior con botón "Ir atrás" ---
        JButton btnAtras = new JButton("Ir atrás");
        btnAtras.addActionListener(e -> this.dispose()); // cierra la ventana actual

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout(FlowLayout.LEFT));
        panelBotones.add(btnAtras);

        getContentPane().add(panelBotones, BorderLayout.SOUTH);

        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        
        // --- Cargar datos ---
        cargarDatos();
        repaint();
        
        setVisible(true);
    }

    private void cargarDatos() {
        try {
            Firestore db = ConectorFirebase.conectar();
            CollectionReference historicoRef = db.collection("usuarios")
                    .document(String.valueOf(usu.getIdUsuario()))
                    .collection("Historico");

            ApiFuture<QuerySnapshot> query = historicoRef.orderBy("fecha", Query.Direction.DESCENDING).get();
            
            QuerySnapshot snapshot = query.get();
            System.out.println("Docs encontrados: " + snapshot.size()); // debug
            
            
            List<HistoricoWorkouts> lista = new ArrayList<>();
            for (DocumentSnapshot doc : query.get().getDocuments()) {
                HistoricoWorkouts hw = new HistoricoWorkouts(
                        doc.getString("nombreWorkout"),
                        doc.getLong("nivel").intValue(),
                        doc.getLong("tiempoTotal").intValue(),
                        doc.getLong("tiempoPrevisto").intValue(),
                        doc.getDate("fecha"),
                        doc.getDouble("porcentajeCompletado")
                );
                lista.add(hw);
            }

            // Llenar la tabla
            for (HistoricoWorkouts hw : lista) {
                tableModel.addRow(new Object[]{
                        hw.getNombreWorkout(),
                        hw.getNivel(),
                        formatearTiempo(hw.getTiempoTotal()),
                        formatearTiempo(hw.getTiempoPrevisto()),
                        hw.getFecha(),
                        hw.getPorcentajeCompletado() + "%"
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar histórico: " + e.getMessage());
        }
        tableModel.fireTableDataChanged();
    }

    private String formatearTiempo(int segundos) {
        int min = segundos / 60;
        int seg = segundos % 60;
        return String.format("%02d:%02d", min, seg);
    }
}

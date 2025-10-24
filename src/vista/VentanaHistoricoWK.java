package vista;



import controlador.Controlador;
import modelo.HistoricoWorkouts;
import modelo.Usuarios;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class VentanaHistoricoWK extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTable table;
    private DefaultTableModel tableModel;
    private Usuarios usu;
    private Controlador controlador;

    public VentanaHistoricoWK(Usuarios usuario) {
        usu = usuario;
        controlador = new Controlador();
        setTitle("Histórico de Workouts");
        setSize(865, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{
                "Nombre Workout", "Nivel", "Tiempo Total", "Tiempo Previsto", "Fecha", "% Completado"
        });

        JButton btnAtras = new JButton("Ir atrás");
        btnAtras.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		dispose();
        		VentanaWorkouts wk = new VentanaWorkouts(usuario);
        		wk.setVisible(true);
        		
        	}
        });

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout(FlowLayout.LEFT));
        panelBotones.add(btnAtras);

        getContentPane().add(panelBotones, BorderLayout.SOUTH);

        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        
        cargarDatosTabla();
        repaint();
        
        setVisible(true);
    }
    private void cargarDatosTabla() {
        try {
            for (HistoricoWorkouts hw : controlador.cargarDatos(usu)) {
                String nombre = (hw.getNombreWorkout() != null) ? hw.getNombreWorkout() : "";
                int nivel = (hw.getWorkout() != null) ? hw.getWorkout().getNivel() : 0;
                int tiempoTotal = hw.getTiempoTotal();
                int tiempoPrevisto = hw.getTiempoPrevisto();
                String fechaStr = (hw.getFecha() != null) ? hw.getFecha().toString() : "";
                String porcentaje = hw.getPorcentajeCompletado() + "%";

                tableModel.addRow(new Object[]{nombre, nivel, tiempoTotal, tiempoPrevisto, fechaStr, porcentaje});
            }
        } catch (IOException | InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error al cargar los históricos: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    }

    
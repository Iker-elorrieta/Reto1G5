package vista;


import controlador.Controlador;
import modelo.Usuarios;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class VentanaHistoricoWK extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTable table;
    private DefaultTableModel tableModel;
    private Usuarios usu; // ID del usuario logeado
    private Controlador controlador;

    public VentanaHistoricoWK(Usuarios usuario) {
        usu = usuario;
        controlador = new Controlador();
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
        try {
			controlador.cargarDatos(usu.getEmail());
		} catch (IOException | InterruptedException | ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        repaint();
        
        setVisible(true);
    }

 
}

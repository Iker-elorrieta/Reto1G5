package vista;

import controlador.Controlador;
import modelo.HistoricoWorkouts;
import modelo.Usuarios;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class VentanaHistoricoWK extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTable table;
	private DefaultTableModel tableModel;
	private Usuarios usu;
	private Controlador controlador;

	public VentanaHistoricoWK(Usuarios usuario) {
		usu = usuario;
		controlador = new Controlador();

		setTitle("POWERHOUSE GYM - Histórico");
		setSize(900, 650);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);

		JPanel contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(15, 15, 15, 15));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		// --- Logo ---
		JLabel lblIMG = new JLabel("");
		lblIMG.setBounds(10, 10, 160, 120);
		ImageIcon icono = new ImageIcon("iconoSF.png");
		if (icono.getImage() != null) {
			Image imagen = icono.getImage().getScaledInstance(lblIMG.getWidth(), lblIMG.getHeight(),
					Image.SCALE_SMOOTH);
			lblIMG.setIcon(new ImageIcon(imagen));
		}
		contentPane.add(lblIMG);

		// --- Título ---
		JLabel lblTitulo = new JLabel("POWERHOUSE GYM");
		lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
		lblTitulo.setForeground(new Color(200, 0, 0));
		lblTitulo.setBounds(180, 20, 400, 50);
		contentPane.add(lblTitulo);

		JLabel lblSubtitulo = new JLabel("HISTÓRICO DE WORKOUTS");
		lblSubtitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
		lblSubtitulo.setForeground(Color.BLACK);
		lblSubtitulo.setBounds(180, 65, 300, 30);
		contentPane.add(lblSubtitulo);

		// --- Botón atrás ---
		JButton btnAtras = new JButton("Volver");
		btnAtras.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnAtras.setFocusPainted(false);
		btnAtras.setBackground(new Color(200, 0, 0));
		btnAtras.setForeground(Color.WHITE);
		btnAtras.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		btnAtras.setBounds(720, 35, 140, 40);

		btnAtras.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent e) {
				btnAtras.setBackground(new Color(255, 40, 40));
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent e) {
				btnAtras.setBackground(new Color(200, 0, 0));
			}
		});

		btnAtras.addActionListener(e -> {
			dispose();
			new VentanaWorkouts(usuario).setVisible(true);
		});

		contentPane.add(btnAtras);

		// --- Tabla ---
		tableModel = new DefaultTableModel();
		tableModel.setColumnIdentifiers(
				new String[] { "Nombre Workout", "Nivel", "Tiempo Total", "Tiempo Previsto", "Fecha", "% Completado" });

		table = new JTable(tableModel);
		table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		table.setRowHeight(28);
		table.setBackground(Color.WHITE);
		table.setForeground(Color.BLACK);
		table.setSelectionBackground(new Color(255, 70, 70));
		table.setSelectionForeground(Color.WHITE);
		table.setGridColor(new Color(200, 0, 0));

		JTableHeader header = table.getTableHeader();
		header.setFont(new Font("Segoe UI", Font.BOLD, 15));
		header.setBackground(Color.BLACK);
		header.setForeground(Color.WHITE);

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(30, 160, 820, 430);
		contentPane.add(scrollPane);

		JLabel lblTituloTabla = new JLabel("HISTÓRICO");
		lblTituloTabla.setFont(new Font("Segoe UI", Font.BOLD, 16));
		lblTituloTabla.setForeground(new Color(200, 0, 0));
		lblTituloTabla.setBounds(380, 130, 200, 25);
		contentPane.add(lblTituloTabla);

		cargarDatosTabla();
	}

	private void cargarDatosTabla() {
		try {
			for (HistoricoWorkouts hw : controlador.cargarDatos(usu)) {
				tableModel.addRow(new Object[] { hw.getNombreWorkout(),
						hw.getWorkout() != null ? hw.getWorkout().getNivel() : 0, hw.getTiempoTotal(),
						hw.getTiempoPrevisto(), hw.getFecha() != null ? hw.getFecha().toString() : "",
						hw.getPorcentajeCompletado() + "%" });
			}
		} catch (IOException | InterruptedException | ExecutionException ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error al cargar históricos: " + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}

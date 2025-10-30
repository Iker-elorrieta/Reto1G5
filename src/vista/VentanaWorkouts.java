package vista;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;

import controlador.Controlador;
import modelo.Ejercicios;
import modelo.Usuarios;
import modelo.Workout;
import java.io.IOException;

public class VentanaWorkouts extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;
	private JTable table_1;
	private JComboBox<String> comboNivel;
	private DefaultTableModel modelo;
	private DefaultTableModel modelo2;
	private ArrayList<Workout> listaWorkouts;
	private ArrayList<Workout> listaMostrada;
	private Controlador controlador;
	private Usuarios usuarioActual;
	// store the currently selected workout so we can open VentanaEjercicio from the exercises table
	private Workout workoutSeleccionado;

	public VentanaWorkouts(Usuarios usuario) {
		controlador = new Controlador();
		usuarioActual = usuario;

		setTitle("POWERHOUSE GYM - Workouts");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 950, 650);
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(15, 15, 15, 15));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		// --- Logo Powerhouse Gym ---
		JLabel lblIMG = new JLabel("");
		lblIMG.setBounds(10, 11, 160, 128);
		ImageIcon icono = new ImageIcon("iconoSF.png" );
		if (icono.getImage() != null) { // verificamos que no sea null
		    Image imagen = icono.getImage().getScaledInstance(lblIMG.getWidth(), lblIMG.getHeight(), Image.SCALE_SMOOTH);
		    lblIMG.setIcon(new ImageIcon(imagen));
		} else {
		    System.out.println("Imagen no encontrada");
		}
		contentPane.add(lblIMG);
		
		
		JButton btnPerfil = new JButton("");
		
		btnPerfil.setBounds(729, 11, 195, 141);
		ImageIcon iconosPerfil = new ImageIcon("iconosPerfil.png");
		if (iconosPerfil.getImage() != null) { // verificamos que no sea null
		    Image imagen = iconosPerfil.getImage().getScaledInstance(btnPerfil.getWidth(), btnPerfil.getHeight(), Image.SCALE_SMOOTH);
		    btnPerfil.setIcon(new ImageIcon(imagen));
		} else {
		    System.out.println("Imagen no encontrada");
		}
		
		// Quitar el fondo, el borde y hacer que parezca solo una imagen
		btnPerfil.setBorderPainted(false);
		btnPerfil.setFocusPainted(false);
		btnPerfil.setContentAreaFilled(false);
		btnPerfil.setOpaque(false);
		
		
		btnPerfil.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VentanaConfigUsuarios v1 = new VentanaConfigUsuarios(usuarioActual);
				v1.setVisible(true);
			}
		});
		
		
		
		contentPane.add(btnPerfil);

		// --- Título principal ---
		JLabel lblTitulo = new JLabel("POWERHOUSE GYM");
		lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 30));
		lblTitulo.setForeground(new Color(200, 0, 0));
		lblTitulo.setBounds(180, 30, 400, 50);
		contentPane.add(lblTitulo);

		JLabel lblSubtitulo = new JLabel("WORKOUTS DISPONIBLES");
		lblSubtitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
		lblSubtitulo.setForeground(Color.BLACK);
		lblSubtitulo.setBounds(180, 70, 300, 30);
		contentPane.add(lblSubtitulo);

		// --- Filtro de nivel ---
		JLabel lblFiltro = new JLabel("Filtrar por nivel:");
		lblFiltro.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		lblFiltro.setForeground(Color.BLACK);
		lblFiltro.setBounds(30, 150, 150, 25);
		contentPane.add(lblFiltro);

		comboNivel = new JComboBox<>(new String[] { "Todos", "0", "1", "2", "3" });
		comboNivel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		comboNivel.setBackground(Color.WHITE);
		comboNivel.setForeground(Color.BLACK);
		comboNivel.setBounds(160, 150, 100, 28);
		comboNivel.addActionListener(e -> filtrarPorNivel());
		contentPane.add(comboNivel);

		// --- Botón histórico ---
		JButton btnHistorico = new JButton("Ver histórico");
		btnHistorico.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnHistorico.setFocusPainted(false);
		btnHistorico.setBackground(new Color(200, 0, 0));
		btnHistorico.setForeground(Color.WHITE);
		btnHistorico.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		btnHistorico.setBounds(738, 163, 160, 30);

		btnHistorico.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnHistorico.setBackground(new Color(255, 30, 30));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				btnHistorico.setBackground(new Color(200, 0, 0));
			}
		});
		btnHistorico.addActionListener(e -> {
			dispose();
			VentanaHistoricoWK historicoWK = new VentanaHistoricoWK(usuarioActual);
			historicoWK.setVisible(true);
		});
		contentPane.add(btnHistorico);

		// --- TABLA WORKOUTS ---
		modelo = new DefaultTableModel(new Object[][] {},
				new String[] { "Nombre", "Nº Ejercicios", "Nivel", "Ver Video" }) {
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int row, int column) {
				return column == 3;
			}
		};

		table = new JTable(modelo);
		table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		table.setRowHeight(30);
		table.setBackground(Color.WHITE);
		table.setForeground(Color.BLACK);
		table.setSelectionBackground(new Color(255, 70, 70));
		table.setSelectionForeground(Color.WHITE);
		table.setGridColor(new Color(200, 0, 0));

		JTableHeader header1 = table.getTableHeader();
		header1.setFont(new Font("Segoe UI", Font.BOLD, 15));
		header1.setBackground(Color.BLACK);
		header1.setForeground(Color.WHITE);

		table.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer());
		table.getColumnModel().getColumn(3).setCellEditor(new ButtonEditor(new JCheckBox()));

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(33, 220, 420, 380);
		contentPane.add(scrollPane);

		// --- TABLA EJERCICIOS ---
		modelo2 = new DefaultTableModel(new Object[][] {},
				new String[] { "Nombre", "Descripción", "Nivel", "Descanso (s)" }) {
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int row, int column) { return false; }
		};

		table_1 = new JTable(modelo2);
		table_1.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		table_1.setRowHeight(28);
		table_1.setBackground(Color.WHITE);
		table_1.setForeground(Color.BLACK);
		table_1.setSelectionBackground(new Color(255, 70, 70));
		table_1.setSelectionForeground(Color.WHITE);
		table_1.setGridColor(new Color(200, 0, 0));

		JTableHeader header2 = table_1.getTableHeader();
		header2.setFont(new Font("Segoe UI", Font.BOLD, 15));
		header2.setBackground(Color.BLACK);
		header2.setForeground(Color.WHITE);

		JScrollPane scrollPane_1 = new JScrollPane(table_1);
		scrollPane_1.setBounds(491, 220, 420, 380);
		contentPane.add(scrollPane_1);

		// Etiquetas
		JLabel lblWorkouts = new JLabel("WORKOUTS");
		lblWorkouts.setHorizontalAlignment(SwingConstants.CENTER);
		lblWorkouts.setFont(new Font("Segoe UI", Font.BOLD, 16));
		lblWorkouts.setForeground(new Color(200, 0, 0));
		lblWorkouts.setBounds(160, 191, 140, 25);
		contentPane.add(lblWorkouts);

		JLabel lblEjercicios = new JLabel("EJERCICIOS");
		lblEjercicios.setHorizontalAlignment(SwingConstants.CENTER);
		lblEjercicios.setFont(new Font("Segoe UI", Font.BOLD, 16));
		lblEjercicios.setForeground(new Color(200, 0, 0));
		lblEjercicios.setBounds(640, 191, 140, 25);
		contentPane.add(lblEjercicios);
		

		try {
			int nivelUsuario = usuarioActual.getNivel();
			listaWorkouts = controlador.leerWorkoutsBD(nivelUsuario);
			mostrarWorkouts(listaWorkouts);
		} catch (IOException | InterruptedException | ExecutionException e1) {
			e1.printStackTrace();
		}

		table.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				int fila = table.getSelectedRow();
				if (fila >= 0 && fila < listaMostrada.size()) {
					// store selected workout
					workoutSeleccionado = listaMostrada.get(fila);
					mostrarEjercicios(listaMostrada.get(fila).getEjercicios());
				}
			}
		});
		
		// When the user clicks an exercise row, open VentanaEjercicio for the selected workout
		table_1.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				int fila = table_1.getSelectedRow();
				if (fila >= 0 && workoutSeleccionado != null) {
					// Open the exercise window for the specific Ejercicios clicked
					Ejercicios ejerc = null;
					if (workoutSeleccionado.getEjercicios() != null && fila < workoutSeleccionado.getEjercicios().size()) {
						ejerc = workoutSeleccionado.getEjercicios().get(fila);
					}
					if (ejerc != null) {
						dispose();
						VentanaEjercicio ve = new VentanaEjercicio(ejerc, workoutSeleccionado);
						ve.setVisible(true);
					}
				}
			}
		});
	}

	private void mostrarWorkouts(ArrayList<Workout> workouts) {
		modelo.setRowCount(0);
		listaMostrada = workouts;
		for (Workout w : workouts) {
			modelo.addRow(new Object[] { w.getNombre(), w.getNumEjers(), w.getNivel(), w.getVideo() });
		}
	}

	private void mostrarEjercicios(ArrayList<Ejercicios> ejercicios) {
		modelo2.setRowCount(0);
		if (ejercicios != null) {
			for (Ejercicios e : ejercicios) {
				modelo2.addRow(new Object[] { e.getNombre(), e.getDescripcion(), e.getNivel(), e.getTiempoDescanso() });
			}
		}
	}

	private void filtrarPorNivel() {
		String seleccionado = (String) comboNivel.getSelectedItem();
		if (seleccionado.equals("Todos")) {
			mostrarWorkouts(listaWorkouts);
		} else {
			int nivel = Integer.parseInt(seleccionado);
			ArrayList<Workout> filtrados = new ArrayList<>();
			for (Workout w : listaWorkouts) {
				if (w.getNivel() == nivel) filtrados.add(w);
			}
			mostrarWorkouts(filtrados);
		}
	}

	// --- Renderizador y Editor de botones ---
	class ButtonRenderer extends JButton implements TableCellRenderer {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public ButtonRenderer() {
			setOpaque(true);
			setBackground(new Color(200, 0, 0));
			setForeground(Color.WHITE);
			setFont(new Font("Segoe UI", Font.BOLD, 13));
			setFocusPainted(false);
			setBorder(BorderFactory.createLineBorder(Color.BLACK));
			setText("Ver Video");
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			if (isSelected) setBackground(new Color(255, 40, 40));
			else setBackground(new Color(200, 0, 0));
			return this;
		}
	}

	class ButtonEditor extends DefaultCellEditor {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private JButton button;
		private String url;

		public ButtonEditor(JCheckBox checkBox) {
			super(checkBox);
			button = new JButton("Ver Video");
			button.setFont(new Font("Segoe UI", Font.BOLD, 13));
			button.setBackground(new Color(200, 0, 0));
			button.setForeground(Color.WHITE);
			button.setFocusPainted(false);
			button.setBorder(BorderFactory.createLineBorder(Color.BLACK));

			button.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) { button.setBackground(new Color(255, 40, 40)); }
				@Override
				public void mouseExited(MouseEvent e) { button.setBackground(new Color(200, 0, 0)); }
			});

			button.addActionListener(e -> {
				if (url != null && !url.trim().isEmpty()) {
					try {
						java.awt.Desktop.getDesktop().browse(new java.net.URI(url));
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, "No se pudo abrir el enlace del video.",
								"Error", JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(null, "Este workout no tiene video asociado.",
							"Aviso", JOptionPane.INFORMATION_MESSAGE);
				}
				fireEditingStopped();
			});
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
				int column) {
			url = (String) value;
			return button;
		}

		@Override
		public Object getCellEditorValue() {
			return url;
		}
	}
}
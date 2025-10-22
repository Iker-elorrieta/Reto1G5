package vista;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import controlador.Controlador;
import modelo.Ejercicios;
import modelo.Usuarios;
import modelo.Workout;
import java.io.IOException;

public class VentanaWorkouts extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;
	private JComboBox<String> comboNivel;
	private DefaultTableModel modelo;
	private DefaultTableModel modelo2;
	private ArrayList<Workout> listaWorkouts;
	private ArrayList<Workout> listaMostrada;
	private JTable table_1;
	private Controlador controlador;
	private Usuarios usuarioActual;
	



	public VentanaWorkouts(Usuarios usuario) {
		controlador = new Controlador();
		usuarioActual=usuario;
		setTitle("Workouts");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 937, 623);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblTitulo = new JLabel("Workouts disponibles");
		lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 22));
		lblTitulo.setBounds(30, 20, 400, 40);
		contentPane.add(lblTitulo);

		JLabel lblFiltro = new JLabel("Filtrar por nivel:");
		lblFiltro.setBounds(30, 80, 120, 25);
		contentPane.add(lblFiltro);

		comboNivel = new JComboBox<>(new String[] { "Todos", "0", "1", "2", "3" });
		comboNivel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				filtrarPorNivel();
			}
		});
		comboNivel.setBounds(150, 80, 100, 25);
		contentPane.add(comboNivel);

		JButton btnHistorico = new JButton("Ver histórico");
		btnHistorico.setBounds(750, 80, 140, 25);
		contentPane.add(btnHistorico);
		btnHistorico.addActionListener(e -> {
			VentanaHistoricoWK historicoWK = new VentanaHistoricoWK(usuarioActual);
			historicoWK.setVisible(true);
		});

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(30, 155, 406, 400);
		contentPane.add(scrollPane);

		modelo = new DefaultTableModel(new Object[][] {},
				new String[] { "Nombre", "Nº Ejercicios", "Nivel", "Video (URL)" }) {
			/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		table = new JTable(modelo);
		table.getTableHeader().setReorderingAllowed(false);
		scrollPane.setViewportView(table);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(484, 155, 406, 400);
		contentPane.add(scrollPane_1);

		modelo2 = new DefaultTableModel(new Object[][] {}, 
				new String[] { "Nombre", "Descripción", "Nivel", "Descanso (s)" }) {
			/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table_1 = new JTable(modelo2);
		table_1.getTableHeader().setReorderingAllowed(false);
		scrollPane_1.setViewportView(table_1);
		
		JLabel lblNewLabel = new JLabel("WORKOUTS");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(160, 116, 100, 28);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("EJERCICIOS");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(649, 116, 90, 28);
		contentPane.add(lblNewLabel_1);

		// Simular datos (esto luego lo cambiamos por los de la BD)
		try {
			
			int nivelUsuario = usuarioActual.getNivel();
			
		listaWorkouts = controlador.leerWorkoutsBD(nivelUsuario);
		mostrarWorkouts(listaWorkouts);
		} catch (IOException | InterruptedException | ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// --- Escuchar selección en la tabla ---
		table.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				int fila = table.getSelectedRow();
				if (fila >= 0) {
					Workout seleccionado = listaMostrada.get(fila);
					mostrarEjercicios(seleccionado.getEjercicios());
				}
			}
		});
	}

	// -------------------------
	// MÉTODOS AUXILIARES
	// -------------------------
//	private void cargarWorkoutsSimulados() {
//	    listaWorkouts = new ArrayList<>();
//
//	    // Workout 1
//	    Workout w1 = new Workout("Full Body Beginner", "https://youtu.be/xyz1", 0, 3);
//	    ArrayList<Ejercicios> ej1 = new ArrayList<>();
//	    ej1.add(new Ejercicios("Sentadillas", "Ejercicio básico para piernas", "sentadillas.png", 0, 30, null));
//	    ej1.add(new Ejercicios("Flexiones", "Fortalece pecho y brazos", "flexiones.png", 0, 30, null));
//	    ej1.add(new Ejercicios("Plancha", "Trabajo de core", "plancha.png", 0, 30, null));
//	    w1.setEjercicios(ej1);
//
//	    // Workout 2
//	    Workout w2 = new Workout("Cardio Express", "https://youtu.be/xyz2", 1, 2);
//	    ArrayList<Ejercicios> ej2 = new ArrayList<>();
//	    ej2.add(new Ejercicios("Jumping Jacks", "Cardio intenso", "jumping.png", 1, 20, null));
//	    ej2.add(new Ejercicios("Burpees", "Ejercicio completo", "burpees.png", 1, 30, null));
//	    w2.setEjercicios(ej2);
//
//	    listaWorkouts.add(w1);
//	    listaWorkouts.add(w2);
//	}

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
				modelo2.addRow(new Object[] { 
					e.getNombre(), 
					e.getDescripcion(), 
					e.getNivel(), 
					e.getTiempoDescanso() 
				});
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
				if (w.getNivel() == nivel) {
					filtrados.add(w);
				}
			}
			mostrarWorkouts(filtrados);
		}
	}
}

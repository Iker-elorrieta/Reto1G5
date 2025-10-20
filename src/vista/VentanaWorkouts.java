package vista;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import modelo.Workout;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

public class VentanaWorkouts extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;
	private JComboBox<String> comboNivel;
	private DefaultTableModel modelo;
	private DefaultTableModel modelo2;
	private ArrayList<Workout> listaWorkouts; // lista simulada de workouts
	private JTable table_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				VentanaWorkouts frame = new VentanaWorkouts();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public VentanaWorkouts() {
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
		btnHistorico.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(VentanaWorkouts.this,
						"Aquí se abriría la pantalla del histórico de workouts");
			}
		});

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(30, 120, 406, 400);
		contentPane.add(scrollPane);

		modelo = new DefaultTableModel(new Object[][] {},
				new String[] { "Nombre", "Nº Ejercicios", "Nivel", "Video (URL)" }) {
			public boolean isCellEditable(int row, int column) {
				return false; // No editable
			}
		};

		table = new JTable(modelo);
		table.getTableHeader().setReorderingAllowed(false); // ← evita mover columnas
		scrollPane.setViewportView(table);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(484, 120, 406, 400);
		contentPane.add(scrollPane_1);
		modelo2 = new DefaultTableModel(new Object[][] {}, new String[] { "Ejercicios" }) {
			public boolean isCellEditable(int row, int column) {
				return false; // No editable
			}
		};
		table_1 = new JTable(modelo2);
		table_1.getTableHeader().setReorderingAllowed(false); // ← evita mover columnas
		scrollPane_1.setViewportView(table_1);

		// Simular datos (esto luego lo cambiamos por los de la BD)
		cargarWorkoutsSimulados();
		mostrarWorkouts(listaWorkouts);

	}

	private void cargarWorkoutsSimulados() {
		listaWorkouts = new ArrayList<>();
		listaWorkouts.add(new Workout("Full Body Beginner", "https://youtu.be/xyz1", 0, 5));
		listaWorkouts.add(new Workout("Cardio Express", "https://youtu.be/xyz2", 1, 4));
		listaWorkouts.add(new Workout("Fuerza Nivel 1", "https://youtu.be/xyz3", 1, 6));
		listaWorkouts.add(new Workout("HIIT Avanzado", "https://youtu.be/xyz4", 2, 8));
		listaWorkouts.add(new Workout("Resistencia Total", "https://youtu.be/xyz5", 3, 10));
	}

	private void mostrarWorkouts(ArrayList<Workout> workouts) {
		modelo.setRowCount(0); // limpiar tabla
		for (Workout w : workouts) {
			modelo.addRow(new Object[] { w.getNombre(), w.getNumEjers(), w.getNivel(), w.getVideo() });
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

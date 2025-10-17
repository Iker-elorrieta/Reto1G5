package vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controlador.ControladorUsuarios;
import modelo.Usuarios;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;

public class VentanaRegistro extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtNombre;
	private JTextField txtEmail;
	private JTextField txtApellido;
	private JTextField txtFecNac;
	private JLabel lblNombre;
	private JLabel lblApellido;
	private JLabel lblEmail;
	private JLabel lblFecNac;
	private JLabel lblContra;
	private JButton btnRegistro;
	private JPasswordField txtContra;
	private ControladorUsuarios controlador;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaRegistro frame = new VentanaRegistro();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public VentanaRegistro() {
		controlador = new ControladorUsuarios();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 852, 597);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		lblNombre = new JLabel("Nombre");
		lblNombre.setBounds(89, 131, 98, 14);
		contentPane.add(lblNombre);

		lblApellido = new JLabel("Apellido");
		lblApellido.setBounds(394, 131, 182, 14);
		contentPane.add(lblApellido);

		lblEmail = new JLabel("Email");
		lblEmail.setBounds(89, 200, 113, 14);
		contentPane.add(lblEmail);

		lblFecNac = new JLabel("FecNac");
		lblFecNac.setBounds(394, 200, 142, 14);
		contentPane.add(lblFecNac);

		btnRegistro = new JButton("Registro");
		btnRegistro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {

					if (txtNombre.getText().equals("") || txtApellido.getText().equals("")
							|| txtFecNac.getText().equals("") || txtEmail.getText().equals("")
							|| txtContra.getPassword().equals("")) {
						JOptionPane.showMessageDialog(null, "Error al registrar usuario", "Error",
								JOptionPane.ERROR_MESSAGE);
					} else if (!esFechaValida(txtFecNac.getText())) {
						JOptionPane.showMessageDialog(null, "Formato de fecha incorrecto. Usa dd/MM/yyyy", "Error",
								JOptionPane.ERROR_MESSAGE);
					}

					else {
						Usuarios usuario = añadirUsuario();
						controlador.registrarUsuario(usuario);
						JOptionPane.showMessageDialog(null, "Usuario registrado correctamente");
					}
				} catch (Exception ex) {
					// TODO Auto-generated catch block
					ex.printStackTrace();
				}

			}
		});
		btnRegistro.setBounds(286, 415, 89, 23);
		contentPane.add(btnRegistro);

		txtNombre = new JTextField();
		txtNombre.setBounds(157, 131, 86, 20);
		contentPane.add(txtNombre);
		txtNombre.setColumns(10);

		txtEmail = new JTextField();
		txtEmail.setColumns(10);
		txtEmail.setBounds(157, 197, 86, 20);
		contentPane.add(txtEmail);

		txtApellido = new JTextField();
		txtApellido.setColumns(10);
		txtApellido.setBounds(467, 128, 86, 20);
		contentPane.add(txtApellido);

		txtFecNac = new JTextField();
		txtFecNac.setColumns(10);
		txtFecNac.setBounds(467, 197, 86, 20);
		contentPane.add(txtFecNac);

		txtContra = new JPasswordField();
		txtContra.setBounds(296, 304, 153, 20);
		contentPane.add(txtContra);

		lblContra = new JLabel("Contraseña");
		lblContra.setBounds(207, 307, 79, 14);
		contentPane.add(lblContra);

	}

	public Usuarios añadirUsuario() {
		Usuarios usuario = new Usuarios();

		try {

			Date fecha = new SimpleDateFormat("dd/MM/yyyy").parse(txtFecNac.getText());

			usuario.setNombre(txtNombre.getText());
			usuario.setApellido(txtApellido.getText());
			usuario.setFecNac(fecha);
			usuario.setEmail(txtEmail.getText());
			usuario.setContraseña(new String(txtContra.getPassword()));

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return usuario;
	}

	private boolean esFechaValida(String fechaStr) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		sdf.setLenient(false); // No permite fechas como 32/13/2025
		try {
			sdf.parse(fechaStr);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}

}

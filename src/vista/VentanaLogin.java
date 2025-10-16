package vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controlador.ControladorUsuarios;
import modelo.Usuarios;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.awt.event.ActionEvent;
import vista.VentanaRegistro;
import javax.swing.JPasswordField;

public class VentanaLogin extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtUsuario;
	private JPasswordField passwordField;
	private ControladorUsuarios controlador;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaLogin frame = new VentanaLogin();
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
	public VentanaLogin() {
		controlador = new ControladorUsuarios();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 705, 471);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblUsuario = new JLabel("Usuario:");
		lblUsuario.setBounds(186, 136, 46, 14);
		contentPane.add(lblUsuario);

		JLabel lblContraseña = new JLabel("Contraseña:");
		lblContraseña.setBounds(186, 186, 76, 14);
		contentPane.add(lblContraseña);

		txtUsuario = new JTextField();
		txtUsuario.setToolTipText("");
		txtUsuario.setBounds(285, 133, 143, 20);
		contentPane.add(txtUsuario);
		txtUsuario.setColumns(10);

		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Usuarios usuarioCompleto = new Usuarios();
				usuarioCompleto = comprobarDatos();
				String usurario = txtUsuario.getText().trim();
				char[] contraseña = passwordField.getPassword();
				if (usurario.isEmpty() || contraseña.length == 0) {
					JOptionPane.showMessageDialog(null, "Campos vacios", "Error", JOptionPane.ERROR_MESSAGE);
				} else {
					try {
						System.out.println(usuarioCompleto.getNombre());
						controlador.login(usuarioCompleto);
					} catch (IOException | InterruptedException | ExecutionException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

			}
		});
		btnLogin.setBounds(186, 237, 89, 23);
		contentPane.add(btnLogin);

		JButton btnRegistro = new JButton("Registrarse");
		btnRegistro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VentanaRegistro registro = new VentanaRegistro();
				registro.setVisible(true);
			}
		});
		btnRegistro.setBounds(336, 237, 89, 23);
		contentPane.add(btnRegistro);

		passwordField = new JPasswordField();
		passwordField.setBounds(285, 183, 143, 20);
		contentPane.add(passwordField);

	}

	public Usuarios comprobarDatos() {
		Usuarios usuario = new Usuarios();

		usuario.setNombre(txtUsuario.getText());
		usuario.setContraseña(new String(passwordField.getPassword()));

		return usuario;

	}
}

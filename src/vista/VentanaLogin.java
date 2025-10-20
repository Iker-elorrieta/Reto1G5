package vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controlador.Controlador;
import modelo.Usuarios;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.awt.event.ActionEvent;
import vista.VentanaRegistro;
import javax.swing.JPasswordField;
import java.awt.Font;

public class VentanaLogin extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtUsuario;
	private JPasswordField passwordField;
	private Controlador controlador;

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
		controlador = new Controlador();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 705, 471);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblUsuario = new JLabel("Usuario:");
		lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 30));
		lblUsuario.setBounds(127, 101, 148, 36);
		contentPane.add(lblUsuario);

		JLabel lblContraseña = new JLabel("Contraseña:");
		lblContraseña.setFont(new Font("Segoe UI", Font.PLAIN, 30));
		lblContraseña.setBounds(127, 173, 188, 36);
		contentPane.add(lblContraseña);

		txtUsuario = new JTextField();
		txtUsuario.setToolTipText("");
		txtUsuario.setBounds(331, 101, 199, 37);
		contentPane.add(txtUsuario);
		txtUsuario.setColumns(10);

		passwordField = new JPasswordField();
		passwordField.setBounds(331, 173, 199, 37);
		contentPane.add(passwordField);

		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				String usuario = txtUsuario.getText().trim();
				String contraseña = new String(passwordField.getPassword());
				if (usuario.isEmpty() || contraseña.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Campos vacios", "Error", JOptionPane.ERROR_MESSAGE);
				} else {
					try {
						
						boolean exito = controlador.login(usuario, contraseña);
						if(exito) {
							dispose();
							VentanaWorkouts workout = new VentanaWorkouts();
							workout.setVisible(true);
						}else {
							JOptionPane.showMessageDialog(null, "todo mal", "Error", JOptionPane.ERROR_MESSAGE);

						}
					} catch (IOException | InterruptedException | ExecutionException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}

			}
		});
		btnLogin.setBounds(127, 299, 199, 52);
		contentPane.add(btnLogin);

		JButton btnRegistro = new JButton("Registrarse");
		btnRegistro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				VentanaRegistro registro = new VentanaRegistro();
				registro.setVisible(true);
			}
		});
		btnRegistro.setBounds(371, 299, 199, 52);
		contentPane.add(btnRegistro);

	}

}

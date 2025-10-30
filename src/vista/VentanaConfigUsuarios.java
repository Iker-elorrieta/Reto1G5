package vista;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

import controlador.Controlador;
import modelo.Usuarios;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.event.ActionEvent;

public class VentanaConfigUsuarios extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Controlador controlador;
	private Usuarios usuarioActual;
	private JTextField txtNombre;
	private JTextField txtApellido;
	private JTextField txtCorreo;
	private JPasswordField txtContra;
	private JTextField txtFecNac;
	private JLabel lblImagen;
	private JLabel lblNombreUsu;
	private JLabel lblBienvenida;
	private JButton btnGuardar;
	private JButton btnAtras;
	private JLabel lblNombre;
	private JLabel lblApellido;
	private JLabel lblEmail;
	private JLabel lblContrasena;
	private JLabel lblFecNac;
	private JLabel lblNewLabel;
	private JButton btnTick;
	private JButton btnX;

	public VentanaConfigUsuarios(Usuarios usu) {
		usuarioActual = usu;
		controlador = new Controlador();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 774, 584);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		btnGuardar = new JButton("Guardar");
		btnGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				guardarCambios();
			}
		});
		btnGuardar.setBounds(233, 454, 112, 23);
		contentPane.add(btnGuardar);
		
		btnAtras = new JButton("Volver atras");
		btnAtras.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnAtras.setBounds(436, 454, 118, 23);
		contentPane.add(btnAtras);
		
		lblNombre = new JLabel("Nombre");
		lblNombre.setBounds(214, 207, 46, 14);
		contentPane.add(lblNombre);
		
		lblApellido = new JLabel("Apellido");
		lblApellido.setBounds(214, 255, 46, 14);
		contentPane.add(lblApellido);
		
		lblEmail = new JLabel("Email");
		lblEmail.setBounds(214, 301, 46, 14);
		contentPane.add(lblEmail);
		
		lblContrasena = new JLabel("Contraseña");
		lblContrasena.setBounds(214, 346, 74, 14);
		contentPane.add(lblContrasena);
		
		lblFecNac = new JLabel("FecNac");
		lblFecNac.setBounds(214, 392, 46, 14);
		contentPane.add(lblFecNac);
		
		txtNombre = new JTextField();
		txtNombre.setBounds(354, 204, 134, 20);
		contentPane.add(txtNombre);
		txtNombre.setColumns(10);
		txtNombre.setEditable(false);
		
		txtApellido = new JTextField();
		txtApellido.setColumns(10);
		txtApellido.setBounds(354, 252, 134, 20);
		contentPane.add(txtApellido);
		txtApellido.setEditable(false);
		
		txtCorreo = new JTextField();
		txtCorreo.setColumns(10);
		txtCorreo.setBounds(354, 298, 134, 20);
		contentPane.add(txtCorreo);
		txtCorreo.setEditable(false);
		
		txtContra = new JPasswordField();
		txtContra.setColumns(10);
		txtContra.setBounds(354, 343, 134, 20);
		contentPane.add(txtContra);
		txtContra.setEditable(false);
		
		txtFecNac = new JTextField();
		txtFecNac.setColumns(10);
		txtFecNac.setBounds(354, 389, 134, 20);
		contentPane.add(txtFecNac);
		txtFecNac.setEditable(false);
		
		lblImagen = new JLabel("");
		lblImagen.setBounds(28, 22, 185, 181);
		ImageIcon icono = new ImageIcon("iconoSF.png" );
		if (icono.getImage() != null) { // verificamos que no sea null
		    Image imagen = icono.getImage().getScaledInstance(lblImagen.getWidth(), lblImagen.getHeight(), Image.SCALE_SMOOTH);
		    lblImagen.setIcon(new ImageIcon(imagen));
		} else {
		    System.out.println("Imagen no encontrada");
		}
		contentPane.add(lblImagen);
		
		lblBienvenida = new JLabel("BIENVENIDO");
		lblBienvenida.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblBienvenida.setBounds(271, 11, 118, 54);
		contentPane.add(lblBienvenida);
		
		lblNombreUsu = new JLabel("");
		lblNombreUsu.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNombreUsu.setBounds(399, 11, 66, 54);
		lblNombreUsu.setText(usu.getNombre());
		contentPane.add(lblNombreUsu);
		
		lblNewLabel = new JLabel("DESEA REALIZAR ALGUNA MODIFICACIÓN?");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel.setBounds(238, 78, 361, 39);
		contentPane.add(lblNewLabel);
		
		
		//BOTON TICK
		btnTick = new JButton("");
		btnTick.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				habilitarEdicion();
			}
		});
		btnTick.setBounds(21, 363, 160, 171);
		ImageIcon icono2 = new ImageIcon("check.png" );
		if (icono2.getImage() != null) { // verificamos que no sea null
		    Image imagen = icono2.getImage().getScaledInstance(lblImagen.getWidth(), lblImagen.getHeight(), Image.SCALE_SMOOTH);
		    btnTick.setIcon(new ImageIcon(imagen));
		} else {
		    System.out.println("Imagen no encontrada");
		}
		contentPane.add(btnTick);
		
		btnTick.setBorderPainted(false);
		btnTick.setFocusPainted(false);
		btnTick.setContentAreaFilled(false);
		btnTick.setOpaque(false);
		//BOTON X
		btnX = new JButton("");
		btnX.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deshabilitarEdicion();
			}
		});
		btnX.setBounds(582, 374, 185, 160);
		ImageIcon icono3 = new ImageIcon("cruz.png" );
		if (icono3.getImage() != null) { // verificamos que no sea null
		    Image imagen = icono3.getImage().getScaledInstance(lblImagen.getWidth(), lblImagen.getHeight(), Image.SCALE_SMOOTH);
		    btnX.setIcon(new ImageIcon(imagen));
		} else {
		    System.out.println("Imagen no encontrada");
		}
		contentPane.add(btnX);
		
		btnX.setBorderPainted(false);
		btnX.setFocusPainted(false);
		btnX.setContentAreaFilled(false);
		btnX.setOpaque(false);
	}
	
	private void habilitarEdicion() {
		txtNombre.setEditable(true);
		txtApellido.setEditable(true);
		txtCorreo.setEditable(true);
		txtContra.setEditable(true);
		txtFecNac.setEditable(true);
	}
	
	private void deshabilitarEdicion() {
		txtNombre.setEditable(false);
		txtApellido.setEditable(false);
		txtCorreo.setEditable(false);
		txtContra.setEditable(false);
		txtFecNac.setEditable(false);
	}
	
	
	private void guardarCambios() {

	    String nuevoNombre = txtNombre.getText();
	    String nuevoApellido = txtApellido.getText();
	    String nuevoEmail = txtCorreo.getText();
	    String nuevaContrasena = new String(txtContra.getPassword());
	    String nuevaFecha = txtFecNac.getText(); 

	    if (nuevoNombre.isEmpty() || nuevoApellido.isEmpty() || nuevaContrasena.isEmpty()) {
	        JOptionPane.showMessageDialog(this, "Por favor, completa todos los campos obligatorios.");
	        return;
	    }

	    try {
	        
	    	SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
	        Date fecha = formato.parse(nuevaFecha);
	    	
	        usuarioActual.setNombre(nuevoNombre);
	        usuarioActual.setApellido(nuevoApellido);
	        usuarioActual.setEmail(nuevoEmail);
	        usuarioActual.setContrasena(nuevaContrasena);
	        usuarioActual.setFecNac(fecha);

	        controlador.actualizarUsuario(usuarioActual);

	        JOptionPane.showMessageDialog(this, "Datos actualizados correctamente.");

	    } catch (Exception ex) {
	        ex.printStackTrace();
	        JOptionPane.showMessageDialog(this, "Error al actualizar los datos.");
	    }
	}
	
	
}

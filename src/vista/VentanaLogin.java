package vista;

import java.awt.Color;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import controlador.Controlador;
import modelo.Usuarios;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;
import java.awt.Font;
import java.awt.Image;

public class VentanaLogin extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtUsuario;
    private JPasswordField passwordField;
    private Controlador controlador;

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

    public VentanaLogin() {
        controlador = new Controlador();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 705, 471);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setFont(new Font("Tahoma", Font.BOLD, 25));
        lblUsuario.setBounds(246, 102, 148, 36);
        contentPane.add(lblUsuario);

        JLabel lblContraseña = new JLabel("Contraseña:");
        lblContraseña.setFont(new Font("Tahoma", Font.BOLD, 25));
        lblContraseña.setBounds(246, 173, 188, 36);
        contentPane.add(lblContraseña);

        txtUsuario = new JTextField();
        txtUsuario.setBounds(464, 101, 199, 37);
        contentPane.add(txtUsuario);
        txtUsuario.setColumns(10);

        passwordField = new JPasswordField();
        passwordField.setBounds(464, 172, 199, 37);
        contentPane.add(passwordField);

        JButton btnLogin = new JButton("Login");
        btnLogin.setBackground(Color.RED);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnLogin.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String usuario = txtUsuario.getText().trim();
                String contrasena = new String(passwordField.getPassword());
                if (usuario.isEmpty() || contrasena.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Campos vacios", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    boolean exito = controlador.login(usuario, contrasena);
                    if (exito) {

                        // --- Lanzar backup en segundo plano ---
                        try {
                            ProcessBuilder pb = new ProcessBuilder("java", "-jar", "backups.jar");
                            final Process process = pb.start();

                            javax.swing.Timer timer = new javax.swing.Timer(500, new java.awt.event.ActionListener() {
                                @Override
                                public void actionPerformed(java.awt.event.ActionEvent evt) {
                                    try {
                                        int exitCode = process.exitValue(); // excepción si sigue corriendo
                                        ((javax.swing.Timer) evt.getSource()).stop(); // parar timer

                                        if (exitCode == 0) {
                                            JOptionPane.showMessageDialog(
                                                    null,
                                                    "Backup finalizado correctamente",
                                                    "Backup",
                                                    JOptionPane.INFORMATION_MESSAGE
                                            );
                                        } else {
                                            JOptionPane.showMessageDialog(
                                                    null,
                                                    "El backup terminó con errores",
                                                    "Backup",
                                                    JOptionPane.ERROR_MESSAGE
                                            );
                                        }

                                    } catch (IllegalThreadStateException ex) {
                                        // sigue corriendo, no hacemos nada
                                    }
                                }
                            });

                            timer.start();

                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                        // --- Obtener usuario y abrir ventana principal ---
                        Usuarios usuarioObtener = controlador.obtenerUsuario(usuario, contrasena);
                        if (usuarioObtener == null) {
                            JOptionPane.showMessageDialog(null, "Error al obtener datos del usuario", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        dispose();
                        VentanaWorkouts workout = new VentanaWorkouts(usuarioObtener);
                        workout.setVisible(true);

                    } else {
                        JOptionPane.showMessageDialog(null, "Usuario y/o contraseña incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        });
        btnLogin.setBounds(207, 299, 199, 52);
        contentPane.add(btnLogin);

        JButton btnRegistro = new JButton("Registrarse");
        btnRegistro.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                VentanaRegistro registro = new VentanaRegistro();
                registro.setVisible(true);
            }
        });
        btnRegistro.setBounds(440, 299, 199, 52);
        btnRegistro.setBackground(Color.RED);
        btnRegistro.setForeground(Color.WHITE);
        btnRegistro.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnRegistro.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        contentPane.add(btnRegistro);

        JLabel lblIMG = new JLabel("");
        lblIMG.setBounds(10, 11, 219, 218);
        ImageIcon icono = new ImageIcon("iconoSF.png");
        if (icono.getImage() != null) {
            Image imagen = icono.getImage().getScaledInstance(lblIMG.getWidth(), lblIMG.getHeight(), Image.SCALE_SMOOTH);
            lblIMG.setIcon(new ImageIcon(imagen));
        } else {
            System.out.println("Imagen no encontrada");
        }
        contentPane.add(lblIMG);
    }
}

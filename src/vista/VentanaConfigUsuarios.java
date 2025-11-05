package vista;

import controlador.Controlador;
import modelo.Usuarios;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;

public class VentanaConfigUsuarios extends JFrame {

    private static final long serialVersionUID = 1L;
    private Controlador controlador;
    private Usuarios usuarioActual;
    private JTextField txtNombre, txtApellido, txtCorreo, txtFecNac;
    private JPasswordField txtContra;

    public VentanaConfigUsuarios(Usuarios usu) {
        usuarioActual = usu;
        controlador = new Controlador();

        setTitle("Configuración de Usuario");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // 🎨 Fondo degradado
        JPanel bgPanel = new JPanel() {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0,
                        new Color(250, 250, 250),
                        0, getHeight(),
                        new Color(230, 230, 230));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        bgPanel.setLayout(new BorderLayout());
        setContentPane(bgPanel);

        // ✅ Header
        JLabel lblTitulo = new JLabel("Configuración de Usuario", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 28));
        lblTitulo.setForeground(new Color(177, 0, 0));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 20, 0));

        // ✅ Panel tipo tarjeta
        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(177, 0, 0), 2),
                BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        card.setLayout(new GridLayout(6, 2, 18, 18));
        card.setPreferredSize(new Dimension(480, 380));

        // Campos
        txtNombre = createField(usuarioActual.getNombre());
        txtApellido = createField(usuarioActual.getApellido());
        txtCorreo = createField(usuarioActual.getEmail());
        txtContra = new JPasswordField(usuarioActual.getContrasena());
        txtContra.setEditable(false);
        txtContra.setFont(new Font("Tahoma", Font.PLAIN, 15));
        txtContra.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        txtFecNac = createField(sdf.format(usuarioActual.getFecNac()));

        addRow(card, "Nombre:", txtNombre);
        addRow(card, "Apellido:", txtApellido);
        addRow(card, "Email:", txtCorreo);
        addRow(card, "Contraseña:", txtContra);
        addRow(card, "Fecha Nacimiento:", txtFecNac);

        // ✅ Botones
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setOpaque(false);

        JButton btnEditar = createBtn("Editar");
        btnEditar.addActionListener(e -> activarEdicion(true));

        JButton btnGuardar = createBtn("Guardar");
        btnGuardar.addActionListener(e -> guardarCambios());

        JButton btnVolver = createBtn("Volver");
        btnVolver.addActionListener(e -> dispose());

        buttonPanel.add(btnEditar);
        buttonPanel.add(btnGuardar);
        buttonPanel.add(btnVolver);

        // ✅ Estructura final
        JPanel centerWrapper = new JPanel();
        centerWrapper.setOpaque(false);
        centerWrapper.add(card);

        bgPanel.add(lblTitulo, BorderLayout.NORTH);
        bgPanel.add(centerWrapper, BorderLayout.CENTER);
        bgPanel.add(buttonPanel, BorderLayout.SOUTH);

        activarEdicion(false);
        setVisible(true);
    }

    private void addRow(JPanel panel, String label, JComponent field) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Tahoma", Font.BOLD, 16));
        lbl.setForeground(new Color(60, 60, 60));
        panel.add(lbl);
        panel.add(field);
    }

    private JTextField createField(String text) {
        JTextField tf = new JTextField(text);
        tf.setEditable(false);
        tf.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        tf.setFont(new Font("Tahoma", Font.PLAIN, 15));
        return tf;
    }

    private JButton createBtn(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Tahoma", Font.BOLD, 15));
        btn.setBackground(new Color(177, 0, 0));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        return btn;
    }

    private void activarEdicion(boolean on) {
        txtNombre.setEditable(on);
        txtApellido.setEditable(on);
        txtCorreo.setEditable(on);
        txtContra.setEditable(on);
        txtFecNac.setEditable(on);
    }

    private void guardarCambios() {
        try {
            usuarioActual.setNombre(txtNombre.getText());
            usuarioActual.setApellido(txtApellido.getText());
            usuarioActual.setEmail(txtCorreo.getText());
            usuarioActual.setContrasena(new String(txtContra.getPassword()));

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            usuarioActual.setFecNac(sdf.parse(txtFecNac.getText()));

            controlador.actualizarUsuario(usuarioActual);
            JOptionPane.showMessageDialog(this, "Datos actualizados correctamente");
            activarEdicion(false);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar");
        }
    }
}

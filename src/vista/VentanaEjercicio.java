package vista;

import modelo.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import controlador.Controlador;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.ArrayList;

public class VentanaEjercicio extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JLabel lblNombreEjercicio, lblDescripcion, lblWorkout, lblCronoGeneral, lblDescanso;
    private JTextArea lblSerieActual;
    private JButton btnControl;
    private Timer timerGeneral, timerSerie, timerDescanso;
    private int tiempoTotal = 0;
    private int serieIndex = 0;
    @SuppressWarnings("unused")
	private boolean enPausa = false;
    private boolean enDescanso = false;
    private Ejercicios ejercicio;
    private Workout workout;
    private ArrayList<Series> series;
    private int duracion = 0;
    private int descanso = 0;
    private Usuarios usuario;
    private Controlador controlador;

    public VentanaEjercicio(Ejercicios ejercicio, Workout workout, Usuarios usuario) {
        controlador = new Controlador();
        this.usuario = usuario;
        this.ejercicio = ejercicio;
        this.workout = workout;
        this.series = ejercicio.getSeries();

        setTitle("POWERHOUSE GYM - Entrenamiento");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 950, 650);
        setLocationRelativeTo(null);
        setResizable(false);

        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        // -------------------- TÍTULO --------------------
        JLabel lblTitulo = new JLabel("POWERHOUSE GYM");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblTitulo.setForeground(new Color(200, 0, 0));
        lblTitulo.setBounds(300, 5, 400, 40);
        contentPane.add(lblTitulo);

        JLabel lblSub = new JLabel("ENTRENANDO EJERCICIO");
        lblSub.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblSub.setBounds(320, 40, 400, 30);
        lblSub.setForeground(Color.BLACK);
        contentPane.add(lblSub);

        lblNombreEjercicio = new JLabel("Ejercicio: " + ejercicio.getNombre());
        lblNombreEjercicio.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblNombreEjercicio.setBounds(50, 90, 600, 40);
        lblNombreEjercicio.setForeground(new Color(200, 0, 0));
        contentPane.add(lblNombreEjercicio);

        lblDescripcion = new JLabel("<html>Descripción: " + ejercicio.getDescripcion() + "</html>");
        lblDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblDescripcion.setBounds(50, 135, 430, 60);
        lblDescripcion.setForeground(Color.BLACK);
        contentPane.add(lblDescripcion);

        lblWorkout = new JLabel("Workout: " + workout.getNombre());
        lblWorkout.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblWorkout.setBounds(650, 100, 250, 40);
        lblWorkout.setForeground(new Color(200, 0, 0));
        contentPane.add(lblWorkout);

        lblCronoGeneral = new JLabel("Tiempo Total: 00:00");
        lblCronoGeneral.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblCronoGeneral.setBounds(50, 210, 300, 40);
        lblCronoGeneral.setForeground(Color.BLACK);
        contentPane.add(lblCronoGeneral);

        lblSerieActual = new JTextArea();
        lblSerieActual.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblSerieActual.setBounds(420, 210, 450, 220);
        lblSerieActual.setBackground(new Color(250, 250, 250));
        lblSerieActual.setEditable(false);
        lblSerieActual.setBorder(BorderFactory.createLineBorder(new Color(200, 0, 0), 2));
        lblSerieActual.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        contentPane.add(lblSerieActual);

        lblSerieActual.addMouseListener(new MouseAdapter() { @Override public void mouseClicked(MouseEvent e) { mostrarImagenesSeries(); } });

        lblDescanso = new JLabel("Descanso:");
        lblDescanso.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblDescanso.setBounds(50, 270, 350, 40);
        lblDescanso.setForeground(new Color(200, 0, 0));
        contentPane.add(lblDescanso);

        btnControl = new JButton("Iniciar");
        btnControl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btnControl.setBounds(340, 480, 260, 70);
        btnControl.setBackground(new Color(200, 0, 0));
        btnControl.setForeground(Color.WHITE);
        btnControl.setFocusPainted(false);
        btnControl.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        contentPane.add(btnControl);

        btnControl.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnControl.setBackground(new Color(255, 40, 40)); }
            public void mouseExited(MouseEvent e) {
                if (!btnControl.getText().equals("Pausar")) btnControl.setBackground(new Color(200, 0, 0));
            }
        });

        btnControl.addActionListener(e -> {
            switch (btnControl.getText()) {
                case "Iniciar":
                    if (timerGeneral == null) iniciarCronometroGeneral();
                    btnControl.setText("Pausar");
                    btnControl.setBackground(Color.ORANGE);
                    iniciarSerie();
                    break;
                case "Pausar":
                    pausarTimers();
                    break;
                case "Reanudar":
                    reanudarTimers();
                    break;
                case "Siguiente Ejercicio":
                    siguienteEjercicio();
                    break;
            }
        });

        mostrarTodasLasSeries();
    }

    // ==================== LÓGICA ORIGINAL ====================
    private void iniciarCronometroGeneral() {
        timerGeneral = new Timer(1000, e -> {
            tiempoTotal++;
            lblCronoGeneral.setText("Tiempo Total: " + formatTime(tiempoTotal));
        });
        timerGeneral.start();
    }

    private void iniciarSerie() {
        if (serieIndex >= series.size()) {
            finalizarEjercicio();
            return;
        }

        Series serie = series.get(serieIndex);
        duracion = serie.getDuracion();

        if(enDescanso) btnControl.setEnabled(false);

        timerSerie = new Timer(1000, e -> {
            if (duracion >= 0) {
                lblDescanso.setText("Tiempo restante: " + duracion + " s");
                duracion--;
            } else {
                timerSerie.stop();
                iniciarDescanso();
            }
        });
        timerSerie.start();
    }

    private void iniciarDescanso() {
        enDescanso = true;
        descanso = ejercicio.getTiempoDescanso();
        lblDescanso.setText("Descanso: " + descanso + " s");
        btnControl.setText("Iniciar");
        btnControl.setBackground(new Color(200, 0, 0));
        btnControl.setEnabled(false);

        timerDescanso = new Timer(1000, e -> {
            if (descanso > 0) {
                lblDescanso.setText("Descanso: " + descanso + " s");
                descanso--;
            } else {
                timerDescanso.stop();
                enDescanso = false;
                serieIndex++;
                btnControl.setEnabled(true);
            }
        });
        timerDescanso.start();
    }

    private void pausarTimers() {
        if (timerSerie != null) timerSerie.stop();
        if (timerDescanso != null) timerDescanso.stop();
        if (timerGeneral != null) timerGeneral.stop();
        btnControl.setText("Reanudar");
        btnControl.setBackground(new Color(0, 90, 200));
        enPausa = true;
    }

    private void reanudarTimers() {
        if (timerSerie != null) timerSerie.start();
        if (timerDescanso != null) timerDescanso.start();
        if (timerGeneral != null) timerGeneral.start();
        btnControl.setText("Pausar");
        btnControl.setBackground(Color.ORANGE);
        enPausa = false;
    }

    private void finalizarEjercicio() {
        if (timerGeneral != null) timerGeneral.stop();
        btnControl.setText("Siguiente Ejercicio");
        btnControl.setBackground(new Color(120, 0, 120));
        lblSerieActual.setText("¡Ejercicio completado!");
        lblDescanso.setText("");
    }

    private void siguienteEjercicio() {
        try {
            controlador.guardarHistoricoAutomatico(usuario, workout, tiempoTotal, 100.0);
            controlador.subirNivelUsuario(usuario);
        } catch (Exception e) { e.printStackTrace(); }

        JOptionPane.showMessageDialog(this,
            "¡Workout completado!\nTiempo total: " + formatTime(tiempoTotal) + "\n¡Gran trabajo!",
            "Completado", JOptionPane.INFORMATION_MESSAGE);

        dispose();
        new VentanaWorkouts(usuario).setVisible(true);
    }

    private String formatTime(int t) {
        return String.format("%02d:%02d", t / 60, t % 60);
    }

    private void mostrarTodasLasSeries() {
        lblSerieActual.setText("");
        for (Series s : series) {
            lblSerieActual.append("Serie: " + s.getNombre() + " | Reps: " + s.getRepeticiones() + "\n");
        }
    }

    private void mostrarImagenesSeries() {
        if (series == null || series.isEmpty()) return;

        JDialog ventana = new JDialog(this, "Imágenes de Series", true);
        ventana.setSize(900, 600);
        ventana.setLocationRelativeTo(this);

        JPanel panelFotos = new JPanel();
        panelFotos.setLayout(new BoxLayout(panelFotos, BoxLayout.Y_AXIS));

        for (Series s : series) {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBorder(BorderFactory.createTitledBorder(s.getNombre()));

            try {
                @SuppressWarnings("deprecation")
				ImageIcon icon = new ImageIcon(new URL(s.getImagen()));
                JLabel lbl = new JLabel(icon);
                lbl.setHorizontalAlignment(SwingConstants.CENTER);
                panel.add(lbl);
            } catch (Exception ex) {
                panel.add(new JLabel("Sin imagen", SwingConstants.CENTER));
            }

            panelFotos.add(panel);
            panelFotos.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        ventana.add(new JScrollPane(panelFotos));
        ventana.setVisible(true);
    }
}

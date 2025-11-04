package vista;

import modelo.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

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
    private boolean enPausa = false;
    private boolean enDescanso = false;
    private Ejercicios ejercicio;
    private Workout workout;
    private ArrayList<Series> series;
    private int duracion = 0;
    private int descanso = 0;
    private Usuarios usuario;
    private Controlador controlador ;

    public VentanaEjercicio(Ejercicios ejercicio, Workout workout, Usuarios usuario) {
        controlador = new Controlador();
        this.usuario = usuario;
        this.ejercicio = ejercicio;
        this.workout = workout;
        this.series = ejercicio.getSeries();

        setTitle("Pantalla de Ejercicio");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 900, 600);
        setResizable(false);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(15, 15, 15, 15));
        contentPane.setLayout(null);
        setContentPane(contentPane);
        contentPane.setBackground(new Color(245, 245, 245));

        lblNombreEjercicio = new JLabel("Ejercicio: " + ejercicio.getNombre());
        lblNombreEjercicio.setFont(new Font("Tahoma", Font.BOLD, 26));
        lblNombreEjercicio.setBounds(300, 11, 500, 40);
        contentPane.add(lblNombreEjercicio);

        lblDescripcion = new JLabel("Descripción: " + ejercicio.getDescripcion());
        lblDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblDescripcion.setBounds(300, 55, 550, 60);
        contentPane.add(lblDescripcion);

        lblWorkout = new JLabel("Workout: " + workout.getNombre());
        lblWorkout.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblWorkout.setBounds(575, 18, 299, 30);
        lblWorkout.setForeground(new Color(128, 0, 0));
        contentPane.add(lblWorkout);

        lblCronoGeneral = new JLabel("Tiempo Total: 00:00");
        lblCronoGeneral.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblCronoGeneral.setBounds(30, 150, 250, 40);
        lblCronoGeneral.setForeground(Color.DARK_GRAY);
        contentPane.add(lblCronoGeneral);

        lblSerieActual = new JTextArea();
        lblSerieActual.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblSerieActual.setBounds(278, 201, 500, 192);
        lblSerieActual.setBackground(new Color(245, 245, 245));
        lblSerieActual.setEditable(false);
        lblSerieActual.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        contentPane.add(lblSerieActual);

        lblSerieActual.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mostrarImagenesSeries();
            }
        });

        lblDescanso = new JLabel("Descanso:");
        lblDescanso.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblDescanso.setBounds(300, 390, 500, 40);
        lblDescanso.setForeground(new Color(102, 0, 0));
        contentPane.add(lblDescanso);

        btnControl = new JButton("Iniciar");
        btnControl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnControl.setBounds(350, 480, 200, 55);
        btnControl.setBackground(Color.GREEN);
        btnControl.setForeground(Color.WHITE);
        btnControl.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        contentPane.add(btnControl);

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

    // ==================== FUNCIONALIDAD ====================

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
        btnControl.setBackground(Color.GREEN);
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
        btnControl.setBackground(Color.BLUE);
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
        btnControl.setBackground(new Color(128, 0, 128));
        lblSerieActual.setText("¡Ejercicio completado!");
        lblDescanso.setText("");
    }

    private void siguienteEjercicio() {
        JOptionPane.showMessageDialog(this,
            "¡Workout completado!\n" +
            "Tiempo total: " + formatTime(tiempoTotal) + "\n" +
            "Ejercicios completados: 100%\n" +
            "¡Buen trabajo!",
            "Resumen",
            JOptionPane.INFORMATION_MESSAGE
        );
        try {
            controlador.guardarHistoricoAutomatico(usuario, workout, tiempoTotal, 100.0);
            controlador.subirNivelUsuario(usuario);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al guardar el histórico: " + e.getMessage());
        }
        dispose();
        VentanaWorkouts ventanaWorkouts = new VentanaWorkouts(usuario);
        ventanaWorkouts.setVisible(true);
    }

    private String formatTime(int totalSeconds) {
        int mins = totalSeconds / 60;
        int secs = totalSeconds % 60;
        return String.format("%02d:%02d", mins, secs);
    }

    private void mostrarTodasLasSeries() {
        lblSerieActual.setText("");
        for (Series s : series) {
            lblSerieActual.append("Serie: " + s.getNombre() + " | Repeticiones: " + s.getRepeticiones() + "\n");
        }
    }

    // ==================== PANEL DE IMÁGENES ====================
    private void mostrarImagenesSeries() {
        if (series == null || series.isEmpty()) return;

        JDialog ventanaImagenes = new JDialog(this, "Imágenes de Series", true);
        ventanaImagenes.setSize(900, 600);
        ventanaImagenes.setLocationRelativeTo(this);

        JPanel panelFotos = new JPanel();
        panelFotos.setLayout(new BoxLayout(panelFotos, BoxLayout.Y_AXIS)); // se apilan verticalmente

        for (Series s : series) {
            JPanel panelSerie = new JPanel();
            panelSerie.setLayout(new BorderLayout());
            panelSerie.setBorder(BorderFactory.createTitledBorder(s.getNombre()));

            try {
                ImageIcon icon = new ImageIcon(new URL(s.getImagen()));
                JLabel lblFoto = new JLabel();
                lblFoto.setIcon(icon);
                lblFoto.setHorizontalAlignment(SwingConstants.CENTER);
                lblFoto.setVerticalAlignment(SwingConstants.CENTER);

                panelSerie.add(lblFoto, BorderLayout.CENTER);
            } catch (Exception ex) {
                JLabel lblError = new JLabel("Sin imagen");
                lblError.setHorizontalAlignment(SwingConstants.CENTER);
                panelSerie.add(lblError, BorderLayout.CENTER);
            }

            panelFotos.add(panelSerie);
            panelFotos.add(Box.createRigidArea(new Dimension(0, 10))); // espacio entre series
        }

        JScrollPane scroll = new JScrollPane(panelFotos);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        ventanaImagenes.add(scroll, BorderLayout.CENTER);

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> ventanaImagenes.dispose());
        ventanaImagenes.add(btnCerrar, BorderLayout.SOUTH);

        ventanaImagenes.setVisible(true);
    }


}

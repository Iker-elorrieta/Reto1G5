package vista;

import modelo.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.BorderFactory;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class VentanaEjercicio extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JLabel lblNombreEjercicio, lblDescripcion, lblWorkout, lblCronoGeneral, lblSerieActual, lblDescanso;
    private JButton btnControl;
    private Timer timerGeneral, timerSerie, timerDescanso, timerCuentaRegresiva;
    private int tiempoTotal = 0;
    private int serieIndex = 0;
    private boolean enPausa = false;
    private boolean enDescanso = false;
    private Ejercicios ejercicio;
    private Workout workout;
    private ArrayList<Series> series;
    private int cuentaRegresiva = 5;
    private int duracion = 0;
    private int descanso = 0;

    public VentanaEjercicio(Ejercicios ejercicio, Workout workout) {
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

        // ======== ETIQUETAS DE ENCABEZADO ========
        lblNombreEjercicio = new JLabel("Ejercicio: " + ejercicio.getNombre());
        lblNombreEjercicio.setFont(new Font("Tahoma", Font.BOLD, 26));
        lblNombreEjercicio.setBounds(300, 10, 500, 40);
        contentPane.add(lblNombreEjercicio);

        lblDescripcion = new JLabel("<html><body>Descripción: " + ejercicio.getDescripcion() + "</body></html>");
        lblDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblDescripcion.setBounds(300, 55, 550, 60);
        contentPane.add(lblDescripcion);

        lblWorkout = new JLabel("Workout: " + workout.getNombre());
        lblWorkout.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblWorkout.setBounds(700, 10, 180, 30);
        lblWorkout.setForeground(new Color(128, 0, 0));
        contentPane.add(lblWorkout);

        // ======== CRONÓMETROS ========
        lblCronoGeneral = new JLabel("Tiempo Total: 00:00");
        lblCronoGeneral.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblCronoGeneral.setBounds(30, 150, 250, 40);
        lblCronoGeneral.setForeground(Color.DARK_GRAY);
        contentPane.add(lblCronoGeneral);

        lblSerieActual = new JLabel("Serie actual:");
        lblSerieActual.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblSerieActual.setBounds(300, 200, 500, 40);
        lblSerieActual.setForeground(new Color(0, 51, 102));
        contentPane.add(lblSerieActual);

        lblDescanso = new JLabel("Descanso:");
        lblDescanso.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblDescanso.setBounds(300, 250, 500, 40);
        lblDescanso.setForeground(new Color(102, 0, 0));
        contentPane.add(lblDescanso);

        // ======== BOTÓN DE CONTROL ========
        btnControl = new JButton("Iniciar");
        btnControl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnControl.setBounds(350, 480, 200, 55);
        btnControl.setBackground(Color.RED);
        btnControl.setForeground(Color.WHITE);
        btnControl.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        contentPane.add(btnControl);

        // ======== ACCIONES ========
        btnControl.addActionListener(e -> {
            switch (btnControl.getText()) {
                case "Iniciar":
                    iniciarCuentaRegresiva();
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

        // ======== IMAGEN ILUSTRATIVA ========
        JLabel lblImagen = new JLabel("");
        lblImagen.setBounds(30, 10, 250, 250);
        ImageIcon icono = new ImageIcon("img/ejercicio.png");
        if (icono.getImage() != null) {
            Image imagen = icono.getImage().getScaledInstance(lblImagen.getWidth(), lblImagen.getHeight(), Image.SCALE_SMOOTH);
            lblImagen.setIcon(new ImageIcon(imagen));
        } else {
            System.out.println("Imagen no encontrada");
        }
        contentPane.add(lblImagen);

        iniciarCronometroGeneral();
    }

    // ====================== FUNCIONALIDAD ======================

    private void iniciarCronometroGeneral() {
        timerGeneral = new Timer(1000, e -> {
            tiempoTotal++;
            lblCronoGeneral.setText("Tiempo Total: " + formatTime(tiempoTotal));
        });
        timerGeneral.start();
    }

    private void iniciarCuentaRegresiva() {
        cuentaRegresiva = 5;
        btnControl.setEnabled(false);
        timerCuentaRegresiva = new Timer(1000, e -> {
            if (cuentaRegresiva > 0) {
                lblSerieActual.setText("Comenzando en: " + cuentaRegresiva + " segundos");
                cuentaRegresiva--;
            } else {
                timerCuentaRegresiva.stop();
                btnControl.setText("Pausar");
                btnControl.setBackground(Color.ORANGE);
                btnControl.setEnabled(true);
                iniciarSerie();
            }
        });
        timerCuentaRegresiva.start();
    }

    private void iniciarSerie() {
        if (serieIndex >= series.size()) {
            finalizarEjercicio();
            return;
        }

        Series serie = series.get(serieIndex);
        duracion = serie.getDuracion();
        lblSerieActual.setText("Serie: " + serie.getNombre() + " | Repeticiones: " + serie.getRepeticiones());

        timerSerie = new Timer(1000, e -> {
            if (duracion > 0) {
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
        btnControl.setBackground(Color.RED);

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
        timerGeneral.stop();
        btnControl.setText("Reanudar");
        btnControl.setBackground(Color.BLUE);
        enPausa = true;
    }

    private void reanudarTimers() {
        if (timerSerie != null) timerSerie.start();
        if (timerDescanso != null) timerDescanso.start();
        timerGeneral.start();
        btnControl.setText("Pausar");
        btnControl.setBackground(Color.ORANGE);
        enPausa = false;
    }

    private void finalizarEjercicio() {
        timerGeneral.stop();
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
        dispose();
    }

    private String formatTime(int totalSeconds) {
        int mins = totalSeconds / 60;
        int secs = totalSeconds % 60;
        return String.format("%02d:%02d", mins, secs);
    }
}

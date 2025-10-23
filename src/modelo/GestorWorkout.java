package modelo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

public class GestorWorkout {

    public ArrayList<Workout> leerWorkoutsBD(int nivelUsuario)
            throws IOException, InterruptedException, ExecutionException {

        Firestore db = null;
        ArrayList<Workout> works = new ArrayList<>();
        try {
            db = ConectorFirebase.conectar();
            CollectionReference workout = db.collection("Workouts");

            QuerySnapshot querySnapshot = workout.get().get();
            List<QueryDocumentSnapshot> workouts = querySnapshot.getDocuments();
            for (QueryDocumentSnapshot work : workouts) {
                int nivelWorkout = work.getDouble("nivel").intValue();

                // Filtrar por nivel
                if (nivelWorkout <= nivelUsuario) {

                    Workout w = new Workout();
                    w.setNombre(work.getString("nombre"));
                    w.setVideo(work.getString("video"));
                    w.setNivel(nivelWorkout);
                    w.setNumEjers(work.getDouble("numEjer").intValue());

                    // Leer ejercicios
                    CollectionReference ejerciciosCol = work.getReference().collection("Ejercicios");
                    List<QueryDocumentSnapshot> ejerciciosDocs = ejerciciosCol.get().get().getDocuments();
                    ArrayList<Ejercicios> listaEjercicios = new ArrayList<>();

                    for (QueryDocumentSnapshot ejDoc : ejerciciosDocs) {
                        Ejercicios e = new Ejercicios(ejDoc.getString("Nombre"), ejDoc.getString("Descripcion"),
                                ejDoc.getString("Img"), ejDoc.getDouble("Nivel").intValue(),
                                ejDoc.getDouble("tiempoDescanso").intValue(), new ArrayList<>());

                        // Leer series si existen
                        CollectionReference seriesCol = ejDoc.getReference().collection("Series");
                        List<QueryDocumentSnapshot> seriesDocs = seriesCol.get().get().getDocuments();
                        ArrayList<Series> listaSeries = new ArrayList<>();
                        for (QueryDocumentSnapshot sDoc : seriesDocs) {
                            Series s = new Series(sDoc.getString("Nombre"), sDoc.getDouble("repeticiones").intValue(),
                                    sDoc.getDouble("duracion").intValue());
                            listaSeries.add(s);
                        }
                        e.setSeries(listaSeries);

                        listaEjercicios.add(e);
                    }

                    w.setEjercicios(listaEjercicios);
                    works.add(w);
                }
            }

        } finally {
            // Do not close Firestore here. The connection is a singleton managed by ConectorFirebase
            // and should be closed once at application shutdown (see Main). Closing here causes
            // the repeated "Firestore cerrado" messages and reinitialization overhead.
        }

        return works;
    }

    public ArrayList<Workout> leerWorkoutsBDBackups()
            throws IOException, InterruptedException, ExecutionException {

        Firestore db = null;
        ArrayList<Workout> works = new ArrayList<>();
        try {
            db = ConectorFirebase.conectar();
            CollectionReference workout = db.collection("Workouts");

            QuerySnapshot querySnapshot = workout.get().get();
            List<QueryDocumentSnapshot> workouts = querySnapshot.getDocuments();
            for (QueryDocumentSnapshot work : workouts) {
                int nivelWorkout = work.getDouble("nivel").intValue();

                Workout w = new Workout();
                w.setNombre(work.getString("nombre"));
                w.setVideo(work.getString("video"));
                w.setNivel(nivelWorkout);
                w.setNumEjers(work.getDouble("numEjer").intValue());

                CollectionReference ejerciciosCol = work.getReference().collection("Ejercicios");
                List<QueryDocumentSnapshot> ejerciciosDocs = ejerciciosCol.get().get().getDocuments();
                ArrayList<Ejercicios> listaEjercicios = new ArrayList<>();

                for (QueryDocumentSnapshot ejDoc : ejerciciosDocs) {
                    Ejercicios e = new Ejercicios(ejDoc.getString("Nombre"), ejDoc.getString("Descripcion"),
                            ejDoc.getString("Img"), ejDoc.getDouble("Nivel").intValue(),
                            ejDoc.getDouble("tiempoDescanso").intValue(), new ArrayList<>());

                    CollectionReference seriesCol = ejDoc.getReference().collection("Series");
                    List<QueryDocumentSnapshot> seriesDocs = seriesCol.get().get().getDocuments();
                    ArrayList<Series> listaSeries = new ArrayList<>();
                    for (QueryDocumentSnapshot sDoc : seriesDocs) {
                        Series s = new Series(sDoc.getString("Nombre"), sDoc.getDouble("repeticiones").intValue(),
                                sDoc.getDouble("duracion").intValue());
                        listaSeries.add(s);
                    }
                    e.setSeries(listaSeries);
                    listaEjercicios.add(e);
                }

                w.setEjercicios(listaEjercicios);
                works.add(w);
            }

        } finally {
            // Do not close Firestore here. The connection is a singleton managed by ConectorFirebase
            // and should be closed once at application shutdown (see Main).
        }

        return works;
    }

    public ArrayList<HistoricoWorkouts> cargarDatos(String email)
            throws IOException, InterruptedException, ExecutionException {

        ArrayList<HistoricoWorkouts> lista = new ArrayList<>();
        if (email == null || email.isEmpty()) {
            System.err.println("Error: email vacío o nulo");
            return lista;
        }

        Firestore db = null;
        try {
            db = ConectorFirebase.conectar();
            CollectionReference historicoRef = db.collection("usuarios").document(email).collection("Historico");

            ApiFuture<QuerySnapshot> query = historicoRef.orderBy("fecha", Query.Direction.DESCENDING).get();
            QuerySnapshot snapshot = query.get();

            System.out.println("Docs encontrados para " + email + ": " + snapshot.size());

            for (DocumentSnapshot doc : snapshot.getDocuments()) {
                HistoricoWorkouts hw = new HistoricoWorkouts(doc.getString("Nombre"), doc.getLong("Nivel").intValue(),
                        doc.getLong("tiempoTotal").intValue(), doc.getLong("tiempoPrevisto").intValue(),
                        doc.getDate("fecha"), (int) Math.round(doc.getDouble("Porcentaje")),
                        doc.getString("usuario"));
                lista.add(hw);

                System.out.println("Cargado: " + hw.getNombreWorkout() + " - Nivel: " + hw.getNivel() + " - Porcentaje: "
                        + hw.getPorcentajeCompletado() + "%");
            }

        } finally {
            // Do not close Firestore here. The connection is a singleton managed by ConectorFirebase
            // and should be closed once at application shutdown (see Main).
        }

        return lista;
    }

    public String formatearTiempo(int segundos) {
        int min = segundos / 60;
        int seg = segundos % 60;
        return String.format("%02d:%02d", min, seg);
    }
}
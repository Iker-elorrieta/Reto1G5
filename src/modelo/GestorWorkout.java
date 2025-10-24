
package modelo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
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
                w.setId(work.getId());
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

    public ArrayList<HistoricoWorkouts> cargarDatos(Usuarios usu) 
            throws IOException, InterruptedException, ExecutionException {

        ArrayList<HistoricoWorkouts> lista = new ArrayList<>();
        if (usu == null || usu.getIdUsuario() == null || usu.getIdUsuario().isEmpty()) {
            System.err.println("Error: usuario o idUsuario vacío");
            return lista;
        }

        Firestore db = ConectorFirebase.conectar();

        // 1️⃣ Cargar todos los workouts completos
        ArrayList<Workout> workouts = leerWorkoutsBDBackups(); // todos los workouts
        Map<String, Workout> mapWorkouts = new HashMap<>();
        for (Workout w : workouts) {
            mapWorkouts.put(w.getId(), w); // usar ID para búsqueda rápida
        }

        // 2️⃣ Obtener el histórico del usuario
        CollectionReference historicoRef = db.collection("usuarios")
                                             .document(usu.getIdUsuario())
                                             .collection("Historico");
        ApiFuture<QuerySnapshot> query = historicoRef.orderBy("fecha", Query.Direction.DESCENDING).get();
        QuerySnapshot snapshot = query.get();

        System.out.println("ID usuario: " + usu.getIdUsuario());
        System.out.println("Históricos encontrados: " + snapshot.size());

        //HACEMOS COMPROBACIONES PARA VER SI COGE EL HISTORICO Y EL WORKOUT CORRECTAMENTE
        for (DocumentSnapshot doc : snapshot.getDocuments()) {
            System.out.println("DocID historico: " + doc.getId() + " | idWorkout: " + doc.get("idWorkout"));

            Workout workout = null;

            Object campo = doc.get("idWorkout");
            if (campo != null) {
                // Si es DocumentReference
                if (campo instanceof DocumentReference) {
                    DocumentReference refWorkout = (DocumentReference) campo;
                    workout = mapWorkouts.get(refWorkout.getId());
                } 
                // Si es String (ID del workout)
                else if (campo instanceof String) {
                    String workoutId = (String) campo;
                    workout = mapWorkouts.get(workoutId);
                }
            }

            if (workout == null) {
                System.err.println("No se encontró el workout para el documento histórico: " + doc.getId());
                continue;
            }

            // Tiempo total
            Long tiempoLong = doc.getLong("tiempoTotal");
            int tiempoTotal = (tiempoLong != null) ? tiempoLong.intValue() :
                              (doc.getDouble("tiempoTotal") != null ? doc.getDouble("tiempoTotal").intValue() : 0);

            // Fecha
            Date fecha = doc.getDate("fecha");
            if (fecha == null) fecha = new Date();

            // Porcentaje completado
            Double porcentaje = doc.getDouble("Porcentaje");
            double porcentajeVal = (porcentaje != null) ? porcentaje : 0;

            // Crear el histórico con el Workout asociado
            HistoricoWorkouts hw = new HistoricoWorkouts(
                    workout.getNombre(),
                    tiempoTotal,
                    fecha,
                    porcentajeVal,
                    workout
            );

            lista.add(hw);

        }

        return lista;
    }




	public String formatearTiempo(int segundos) {
		int min = segundos / 60;
		int seg = segundos % 60;
		return String.format("%02d:%02d", min, seg);
	}
}
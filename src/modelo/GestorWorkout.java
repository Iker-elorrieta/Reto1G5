
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
	private final String coleccionW = "Workouts";
	private final String coleccionU = "usuarios";
	private final String coleccionE = "Ejercicios";
	private final String coleccionS = "Series";
	private final String nombreW = "nombre";
	private final String nombreU = "Nombre";
	private final String video = "video";
	private final String nivelW = "nivel";
	private final String numEjer = "numEjer";
	private final String nivelE = "Nivel";
	private final String tiempo = "tiempoTotal";
	private final String duracion = "duracion";
	private final String repeticiones = "repeticiones";
	private final String img = "Img";
	private final String imagenSerie = "imagen";
	private final String descripcion = "Descripcion";
	private final String tiempoD = "tiempoDescanso";



    public ArrayList<Workout> leerWorkoutsBD(int nivelUsuario)
            throws IOException, InterruptedException, ExecutionException {

        Firestore db = null;
        ArrayList<Workout> works = new ArrayList<>();
        try {
            db = ConectorFirebase.conectar();
            CollectionReference workout = db.collection(coleccionW);

            QuerySnapshot querySnapshot = workout.get().get();
            List<QueryDocumentSnapshot> workouts = querySnapshot.getDocuments();
            for (QueryDocumentSnapshot work : workouts) {
                int nivelWorkout = work.getDouble(nivelW).intValue();

                // Filtrar por nivel
                if (nivelWorkout <= nivelUsuario) {

                    Workout w = new Workout();
                    w.setId(work.getId());
                    w.setNombre(work.getString(nombreW));
                    w.setVideo(work.getString(video));
                    w.setNivel(nivelWorkout);
                    w.setNumEjers(work.getDouble(numEjer).intValue());

                    // Leer ejercicios
                    CollectionReference ejerciciosCol = work.getReference().collection(coleccionE);
                    List<QueryDocumentSnapshot> ejerciciosDocs = ejerciciosCol.get().get().getDocuments();
                    ArrayList<Ejercicios> listaEjercicios = new ArrayList<>();

                    for (QueryDocumentSnapshot ejDoc : ejerciciosDocs) {
                        Ejercicios e = new Ejercicios(ejDoc.getString(nombreU), ejDoc.getString(descripcion),
                                ejDoc.getString(img), ejDoc.getDouble(nivelE).intValue(),
                                ejDoc.getDouble(tiempoD).intValue(), new ArrayList<>());

                        // Leer series si existen
                        CollectionReference seriesCol = ejDoc.getReference().collection(coleccionS);
                        List<QueryDocumentSnapshot> seriesDocs = seriesCol.get().get().getDocuments();
                        ArrayList<Series> listaSeries = new ArrayList<>();
                        for (QueryDocumentSnapshot sDoc : seriesDocs) {
                            Series s = new Series(sDoc.getString(nombreU), sDoc.getDouble(repeticiones).intValue(),
                                    sDoc.getDouble(duracion).intValue(), sDoc.getString(imagenSerie));
                            
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

        }

        return works;
    }

    public ArrayList<Workout> leerWorkoutsBDBackups()
            throws IOException, InterruptedException, ExecutionException {

        Firestore db = null;
        ArrayList<Workout> works = new ArrayList<>();
        try {
            db = ConectorFirebase.conectar();
            CollectionReference workout = db.collection(coleccionW);

            QuerySnapshot querySnapshot = workout.get().get();
            List<QueryDocumentSnapshot> workouts = querySnapshot.getDocuments();
            for (QueryDocumentSnapshot work : workouts) {
                int nivelWorkout = work.getDouble(nivelW).intValue();

                Workout w = new Workout();
                w.setId(work.getId());
                w.setNombre(work.getString(nombreW));
                w.setVideo(work.getString(video));
                w.setNivel(nivelWorkout);
                w.setNumEjers(work.getDouble(numEjer).intValue());

                CollectionReference ejerciciosCol = work.getReference().collection(coleccionE);
                List<QueryDocumentSnapshot> ejerciciosDocs = ejerciciosCol.get().get().getDocuments();
                ArrayList<Ejercicios> listaEjercicios = new ArrayList<>();

                for (QueryDocumentSnapshot ejDoc : ejerciciosDocs) {
                    Ejercicios e = new Ejercicios(ejDoc.getString(nombreU), ejDoc.getString(descripcion),
                            ejDoc.getString(img), ejDoc.getDouble(nivelE).intValue(),
                            ejDoc.getDouble(tiempoD).intValue(), new ArrayList<>());

                    CollectionReference seriesCol = ejDoc.getReference().collection(coleccionS);
                    List<QueryDocumentSnapshot> seriesDocs = seriesCol.get().get().getDocuments();
                    ArrayList<Series> listaSeries = new ArrayList<>();
                    for (QueryDocumentSnapshot sDoc : seriesDocs) {
                        Series s = new Series(sDoc.getString(nombreU), sDoc.getDouble(repeticiones).intValue(),
                                sDoc.getDouble(duracion).intValue(), sDoc.getString(imagenSerie));
                        listaSeries.add(s);
                    }
                    e.setSeries(listaSeries);
                    listaEjercicios.add(e);
                }

                w.setEjercicios(listaEjercicios);
                works.add(w);
            }

        } finally {
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

        // 1️ Cargar todos los workouts completos
        ArrayList<Workout> workouts = leerWorkoutsBDBackups(); // todos los workouts
        Map<String, Workout> mapWorkouts = new HashMap<>();
        for (Workout w : workouts) {
            mapWorkouts.put(w.getId(), w); // usar ID para búsqueda rápida
        }

        // 2️ Obtener el histórico del usuario
        CollectionReference historicoRef = db.collection(coleccionU)
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
            Long tiempoLong = doc.getLong(tiempo);
            int tiempoTotal = (tiempoLong != null) ? tiempoLong.intValue() :
                              (doc.getDouble(tiempo) != null ? doc.getDouble(tiempo).intValue() : 0);

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


    public void guardarHistoricoAutomatico(Usuarios usuario, Workout workout, int tiempoTotal, double porcentaje)
            throws InterruptedException, ExecutionException, IOException {

        if (usuario == null || usuario.getIdUsuario() == null || usuario.getIdUsuario().isEmpty()) {
            System.err.println("Usuario no válido para guardar histórico");
            return;
        }

        Firestore db = ConectorFirebase.conectar();

        // Ruta: usuarios/{id}/Historico
        CollectionReference historicoRef = db.collection("usuarios")
                                             .document(usuario.getIdUsuario())
                                             .collection("Historico");

        Map<String, Object> datos = new HashMap<>();
        datos.put("fecha", new java.util.Date());
        datos.put("Porcentaje", porcentaje);
        datos.put("tiempoTotal", tiempoTotal);
        DocumentReference workoutRef = db.collection("Workouts").document(workout.getId());
        datos.put("idWorkout", workoutRef);


        historicoRef.add(datos).get(); // Espera a que se guarde
        System.out.println("Histórico guardado correctamente para el usuario " + usuario.getIdUsuario());
    }


	public String formatearTiempo(int segundos) {
		int min = segundos / 60;
		int seg = segundos % 60;
		return String.format("%02d:%02d", min, seg);
	}
}
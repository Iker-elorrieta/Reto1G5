package modelo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

public class GestorWorkout {

	public ArrayList<Workout> leerWorkoutsBD(int nivelUsuario)
			throws IOException, InterruptedException, ExecutionException {
		Firestore db = ConectorFirebase.conectar();
		CollectionReference workout = db.collection("Workouts");
		ArrayList<Workout> works = new ArrayList<>();

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
					Ejercicios e = new Ejercicios(ejDoc.getString("Nombre"), ejDoc.getString("Descripcion"), // corregido
																												// typo
							ejDoc.getString("Img"), ejDoc.getDouble("Nivel").intValue(),
							ejDoc.getDouble("tiempoDescanso").intValue(), new ArrayList<>());

					// Leer series si existen
					CollectionReference seriesCol = ejDoc.getReference().collection("Series");
					List<QueryDocumentSnapshot> seriesDocs = seriesCol.get().get().getDocuments();
					ArrayList<Series> listaSeries = new ArrayList<>();
					for (QueryDocumentSnapshot sDoc : seriesDocs) {
						Series s = new Series(sDoc.getString("nombre"), sDoc.getDouble("repeticiones").intValue(),
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

		return works;

	}

	public ArrayList<Workout> leerWorkoutsBDBackups() throws IOException, InterruptedException, ExecutionException {
		Firestore db = ConectorFirebase.conectar();
		CollectionReference workout = db.collection("Workouts");
		ArrayList<Workout> works = new ArrayList<>();

		QuerySnapshot querySnapshot = workout.get().get();
		List<QueryDocumentSnapshot> workouts = querySnapshot.getDocuments();
		for (QueryDocumentSnapshot work : workouts) {
			int nivelWorkout = work.getDouble("nivel").intValue();

			// Filtrar por nivel

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
				Ejercicios e = new Ejercicios(ejDoc.getString("Nombre"), ejDoc.getString("Descripcion"), // corregido
																											// typo
						ejDoc.getString("Img"), ejDoc.getDouble("Nivel").intValue(),
						ejDoc.getDouble("tiempoDescanso").intValue(), new ArrayList<>());

				// Leer series si existen
				CollectionReference seriesCol = ejDoc.getReference().collection("Series");
				List<QueryDocumentSnapshot> seriesDocs = seriesCol.get().get().getDocuments();
				ArrayList<Series> listaSeries = new ArrayList<>();
				for (QueryDocumentSnapshot sDoc : seriesDocs) {
					Series s = new Series(sDoc.getString("nombre"), sDoc.getDouble("repeticiones").intValue(),
							sDoc.getDouble("duracion").intValue());
					listaSeries.add(s);
				}
				e.setSeries(listaSeries);

				listaEjercicios.add(e);
			}

			w.setEjercicios(listaEjercicios);
			works.add(w);
		}

		return works;

	}
}

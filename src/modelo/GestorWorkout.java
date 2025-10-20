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

	public ArrayList<Workout> leerWorkoutsBD() throws IOException, InterruptedException, ExecutionException {
		Firestore db = ConectorFirebase.conectar();
		CollectionReference workout = db.collection("Workouts");
		ArrayList<Workout> works = new ArrayList<>();
		
		QuerySnapshot querySnapshot = workout.get().get();
		List<QueryDocumentSnapshot> workouts = querySnapshot.getDocuments();
		for(QueryDocumentSnapshot work : workouts) {
			Workout w = new Workout();
			w.setNombre(work.getString("nombre"));
			w.setVideo(work.getString("video"));
			w.setNivel(work.getDouble("nivel").intValue());
			w.setNumEjers(work.getDouble("numEjer").intValue());
			works.add(w);
		}
		return works;



		
		
	}
}

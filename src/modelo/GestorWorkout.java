package modelo;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

public class GestorWorkout {

	public void leerWorkoutsBD() throws IOException, InterruptedException, ExecutionException {
		Firestore db = ConectorFirebase.conectar();
		CollectionReference workout = db.collection("Workouts");
		
		QuerySnapshot querySnapshot = workout.get().get();
		List<QueryDocumentSnapshot> workouts = querySnapshot.getDocuments();
		for(QueryDocumentSnapshot work : workouts) {
			work.getString("nombre");
			work.getString("video");
			work.getDouble("nivel");
			work.getDouble("numEjer");
		}


		
		
		
	}
}

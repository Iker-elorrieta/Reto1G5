package Backup;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;

public class backup extends Thread {

	@Override
	public void run() {
		Firestore db = null;
		try {
			FileInputStream srvcAccnt = new FileInputStream("usuarios.json");

			FirestoreOptions opciones = FirestoreOptions.getDefaultInstance().toBuilder().setProjectId("reto1-9e159")
					.setCredentials(GoogleCredentials.fromStream(srvcAccnt)).build();

			db = opciones.getService();
			System.out.println("Conexión establecida.");

			// --- 1️ Leer colección de usuarios ---
			CollectionReference usuariosRef = db.collection("usuarios");
			List<QueryDocumentSnapshot> usuariosDocs = usuariosRef.get().get().getDocuments();
			System.out.println("Usuarios encontrados: " + usuariosDocs.size());

			// --- 2️ Leer colección de workouts global ---
			CollectionReference workoutsRef = db.collection("Workouts");
			List<QueryDocumentSnapshot> workoutsDocs = workoutsRef.get().get().getDocuments();
			System.out.println("Workouts globales encontrados: " + workoutsDocs.size());

			// Listas globales
			List<Map<String, Object>> usuariosList = new ArrayList<>();
			List<Map<String, Object>> workoutsList = new ArrayList<>();
			List<Map<String, Object>> historicoGlobal = new ArrayList<>();
			List<Map<String, Object>> EjerciciosList = new ArrayList<>();
			List<Map<String, Object>> SeriesList = new ArrayList<>();

			// --- Procesar usuarios ---
			for (DocumentSnapshot docUsuario : usuariosDocs) {
				Map<String, Object> usuarioData = new HashMap<>(docUsuario.getData());
				usuarioData.put("id", docUsuario.getId());
				usuarioData.put("Nombre", docUsuario.getString("Nombre"));
				usuarioData.put("Apellido", docUsuario.getString("Apellido"));
				usuarioData.put("Contraseña", docUsuario.getString("Contraseña"));
				usuarioData.put("Email", docUsuario.getString("Email"));
				usuarioData.put("nivel", docUsuario.getDouble("nivel"));
				usuarioData.put("FecNac", docUsuario.getDate("FecNac"));
				usuariosList.add(usuarioData);
				System.out.println("Usuario añadido: " + docUsuario.getId());

				// --- 3️ Leer subcolección historico de cada usuario ---
				CollectionReference historicoRef = docUsuario.getReference().collection("Historico");
				List<QueryDocumentSnapshot> historicoDocs = historicoRef.get().get().getDocuments();
				System.out.println(
						"Historico encontrado para usuario " + docUsuario.getId() + ": " + historicoDocs.size());

				for (DocumentSnapshot docHistorico : historicoDocs) {
					Map<String, Object> h = new HashMap<>(docHistorico.getData());
					h.put("usuarioId", docUsuario.getId());
					h.put("historicoId", docHistorico.getId());
					h.put("Porcentaje", docHistorico.getDouble("Porcentaje"));
					h.put("fecha", docHistorico.getDate("fecha"));
					DocumentReference refid = (DocumentReference) h.get("idWorkout");
					h.put("idWorkout", refid);
					h.put("tiempoTotal", docHistorico.getDouble("tiempoTotal"));
					historicoGlobal.add(h);
					System.out.println("Registro de historico añadido: " + docHistorico.getId());
				}
			}

			// --- Procesar workouts globales ---
			for (DocumentSnapshot doc : workoutsDocs) {
				Map<String, Object> data = new HashMap<>(doc.getData());
				data.put("id", doc.getId());
				data.put("nivel", doc.getDouble("nivel"));
				data.put("nombre", doc.getString("nombre"));
				data.put("numEjer", doc.getDouble("numEjer"));
				data.put("video", doc.getString("video"));
				workoutsList.add(data);
				System.out.println("Workout añadido: " + doc.getId());

				CollectionReference EjerciciosRef = doc.getReference().collection("Ejercicios");
				List<QueryDocumentSnapshot> EjerciciosDocs = EjerciciosRef.get().get().getDocuments();
				System.out.println("Ejercicios encontrado para workout " + doc.getId() + ": " + EjerciciosDocs.size());

				for (DocumentSnapshot docEjercicio : EjerciciosDocs) {
					Map<String, Object> h2 = new HashMap<>(docEjercicio.getData());
					h2.put("WorkoutdId", doc.getId());
					h2.put("EjerciciosId", docEjercicio.getId());
					h2.put("Nombre", docEjercicio.getString("Nombre"));
					h2.put("Descripcion", docEjercicio.getString("Descripcion"));
					h2.put("Nivel", docEjercicio.getDouble("Nivel"));
					DocumentReference refid = (DocumentReference) h2.get("Workout");
					h2.put("Workout", refid);
					h2.put("img", docEjercicio.getString("img"));
					h2.put("tiempoDescanso", docEjercicio.getDouble("tiempoDescanso"));
					EjerciciosList.add(h2);
					System.out.println("Registro de Ejercicio añadido: " + docEjercicio.getId());

					CollectionReference SeriesRef = doc.getReference().collection("Series");
					List<QueryDocumentSnapshot> SeriesDocs = SeriesRef.get().get().getDocuments();
					System.out.println(
							"Serie encontrado para Ejercicio " + docEjercicio.getId() + ": " + SeriesDocs.size());

					for (DocumentSnapshot docSerie : SeriesDocs) {
						Map<String, Object> h3 = new HashMap<>(docSerie.getData());
						h3.put("SerieId", docSerie.getId());
						h3.put("EjerciciosId", docEjercicio.getId());
						h3.put("Nombre", docSerie.getString("Nombre"));
						h3.put("duracion", docSerie.getDouble("duracion"));
						h3.put("repeticiones", docSerie.getDouble("repeticiones"));

						SeriesList.add(h3);
						System.out.println("Registro de Ejercicio añadido: " + docEjercicio.getId());
					}
				}
			}

			// --- Guardar .dat y XML ---
			guardarBackupDat(usuariosList, workoutsList, EjerciciosList, SeriesList);
			guardarHistoricoXmlGlobal(historicoGlobal, db);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (db != null) {
				try {
					db.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) {
		System.out.println("Iniciando backup...");

		backup hiloBackup = new backup();
		hiloBackup.start();

		try {
			hiloBackup.join(); // Espera a que termine el hilo antes de salir
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("Backup finalizado.");
	}

	// --- GUARDAR BACKUP .DAT ---
	private void guardarBackupDat(List<Map<String, Object>> usuarios, List<Map<String, Object>> workouts,
			List<Map<String, Object>> ejercicios, List<Map<String, Object>> series) {
		try (DataOutputStream out = new DataOutputStream(new FileOutputStream("backup_global.dat"))) {

// --- Usuarios ---
			out.writeInt(usuarios.size());
			for (Map<String, Object> u : usuarios) {
				out.writeUTF(defaultString((String) u.get("Nombre"), "Desconocido"));
				out.writeUTF(defaultString((String) u.get("Apellido"), "Desconocido"));
				Date fecha = (Date) u.get("FecNac");
				if (fecha != null)
					out.writeUTF(fecha.toString());
				else
					out.writeUTF("Desconocida");
				out.writeUTF(defaultString((String) u.get("Email"), "email@desconocido.com"));
				out.writeUTF(defaultString((String) u.get("Contraseña"), "1234"));
				out.writeInt(defaultInt(u.get("nivel"), 1));
			}

// --- Workouts ---
			out.writeInt(workouts.size());
			for (Map<String, Object> w : workouts) {
				out.writeUTF(defaultString((String) w.get("nombre"), "WorkoutDesconocido"));
				out.writeUTF(defaultString((String) w.get("video"), ""));
				out.writeInt(defaultInt(w.get("nivel"), 1));
				out.writeInt(defaultInt(w.get("numEjer"), 0));
				out.writeUTF(defaultString((String) w.get("usuarioId"), "sinUsuario"));
			}

// --- Ejercicios ---
			out.writeInt(ejercicios.size());
			for (Map<String, Object> e : ejercicios) {
				out.writeUTF(defaultString((String) e.get("Nombre"), "EjercicioDesconocido"));
				out.writeUTF(defaultString((String) e.get("Descripcion"), ""));
				out.writeUTF(defaultString((String) e.get("WorkoutdId"), "sinWorkout"));
				out.writeUTF(defaultString((String) e.get("img"), ""));
				out.writeInt(defaultInt(e.get("Nivel"), 1));
				out.writeInt(defaultInt(e.get("tiempoDescanso"), 0));
			}

// --- Series ---
			out.writeInt(series.size());
			for (Map<String, Object> s : series) {
				out.writeUTF(defaultString((String) s.get("Nombre"), "SerieDesconocida"));
				out.writeUTF(defaultString((String) s.get("EjerciciosId"), "sinEjercicio"));
				out.writeInt(defaultInt(s.get("duracion"), 0));
				out.writeInt(defaultInt(s.get("repeticiones"), 0));
			}

			System.out.println(
					"Backup global (.dat) con usuarios, workouts, ejercicios y series guardado correctamente.");

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error al guardar backup .dat");
		}
	}

	// --- GUARDAR XML GLOBAL DEL HISTÓRICO ---
	private void guardarHistoricoXmlGlobal(List<Map<String, Object>> historico, Firestore db) {
		if (historico.isEmpty()) {
			System.out.println("No hay histórico para guardar.");
			return;
		}

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.newDocument();

			Element root = doc.createElement("Historico");
			doc.appendChild(root);

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			for (Map<String, Object> h : historico) {
				Element workoutElem = doc.createElement("Workouts");

				// Datos del usuario y del histórico
				workoutElem.setAttribute("usuarioId", defaultString((String) h.get("usuarioId"), "desconocido"));
				workoutElem.setAttribute("historicoId", defaultString((String) h.get("historicoId"), "sinId"));

				// Fecha del histórico
				Object fechaObj = h.get("fecha");
				Date fecha = new Date();
				if (fechaObj instanceof Timestamp) {
					fecha = ((Timestamp) fechaObj).toDate();
				}
				workoutElem.setAttribute("fecha", sdf.format(fecha));

				// Datos del histórico
				workoutElem.setAttribute("porcentajeCompletado",
						String.valueOf(defaultDouble(h.get("Porcentaje"), 0.0)));
				workoutElem.setAttribute("tiempoTotal", String.valueOf(defaultInt(h.get("tiempoTotal"), 0)));

				// Datos del workout real usando idWorkout
				Object refObj = h.get("idWorkout");
				DocumentSnapshot workoutDoc = null;

				if (refObj instanceof DocumentReference) {
					DocumentReference refWorkout = (DocumentReference) refObj;
					workoutDoc = refWorkout.get().get();
				} else if (refObj instanceof String) {
					String path = (String) refObj;
					DocumentReference refWorkout = db.document(path);
					workoutDoc = refWorkout.get().get();
				}

				if (workoutDoc != null) {
					workoutElem.setAttribute("nombre",
							defaultString(workoutDoc.getString("nombre"), "WorkoutDesconocido"));
					workoutElem.setAttribute("nivel", String.valueOf(defaultInt(workoutDoc.getLong("nivel"), 1)));
				} else {
					workoutElem.setAttribute("nombre", "WorkoutDesconocido");
					workoutElem.setAttribute("nivel", "1");
				}

				root.appendChild(workoutElem);
			}

			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

			transformer.transform(new DOMSource(doc), new StreamResult(new File("historico_global.xml")));

			System.out
					.println("Histórico global XML generado correctamente. Total: " + historico.size() + " registros.");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// --- MÉTODOS AUXILIARES ---
	private String defaultString(String valor, String defecto) {
		return (valor == null || valor.isEmpty()) ? defecto : valor;
	}

	private int defaultInt(Object valor, int defecto) {
		if (valor instanceof Number)
			return ((Number) valor).intValue();
		return defecto;
	}

	private double defaultDouble(Object valor, double defecto) {
		if (valor instanceof Number)
			return ((Number) valor).doubleValue();
		return defecto;
	}
}

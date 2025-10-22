package modelo;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.Firestore;

public class Backups extends Thread {

	@Override
	public void run() {
		Firestore db = null;
		try {
			db = ConectorFirebase.conectar();
			CollectionReference usuariosRef = db.collection("usuarios");
			ApiFuture<QuerySnapshot> future = usuariosRef.get();
			List<QueryDocumentSnapshot> usuariosDocs = future.get().getDocuments();

			System.out.println("Usuarios encontrados: " + usuariosDocs.size());

			// Creamos lista de todos los usuarios
			ArrayList<Usuarios> todosUsuarios = new ArrayList<>();
			ArrayList<HistoricoWorkouts> historicoGlobal = new ArrayList<>();
			for (DocumentSnapshot docUsuario : usuariosDocs) {
				Usuarios usuario = new Usuarios();
				usuario.setNombre(defaultString(docUsuario.getString("Nombre"), "UsuarioDesconocido"));
				usuario.setEmail(defaultString(docUsuario.getString("Email"), "email@desconocido.com"));
				usuario.setContraseña(defaultString(docUsuario.getString("Contraseña"), "1234"));
				usuario.setNivel(defaultInt(docUsuario.getDouble("nivel"), 1));
				todosUsuarios.add(usuario);

				// Guardamos el histórico XML individual
				ArrayList<Workout> workouts = new GestorWorkout().leerWorkoutsBDBackups();
				guardarBackup(todosUsuarios, workouts);

				guardarHistoricoXmlGlobal(historicoGlobal);
			}

			// Guardamos todos los usuarios y workouts en un solo .dat
			ArrayList<Workout> workouts = new GestorWorkout().leerWorkoutsBDBackups();
			guardarBackup(todosUsuarios, workouts);

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

	// --- DAT GLOBAL ---
	public String guardarBackup(List<Usuarios> usuarios, ArrayList<Workout> workouts) {
		if (usuarios == null || usuarios.isEmpty())
			return "No hay usuarios para guardar.";

		try (DataOutputStream out = new DataOutputStream(new FileOutputStream("backup_usuarios.dat"))) {

			out.writeInt(usuarios.size()); // Cantidad de usuarios

			for (Usuarios usuario : usuarios) {
				// Usuario
				out.writeUTF(defaultString(usuario.getNombre(), "UsuarioDesconocido"));
				out.writeUTF(defaultString(usuario.getEmail(), "email@desconocido.com"));
				out.writeUTF(defaultString(usuario.getContraseña(), "1234"));
				out.writeInt(defaultInt(usuario.getNivel(), 1));

				// Workouts
				if (workouts != null) {
					out.writeInt(workouts.size());
					for (Workout w : workouts) {
						out.writeUTF(defaultString(w.getNombre(), "WorkoutDesconocido"));
						out.writeUTF(defaultString(w.getVideo(), ""));
						out.writeInt(defaultInt(w.getNivel(), 1));
						out.writeInt(defaultInt(w.getNumEjers(), 0));

						if (w.getEjercicios() != null) {
							out.writeInt(w.getEjercicios().size());
							for (Ejercicios e : w.getEjercicios()) {
								out.writeUTF(defaultString(e.getNombre(), "EjercicioDesconocido"));
								out.writeUTF(defaultString(e.getDescripcion(), ""));
								out.writeUTF(defaultString(e.getImg(), ""));
								out.writeInt(defaultInt(e.getNivel(), 1));
								out.writeInt(defaultInt(e.getTiempoDescanso(), 0));

								if (e.getSeries() != null) {
									out.writeInt(e.getSeries().size());
									for (Series s : e.getSeries()) {
										out.writeUTF(defaultString(s.getNombre(), "SerieDesconocida"));
										out.writeInt(defaultInt(s.getRepeticiones(), 0));
										out.writeInt(defaultInt(s.getDuracion(), 0));
									}
								} else
									out.writeInt(0);
							}
						} else
							out.writeInt(0);
					}
				} else
					out.writeInt(0);
			}

			System.out.println("Backup global .dat guardado correctamente.");
			return "Backup global .dat guardado correctamente.";

		} catch (IOException e) {
			e.printStackTrace();
			return "Error al guardar backup global .dat";
		}
	}

	public String guardarHistoricoXmlGlobal(List<HistoricoWorkouts> historicoGlobal) {
		if (historicoGlobal == null || historicoGlobal.isEmpty()) {
			return "No hay histórico para guardar.";
		}

		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.newDocument();

			Element root = doc.createElement("HistoricoGlobal");
			doc.appendChild(root);

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			for (HistoricoWorkouts h : historicoGlobal) {
				Element workoutElem = doc.createElement("Workout");

				String nombre = h.getNombreWorkout();
				if (nombre == null || nombre.isEmpty()) {
					nombre = "WorkoutDesconocido";
				}
				workoutElem.setAttribute("nombre", nombre);

				int nivel = h.getNivel();
				workoutElem.setAttribute("nivel", String.valueOf(nivel));

				int tiempoTotal = h.getTiempoTotal();
				workoutElem.setAttribute("tiempoTotal", String.valueOf(tiempoTotal));

				int tiempoPrevisto = h.getTiempoPrevisto();
				workoutElem.setAttribute("tiempoPrevisto", String.valueOf(tiempoPrevisto));

				if (h.getFecha() != null) {
					workoutElem.setAttribute("fecha", sdf.format(h.getFecha()));
				} else {
					workoutElem.setAttribute("fecha", "1970-01-01 00:00:00");
				}

				double porcentaje = h.getPorcentajeCompletado();
				workoutElem.setAttribute("porcentajeCompletado", String.valueOf(porcentaje));

				String usuario = h.getUsuario();
				if (usuario == null || usuario.isEmpty()) {
					usuario = "desconocido";
				}
				workoutElem.setAttribute("usuario", usuario);

				root.appendChild(workoutElem);
			}

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("historico_global.xml"));
			transformer.transform(source, result);

			System.out.println("XML global generado correctamente: historico_global.xml");
			return "Histórico global XML guardado correctamente.";

		} catch (ParserConfigurationException | TransformerException e) {
			e.printStackTrace();
			return "Error al guardar histórico global XML";
		}
	}

	// --- MÉTODOS AUXILIARES ---
	private String defaultString(String valor, String defecto) {
		if (valor == null || valor.isEmpty()) {
			return defecto;
		} else {
			return valor;
		}
	}

	private int defaultInt(Number valor, int defecto) {
		if (valor == null) {
			return defecto;
		} else {
			return valor.intValue();
		}
	}
}
package modelo;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Backups extends Thread {

	private Usuarios usuario;
	private ArrayList<Workout> workouts; // Para .dat
	private ArrayList<HistoricoWorkouts> historico; // Para XML

	public Backups(Usuarios usuario, ArrayList<Workout> workouts, ArrayList<HistoricoWorkouts> historico) {
		this.usuario = usuario;
		this.workouts = workouts;
		this.historico = historico;
	}

	@Override
	public void run() {
		String resultadoDat = guardarBackup();
		String resultadoXml = guardarHistoricoXml();

		javax.swing.SwingUtilities.invokeLater(() -> {
			JOptionPane.showMessageDialog(null, resultadoDat + "\n" + resultadoXml, "Backup",
					JOptionPane.INFORMATION_MESSAGE);
		});
	}

	// --- DAT ---
	public String guardarBackup() {
		if (usuario == null)
			return "Error: usuario nulo";

		try (DataOutputStream out = new DataOutputStream(
				new FileOutputStream("backup_" + usuario.getNombre() + ".dat"))) {

			writeStringSafe(out, usuario.getNombre());
			writeStringSafe(out, usuario.getEmail());
			writeStringSafe(out, usuario.getContraseña());
			out.writeInt(usuario.getNivel());

			if (workouts != null) {
				out.writeInt(workouts.size());
				for (Workout w : workouts) {
					writeStringSafe(out, w.getNombre());
					writeStringSafe(out, w.getVideo());
					out.writeInt(w.getNivel());
					out.writeInt(w.getNumEjers());

					if (w.getEjercicios() != null) {
						out.writeInt(w.getEjercicios().size());
						for (Ejercicios e : w.getEjercicios()) {
							writeStringSafe(out, e.getNombre());
							writeStringSafe(out, e.getDescripcion());
							writeStringSafe(out, e.getImg());
							out.writeInt(e.getNivel());
							out.writeInt(e.getTiempoDescanso());

							if (e.getSeries() != null) {
								out.writeInt(e.getSeries().size());
								for (Series s : e.getSeries()) {
									writeStringSafe(out, s.getNombre());
									out.writeInt(s.getRepeticiones());
									out.writeInt(s.getDuracion());
								}
							} else
								out.writeInt(0);
						}
					} else
						out.writeInt(0);
				}
			} else
				out.writeInt(0);

			return "Backup .dat guardado correctamente para " + usuario.getNombre();

		} catch (IOException e) {
			e.printStackTrace();
			return "Error al guardar backup .dat para " + (usuario != null ? usuario.getNombre() : "desconocido");
		}
	}

	// --- XML ---
	public String guardarHistoricoXml() {
		if (usuario == null)
			return "Usuario no definido. No se puede guardar XML.";
		if (historico == null || historico.isEmpty())
			return "No hay histórico para guardar.";

		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.newDocument();

			Element root = doc.createElement("HistoricoWorkouts");
			doc.appendChild(root);

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			for (HistoricoWorkouts h : historico) {
				Element workoutElem = doc.createElement("Workout");
				workoutElem.setAttribute("nombre", h.getNombreWorkout());
				workoutElem.setAttribute("nivel", String.valueOf(h.getNivel()));
				workoutElem.setAttribute("tiempoTotal", String.valueOf(h.getTiempoTotal()));
				workoutElem.setAttribute("tiempoPrevisto", String.valueOf(h.getTiempoPrevisto()));
				workoutElem.setAttribute("fecha", sdf.format(h.getFecha()));
				workoutElem.setAttribute("porcentajeCompletado", String.valueOf(h.getPorcentajeCompletado()));
				root.appendChild(workoutElem);
			}

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("historico_" + usuario.getNombre() + ".xml"));
			transformer.transform(source, result);

			return "Histórico XML guardado correctamente para " + usuario.getNombre();

		} catch (ParserConfigurationException | TransformerException e) {
			e.printStackTrace();
			return "Error al guardar histórico XML para " + usuario.getNombre();
		}
	}

	private void writeStringSafe(DataOutputStream out, String str) throws IOException {
		if (str == null)
			str = "";
		out.writeInt(str.length());
		out.writeChars(str);
	}
}

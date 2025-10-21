package modelo;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Backups {

	public void guardarBackup(Usuarios usuario, ArrayList<Workout> workouts) {
		try (DataOutputStream out = new DataOutputStream(
				new FileOutputStream("backup_" + usuario.getNombre() + ".dat"))) {

			// --- Datos del usuario ---
			writeStringSafe(out, usuario.getNombre());
			writeStringSafe(out, usuario.getEmail());
			writeStringSafe(out, usuario.getContraseña());
			out.writeInt(usuario.getNivel());

			// --- Lista de workouts ---
			if (workouts != null) {
				out.writeInt(workouts.size());
				for (Workout w : workouts) {
					writeStringSafe(out, w.getNombre());
					writeStringSafe(out, w.getVideo());
					out.writeInt(w.getNivel());
					out.writeInt(w.getNumEjers());

					// --- Lista de ejercicios ---
					ArrayList<Ejercicios> ejercicios = w.getEjercicios();
					if (ejercicios != null) {
						out.writeInt(ejercicios.size());
						for (Ejercicios e : ejercicios) {
							writeStringSafe(out, e.getNombre());
							writeStringSafe(out, e.getDescripcion());
							writeStringSafe(out, e.getImg());
							out.writeInt(e.getNivel());
							out.writeInt(e.getTiempoDescanso());

							// --- Lista de series ---
							ArrayList<Series> series = e.getSeries();
							if (series != null) {
								out.writeInt(series.size());
								for (Series s : series) {
									writeStringSafe(out, s.getNombre());
									out.writeInt(s.getRepeticiones());
									out.writeInt(s.getDuracion());
								}
							} else {
								out.writeInt(0);
							}
						}
					} else {
						out.writeInt(0);
					}
				}
			} else {
				out.writeInt(0);
			}

			System.out.println("Backup guardado correctamente para " + usuario.getNombre());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Metodo auxiliar para escribir cadenas sin provocar NullPointerException
	private void writeStringSafe(DataOutputStream out, String str) throws IOException {
		if (str == null)
			str = "";
		out.writeInt(str.length());
		out.writeChars(str);
	}
}

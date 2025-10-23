package modelo;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class GestorBackup {

	public void llamarBackup() {
		try {
		    // 1️ Crear el proceso que ejecuta el backup
		    ProcessBuilder pb = new ProcessBuilder("java", "Backup.backup");
		    pb.directory(new File("bin"));
		    pb.redirectErrorStream(true); // combina salida estándar y errores
		    Process proceso = pb.start();

		    // 2️ Leer la salida del proceso línea a línea
		    InputStream is = proceso.getInputStream();
		    InputStreamReader isr = new InputStreamReader(is);
		    BufferedReader br = new BufferedReader(isr);

		    String linea;
		    while ((linea = br.readLine()) != null) {
		        System.out.println("[BACKUP] " + linea);
		    }

		    // 3️ Esperar a que el proceso termine
		    int codigoSalida = proceso.waitFor();
		    System.out.println("El proceso de backup terminó con código: " + codigoSalida);

		    // 4️ Cerrar recursos
		    br.close();
		    isr.close();
		    is.close();

		} catch (IOException | InterruptedException e) {
		    e.printStackTrace();
		}

	}
}

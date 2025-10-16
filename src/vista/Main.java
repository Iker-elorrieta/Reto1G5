package vista;

import javax.swing.SwingUtilities;

import modelo.ConectorFirebase;

public class Main {

	public static void main(String[] args) {
		try {
			// Inicializar Firebase
			ConectorFirebase.recogerConexion();

			// Mostrar ventana de registro
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					VentanaLogin login = new VentanaLogin();
					login.setVisible(true);
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

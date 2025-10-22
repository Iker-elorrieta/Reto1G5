package modelo;

import java.util.Date;

public class HistoricoWorkouts {

	 private String nombreWorkout;
	    private int nivel;
	    private int tiempoTotal;
	    private int tiempoPrevisto;
	    private Date fecha;
	    private double porcentajeCompletado;
	    private String usuario;
	    
	    public HistoricoWorkouts() {
	    	
	    }
	    
	    public HistoricoWorkouts(String nombreWorkout, int nivel, int tiempoTotal, int tiempoPrevisto, Date fecha, double porcentajeCompletado, String usuario) {
	        this.nombreWorkout = nombreWorkout;
	        this.nivel = nivel;
	        this.tiempoTotal = tiempoTotal;
	        this.tiempoPrevisto = tiempoPrevisto;
	        this.fecha = fecha;
	        this.porcentajeCompletado = porcentajeCompletado;
	        this.usuario=usuario;
	    }
	    
	    public String getNombreWorkout() {
	        return nombreWorkout;
	    }
	    public int getNivel() {
	        return nivel;
	    }
	    public int getTiempoTotal() {
	        return tiempoTotal;
	    }
	    public int getTiempoPrevisto() {
	        return tiempoPrevisto;
	    }
	    public Date getFecha() {
	        return fecha;
	    }
	    public double getPorcentajeCompletado() {
	        return porcentajeCompletado;
	    }

		public void setNombreWorkout(String nombreWorkout) {
			this.nombreWorkout = nombreWorkout;
		}

		public void setNivel(int nivel) {
			this.nivel = nivel;
		}

		public void setTiempoTotal(int tiempoTotal) {
			this.tiempoTotal = tiempoTotal;
		}

		public void setTiempoPrevisto(int tiempoPrevisto) {
			this.tiempoPrevisto = tiempoPrevisto;
		}

		public void setFecha(Date fecha) {
			this.fecha = fecha;
		}

		public void setPorcentajeCompletado(double porcentajeCompletado) {
			this.porcentajeCompletado = porcentajeCompletado;
		}

		public String getUsuario() {
			return usuario;
		}

		public void setUsuario(String usuario) {
			this.usuario = usuario;
		}
	    
	    
}

package modelo;

import java.util.Date;

public class HistoricoWorkouts {

	 private String nombreWorkout;
	    private int tiempoTotal;
	    private int tiempoPrevisto;
	    private Date fecha;
	    private double porcentajeCompletado;
	    private Workout workout;
	    
	    public HistoricoWorkouts() {
	    	
	    }
	    
	    public HistoricoWorkouts(String nombreWorkout,  int tiempoTotal, Date fecha, double porcentajeCompletado, Workout workout) {
	        this.nombreWorkout = nombreWorkout;
	        this.tiempoTotal = tiempoTotal;
	        this.tiempoPrevisto = calcularTiempoPrevisto(workout);
	        this.fecha = fecha;
	        this.porcentajeCompletado = porcentajeCompletado;
	        this.workout = workout;
	    }
	    
	    public String getNombreWorkout() {
	        return nombreWorkout;
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
		
	    public Workout getWorkout() {
			return workout;
		}

		public void setWorkout(Workout workout) {
			this.workout = workout;
			this.tiempoPrevisto = calcularTiempoPrevisto(workout);
		}
		
		private int calcularTiempoPrevisto(Workout workout) {
			
			int tiempoTotalPrevisto = 0;
			
			if(workout != null && workout.getEjercicios() != null) {
				
				for(Ejercicios ejercicio : workout.getEjercicios()) {
					int descansoEjercicio = ejercicio.getTiempoDescanso();
						if(ejercicio.getSeries() != null) {
							for(Series serie : ejercicio.getSeries()) {
							tiempoTotalPrevisto += serie.getDuracion(); //SON LO MISMO tiempoTotalPrevisto = tiempoTotalPrevisto + serie.getDuracion();
							}
						}
							tiempoTotalPrevisto += descansoEjercicio;
				}
					
			}
					return tiempoTotalPrevisto;
				}

		
		

	
}
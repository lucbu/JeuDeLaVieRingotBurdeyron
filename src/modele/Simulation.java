package modele;

public class Simulation extends Thread{
	private Simulable grille;
	private boolean pause;
	private boolean stop;
	private int tempsPause;
	
	public Simulation(Simulable grille, boolean pause, int tempsPause) {
		super();
		this.grille = grille;
		this.pause = pause;
		this.stop = false;
		this.tempsPause = tempsPause;
	}


	public Simulable getGrille() {
		return grille;
	}


	public void setGrille(Simulable grille) {
		this.grille = grille;
	}


	public int getTempsPause() {
		return tempsPause;
	}


	public void setTempsPause(int tempsPause) {
		this.tempsPause = tempsPause;
	}


	@Override
	public void run() {
		super.run();
		while (!isStop()) {
			try {
				Thread.currentThread().sleep(getTempsPause());
			if(pause)
			{
				synchronized(this){
					wait();
				};
			}
			grille.update();
				
			} catch (InterruptedException e) {
				System.err.println("Problème de slip du Thread ?");
				e.printStackTrace();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void pause()
	{
		setPause(true);
	}
	
	public void redemarrer()
	{
		setPause(false);
		synchronized(this){
			notify();
		}
	}
	
	public void unTour()
	{
		synchronized(this){
			notify();
		}
	}
	
	public void setPause(boolean pause) {
		this.pause = pause;
	}

	public boolean isPause() {
		return pause;
	}

	public boolean isStop() {
		return stop;
	}

	public void setStop(boolean stop) {
		this.stop = stop;
	}


	public void accelerer() {
		tempsPause *= 0.9; 
	}
	
	public void ralentir() {
		tempsPause *= 1.1; 
	}
}

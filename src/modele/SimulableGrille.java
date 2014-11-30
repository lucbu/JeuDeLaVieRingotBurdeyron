package modele;

import java.io.Serializable;
import java.util.Observer;

public class SimulableGrille extends Grille implements Simulable, Serializable {
	private static final long serialVersionUID = 1L;

	public SimulableGrille(int largeur, int hauteur, Observer observer, ModeEnum mode) {
		super(largeur, hauteur, observer, mode);
	}

	public SimulableGrille(Observer observer) {
		super(observer);
	}

	@Override
	public void update() {
		super.update();
	}
}

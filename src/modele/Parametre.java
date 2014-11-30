package modele;

import java.awt.Color;
import java.io.Serializable;

public class Parametre implements Serializable {
	private static final long serialVersionUID = -7942603654159256163L;
	
	/* Variables de gestion des préférences utilisateur */
	private Color couleurVie;
	private Color couleurMort;
	private Color couleurBordure;
	private Color couleurBordureSelection;
	private int tailleBordure;
	
	public Parametre() {
		//float[] hsbColor = Color.RGBtoHSB(r, g, b, null);
        //setCouleurVie(Color.getHSBColor(hsbColor[0], hsbColor[1], hsbColor[2]));
		this(Color.orange, Color.black, Color.black, Color.white, 1);
	}
	
	public Parametre(Color couleurVie, Color couleurMort, Color couleurBordure, Color couleurBordureSelection,
			int tailleBordure) {
		super();
		this.couleurVie = couleurVie;
		this.couleurMort = couleurMort;
		this.couleurBordure = couleurBordure;
		this.couleurBordureSelection = couleurBordureSelection;
		this.tailleBordure = tailleBordure;
	}
	
	public void copie(Parametre p) {
		this.couleurVie = p.couleurVie;
		this.couleurMort = p.couleurMort;
		this.couleurBordure = p.couleurBordure;
		this.couleurBordureSelection = p.couleurBordureSelection;
		this.tailleBordure = p.tailleBordure;
	}

	public Color getCouleurVie() {
		return couleurVie;
	}

	public void setCouleurVie(Color couleurVie) {
		this.couleurVie = couleurVie;
	}

	public Color getCouleurMort() {
		return couleurMort;
	}

	public void setCouleurMort(Color couleurMort) {
		this.couleurMort = couleurMort;
	}

	public Color getCouleurBordure() {
		return couleurBordure;
	}

	public void setCouleurBordure(Color couleurBordure) {
		this.couleurBordure = couleurBordure;
	}

	public int getTailleBordure() {
		return tailleBordure;
	}

	public void setTailleBordure(int tailleBordure) {
		this.tailleBordure = tailleBordure;
	}

	public Color getCouleurBordureSelection() {
		return couleurBordureSelection;
	}

	public void setCouleurBordureSelection(Color couleurBordureSelection) {
		this.couleurBordureSelection = couleurBordureSelection;
	}
}

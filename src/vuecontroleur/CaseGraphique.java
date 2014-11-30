package vuecontroleur;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class CaseGraphique extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private int h;
	private int l;
	
	public CaseGraphique(int hauteur, int largeur, Color color, Color bordure, int tailleBordure) { 
		this.h = hauteur;
		this.l = largeur;
		
		setBackground(color);
		setBorder(BorderFactory.createLineBorder(bordure,tailleBordure));
	}
	
	public void actualiserCouleur(Color color) {
		setBackground(color);
	}

	public int getH() {
		return h;
	}

	public void setH(int h) {
		this.h = h;
	}

	public int getL() {
		return l;
	}

	public void setL(int l) {
		this.l = l;
	}
	
}

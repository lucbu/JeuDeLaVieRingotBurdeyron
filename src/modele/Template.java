package modele;

import java.io.Serializable;

public class Template implements Serializable {
	private static final long serialVersionUID = 1L;

	private String nom;

	private int hauteur;
	private int largeur;
	private boolean[][] quadrillage;
	
	public Template(int hauteur, int largeur) {
		this.hauteur = hauteur;
		this.largeur = largeur;
		this.quadrillage = new boolean[hauteur][largeur];
	}

	public Template() {	}

	public String getNom() {
		return nom;
	}
	
	public int getHauteur() {
		return hauteur;
	}

	public void setHauteur(int hauteur) {
		this.hauteur = hauteur;
	}

	public int getLargeur() {
		return largeur;
	}

	public void setLargeur(int largeur) {
		this.largeur = largeur;
	}

	public boolean[][] getQuadrillage() {
		return quadrillage;
	}

	public void setQuadrillage(boolean[][] quadrillage) {
		this.quadrillage = quadrillage;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	@Override
	public String toString()
	{
		return getNom();
	}
}

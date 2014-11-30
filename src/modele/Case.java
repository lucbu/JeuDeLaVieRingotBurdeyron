package modele;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

public class Case implements Serializable {
	private static final long serialVersionUID = 1L;
	
	// Etat Vrai : vivant, faux : mort
	private boolean etatCourant;
	private boolean etatSuivant;

	public Case() {
        java.util.Random lRand = new Random();
        setEtatCourant(lRand.nextBoolean());
    }
	
	public Case(boolean etat) {
        setEtatCourant(etat);
    }
	
	/**
	 * Application des règles du jeu de la vie
	 * @param listeVoisines
	 */
	public void calculerEtatSuivant(List<Case> listeVoisines, Regle regles) {
		boolean lEtatSuivant = false;
		int nbVoisines = listeVoisines.size();
		
		for(Integer i : regles.getToBorn())
		{
			if(nbVoisines == i.intValue())
				lEtatSuivant = true;
		}
		if (isEtatCourant()) {
			for(Integer i : regles.getToSurvive())
			{
				if(nbVoisines == i.intValue())
					lEtatSuivant = true;
			}
		}
		/* Pour le debug
		System.out.println("Liste To Born : " + regles.getToBorn().toString());
		System.out.println("Liste To Survive : " + regles.getToSurvive().toString());
		
		System.out.println("nb Voisines: " + nbVoisines
				+ "\nEtat courant : " + (isEtatCourant() ? "Vivant" : "Mort")
				+ "\nEtat Suivant : " + (isEtatSuivant() ? "Vivant" : "Mort"));
		System.out.println();
		*/
		setEtatSuivant(lEtatSuivant);		
	}
	
	public void actualiserEtat() {
		setEtatCourant(isEtatSuivant());
	}
	
	public void changerEtat() {
		setEtatCourant(!isEtatCourant());
	}
	
	public boolean isEtatCourant() {
		return etatCourant;
	}

	public void setEtatCourant(boolean etatCourant) {
		this.etatCourant = etatCourant;
	}


	public boolean isEtatSuivant() {
		return etatSuivant;
	}


	public void setEtatSuivant(boolean etatSuivant) {
		this.etatSuivant = etatSuivant;
	}
}

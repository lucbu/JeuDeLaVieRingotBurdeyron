package modele;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

public class Grille extends Observable implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int largeur;
	private int hauteur;
	private Case[][] cases;
	private Regle regles;
	private List<ArrayList<Case>> listeAdjacence;
	private ModeEnum mode;
	
	public Grille(Observer observer) {
		this(12, 12, observer, ModeEnum.GRILLE);
	}
	
	public Grille(int largeur, int hauteur, Observer observer, ModeEnum mode) {
		super();
		this.largeur = largeur;
		this.hauteur = hauteur;
		this.mode = mode;
		setCases(new Case[hauteur][largeur]);
		setRegles(Regle.regleParDefaut());
		initCases();
		genererListeAdjacence();
		addObserver(observer);
	}
	
	private void genererListeAdjacence() {
		listeAdjacence = new ArrayList<ArrayList<Case>>();

		for (int h = 0; h < getHauteur(); h++) {
			for (int l = 0; l < getLargeur(); l++) {
				ArrayList<Case> listeVoisins = new ArrayList<Case>();
				for (int i = h-1; i <= h+1; i++) {
					for (int j = l-1; j <= l+1; j++) {
						if (getMode().equals(ModeEnum.GRILLE)) {
							if (i >= 0 && i < getHauteur() 
									&& j >= 0 && j < getLargeur() 
									&& (i != h || j != l)) {
								listeVoisins.add(getCases()[i][j]);
							}
						}
						else if (getMode().equals(ModeEnum.TORUS)) {
							int jj = j, ii = i;
							if(jj==-1)
								jj= this.getLargeur()-1;
							else if(jj==this.getLargeur())
								jj=0;
							if(ii==-1)
								ii= this.getHauteur()-1;
							else if(ii==this.getHauteur())
								ii=0;
							if(ii != h || jj != l) 
							{
								listeVoisins.add(getCases()[ii][jj]);
							}
						}
					}
				}
				listeAdjacence.add(listeVoisins);
			}
		}
	}
	
	public void update()
	{
		calculerEtatSuivant();
		System.out.println("Update de la Grille");
		setChanged();
		notifyObservers();
	}
	
	public void calculerEtatSuivant() {
		for (int i = 0; i < getHauteur(); i++) {
			for (int j = 0; j < getLargeur(); j++) {
				getCases()[i][j].calculerEtatSuivant(getVoisinsVivants(i, j), regles);
			}
		}
		
		for (int i = 0; i < getHauteur(); i++) {
			for (int j = 0; j < getLargeur(); j++) {
				getCases()[i][j].actualiserEtat();
			}
		}
	}
	
	public void initCases()
	{
		for (int i = 0; i < getHauteur(); i++) {
			for (int j = 0; j < getLargeur(); j++) {
				getCases()[i][j] = new Case();
			}
		}
	}
	
	public void regenerer() {
		java.util.Random lRand = new Random();
		for (int i = 0; i < getHauteur(); i++) {
			for (int j = 0; j < getLargeur(); j++) {
				getCases()[i][j].setEtatCourant(lRand.nextBoolean());
			}
		}
	}
	
	public List<Case> getVoisinsVivants(int h, int l) {
		List<Case> list = new ArrayList<Case>();
		for (Case c : listeAdjacence.get(h * getLargeur() + l)) {
			if (c.isEtatCourant())
				list.add(c);
		}
		return list;
	}

	public int getLargeur() {
		return largeur;
	}
	public void setLargeur(int largeur) {
		this.largeur = largeur;
	}
	public int getHauteur() {
		return hauteur;
	}
	public void setHauteur(int hauteur) {
		this.hauteur = hauteur;
	}
	public Case[][] getCases() {
		return cases;
	}
	public void setCases(Case[][] cases) {
		this.cases = cases;
	}

	public void initMort()
	{
		for (int i = 0; i < getHauteur(); i++) {
			for (int j = 0; j < getLargeur(); j++) {
				getCases()[i][j].setEtatCourant(false);
			}
		}
	}
	public void initVie()
	{
		for (int i = 0; i < getHauteur(); i++) {
			for (int j = 0; j < getLargeur(); j++) {
				getCases()[i][j] = new Case(true);
			}
		}
	}

	public void setRegles(Regle regles) {
		this.regles = regles;
	}

	public Regle getRegles() {
		return regles;
	}

	public ModeEnum getMode() {
		return mode;
	}

	public void setMode(ModeEnum mode) {
		this.mode = mode;
	}

	public void copie(Grille grilleTmp) {
		this.cases = grilleTmp.getCases();
		this.hauteur = grilleTmp.getHauteur();
		this.largeur = grilleTmp.getLargeur();
		this.listeAdjacence = grilleTmp.listeAdjacence;
		this.mode = grilleTmp.getMode();
		this.regles = grilleTmp.getRegles();
	}
	
	public void chargerGrille() {chargerGrille("grille.ser");}
	
	public void chargerGrille(String nomFichier) {
		try {
			FileInputStream fichier = new FileInputStream(nomFichier);
			ObjectInputStream ois = new ObjectInputStream(fichier);
			Grille grilleTmp = (Grille) ois.readObject();
			ois.close();
			fichier.close();
			copie(grilleTmp);
			System.out.println("Chargement de la grille et des règles");
			setChanged();
			notifyObservers();
		}
		catch (java.io.IOException e) {
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void enregistrerGrille() {enregistrerGrille("grille.ser");}
	
	public void enregistrerGrille(String nomFichier) {
		try {
			System.out.println("Enregistrement de la grille et des règles");
			FileOutputStream fichier = new FileOutputStream(nomFichier);
			ObjectOutputStream oos = new ObjectOutputStream(fichier);
			oos.writeObject(this);
			oos.flush();
			oos.close();
			fichier.close();
		}
		catch (java.io.IOException exception) {
			exception.printStackTrace();
		}
	}
	
}

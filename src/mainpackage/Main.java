package mainpackage;

import javax.swing.SwingUtilities;

import vuecontroleur.Vue;

public class Main {

	public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				//On cr�e l'instance de notre fen�tre
				Vue fenetre = new Vue();
				fenetre.setVisible(true);//On la rend visible
			}
		});
	}
}

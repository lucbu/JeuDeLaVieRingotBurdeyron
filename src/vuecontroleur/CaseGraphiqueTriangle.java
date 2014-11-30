package vuecontroleur;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;

public class CaseGraphiqueTriangle extends CaseGraphique {
	private static final long serialVersionUID = 1L;
	private boolean toUp;
	private Color bordure;
	
	public CaseGraphiqueTriangle(int hauteur, int largeur, Color color,
			Color bordure, int tailleBordure) {
		super(hauteur, largeur, color, bordure, tailleBordure);
		// TODO Auto-generated constructor stub
		
		this.bordure = bordure;
		
	}

	@Override
	public void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponents(g);
		int [] x = {0,super.getWidth()/2,super.getWidth()};
		int [] y = {0,super.getHeight(),0};
		Graphics2D g2 = (Graphics2D) g;
		
		Polygon p = new Polygon(x, y, 3);
		
		g2.setColor(bordure);
        g2.draw(p);
        g2.setColor(super.getBackground());
        g2.fillPolygon(p);
	}
}

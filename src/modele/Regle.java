package modele;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Regle implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public List<Integer> toBorn;
	public List<Integer> toSurvive;
	
	public Regle() {
		toBorn = new ArrayList<Integer>();
		
		toSurvive = new ArrayList<Integer>();
	}

	public Regle(List<Integer> toBorn, List<Integer> toSurvive) {
		this.toBorn = toBorn;
		this.toSurvive = toSurvive;
	}
	
	@Override
	public String toString() {
		return "Born:" + getToBorn() + ", Survive: " + getToSurvive();
	}

	public List<Integer> getToBorn() {
		return toBorn;
	}

	public void setToBorn(List<Integer> toBorn) {
		this.toBorn = toBorn;
	}

	public List<Integer> getToSurvive() {
		return toSurvive;
	}

	public void setToSurvive(List<Integer> toSurvive) {
		this.toSurvive = toSurvive;
	}

	static public Regle regleParDefaut() {


		List<Integer> toBorn2;
		List<Integer> toSurvive2;
		
		toBorn2 = new ArrayList<Integer>();
		toBorn2.add(3);
		
		toSurvive2 = new ArrayList<Integer>();
		toSurvive2.add(2);
		toSurvive2.add(3);
		
		return new Regle(toBorn2, toSurvive2);
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean equal = true;
		Regle laRegle = (Regle) obj;
		if (obj == null) {
			if (this == null)
				return true;
		}
		else {
			if (getToBorn().size() == laRegle.getToBorn().size()) {
				if (getToSurvive().size() == laRegle.getToSurvive().size()) {
					for (Integer i : getToBorn()) {
						if (! laRegle.getToBorn().contains(i))
							equal = false;
					}
				
					for (Integer i : getToSurvive()) {
						if (! laRegle.getToSurvive().contains(i))
							equal = false;
					}
				}
				else {
					equal = false;
				}
			}
			else {
				equal = false;
			}
		}
		return equal;
	}
	
}

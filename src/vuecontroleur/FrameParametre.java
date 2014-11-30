package vuecontroleur;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import modele.Parametre;

public class FrameParametre extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private Vue vue; 
	private Parametre parametres;
	private Color bordure;
	private Color mort;
	private Color vie;
	private int tbordure;
	
	JButton bt_LifeColor;
	JButton bt_DeathColor;
	JButton bt_BorderColor;
	JComboBox cb_BorderSize;

	public FrameParametre(Parametre p, Vue vue) {
        super();
        this.vue = vue;
        this.parametres = p;
        bordure = parametres.getCouleurBordure();
        tbordure = parametres.getTailleBordure();
        mort = parametres.getCouleurMort();
        vie = parametres.getCouleurVie();
        build();
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent arg0) {
            }
        });

    }
	
	public void majFrame()
	{
        bordure = parametres.getCouleurBordure();
        tbordure = parametres.getTailleBordure();
        mort = parametres.getCouleurMort();
        vie = parametres.getCouleurVie();
		bt_LifeColor.setBackground(vie);
		bt_DeathColor.setBackground(mort);
		bt_BorderColor.setBackground(bordure);
		cb_BorderSize.setSelectedItem(Integer.toString(tbordure));
	}
	
	public void chargerParametres() {
		try {
			FileInputStream fichier = new FileInputStream("param.ser");
			ObjectInputStream ois = new ObjectInputStream(fichier);
			Parametre parametres2 = (Parametre) ois.readObject();
			parametres.copie(parametres2);
			System.out.println("Params : ");
			System.out.println("Couleur vie : " + parametres.getCouleurVie().toString());
			majFrame();
			ois.close();
		}
		catch (java.io.IOException e) {
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void enregistrerParametres() {
		try {
			FileOutputStream fichier = new FileOutputStream("param.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fichier);
			oos.writeObject(parametres);
			oos.flush();
			oos.close();
		}
		catch (java.io.IOException exception) {
			exception.printStackTrace();
		}
	}
	
	public void build() 
	{
		JPanel panelPrinc = new JPanel();
		panelPrinc.setLayout(new BoxLayout(panelPrinc, BoxLayout.Y_AXIS));
		
		JPanel panelTb = new JPanel();
		panelTb.setLayout(new BoxLayout(panelTb, BoxLayout.Y_AXIS));
		JPanel panelBt1 = new JPanel();
		panelBt1.setLayout(new FlowLayout());
		JPanel panelBt2 = new JPanel();
		panelBt2.setLayout(new FlowLayout());
		
		JPanel panel1 = new JPanel();
		panel1.setLayout(new FlowLayout());
		JLabel jl_LifeColor = new JLabel("Choisir la couleur d'une cellule vivante :");
		bt_LifeColor = new JButton("Couleur vie");
		bt_LifeColor.setBackground(vie);
        bt_LifeColor.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
                        Color background = JColorChooser.showDialog(null,
                                "Choix de la couleur d'une cellule vivante", parametres.getCouleurVie());
                        if (background != null) {
                        	vie = background;
                        	parametres.setCouleurVie(vie);
                        	((JButton)e.getSource()).setBackground(background);
                        	vue.actualiserCouleurs();
                        }
                    }
		});
		panel1.add(jl_LifeColor);
		panel1.add(bt_LifeColor);
		panelTb.add(panel1);
		
		JPanel panel2 = new JPanel();
		panel2.setLayout(new FlowLayout());
		JLabel jl_DeathColor = new JLabel("Choisir la couleur d'une cellule morte :");
		bt_DeathColor = new JButton("Couleur mort");
		bt_DeathColor.setBackground(mort);
        bt_DeathColor.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
                        Color background = JColorChooser.showDialog(null,
                                "Choix de la couleur d'une cellule morte", parametres.getCouleurMort());
                        
                        if (background != null) {
                        	mort = background;
                        	parametres.setCouleurMort(mort);
                            ((JButton)e.getSource()).setBackground(background);
                        	vue.actualiserCouleurs();
                        }
                    }
		});
		panel2.add(jl_DeathColor);
		panel2.add(bt_DeathColor);
		panelTb.add(panel2);
		
		JPanel panel3 = new JPanel();
		panel3.setLayout(new FlowLayout());
		JLabel jl_BorderColor = new JLabel("Choisir la couleur de la bordure :");
		bt_BorderColor = new JButton("Couleur bordure");
		bt_BorderColor.setBackground(bordure);
        bt_BorderColor.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
                        Color background = JColorChooser.showDialog(null,
                                "Choix de la couleur des bordures", parametres.getCouleurBordure());
                        if (background != null) {
                        	bordure = background;
                        	parametres.setCouleurBordure(bordure);
                        	((JButton)e.getSource()).setBackground(background);
                        	vue.actualiserCouleurs();
                        }
                    }
		});
		panel3.add(jl_BorderColor);
		panel3.add(bt_BorderColor);
		panelTb.add(panel3);
		
		String[] borderSizeValues = {"0", "1", "2"};
		JPanel panel4 = new JPanel();
		panel4.setLayout(new FlowLayout());
		JLabel jl_BorderSize = new JLabel("Choisir la taille de la bordure :");
		cb_BorderSize = new JComboBox(borderSizeValues);
		cb_BorderSize.setSelectedItem(Integer.toString(tbordure));
		cb_BorderSize.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				tbordure = Integer.parseInt(((JComboBox)arg0.getSource()).getSelectedItem().toString());
				parametres.setTailleBordure(tbordure);
			}
		});
		panel4.add(jl_BorderSize);
		panel4.add(cb_BorderSize);
		panelTb.add(panel4);

        JButton btCharger = new JButton("Charger");
        btCharger.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent event) {
        		super.mouseClicked(event);
        		chargerParametres();
        	}
		});
        JButton btEnregistrer = new JButton("Enregistrer");
        btEnregistrer.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		super.mouseClicked(e);
        		enregistrerParametres();
        	}
		});

        panelBt1.add(btCharger);
        panelBt1.add(btEnregistrer);

        JButton btAppliquer = new JButton("Appliquer");
        btAppliquer.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		parametres.setTailleBordure(tbordure);
        		parametres.setCouleurBordure(bordure);
        		parametres.setCouleurMort(mort);
            	parametres.setCouleurVie(vie);
            }
		});
        
        JButton btOk = new JButton("Ok");
        btOk.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		parametres.setTailleBordure(tbordure);
        		parametres.setCouleurBordure(bordure);
        		parametres.setCouleurMort(mort);
            	parametres.setCouleurVie(vie);
        		fermer();
                    }
		});

        JButton btAnnuler = new JButton("Annuler");
        btAnnuler.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		fermer();
                    }
		});

        panelBt2.add(btAppliquer);
        panelBt2.add(btOk);
        panelBt2.add(btAnnuler);
        
        setTitle("Modification des paramètres");
        setSize(400, 400);
        panelPrinc.add(panelTb);
        panelPrinc.add(panelBt1);
        panelPrinc.add(panelBt2);
        
        add(panelPrinc);
	}

	public Parametre getParametres() {
		return parametres;
	}

	public void setParametres(Parametre parametres) {
		this.parametres = parametres;
	}
	
	public void fermer()
	{
		this.dispose();
	}
}

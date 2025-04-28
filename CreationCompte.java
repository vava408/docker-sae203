import javax.swing.*;
import java.awt.GridLayout;

public class CreationCompte extends JFrame
{
	private JPanel panelPrc;
	private JButton btnValider;
	private JTextField txtNom;
	
	public CreationCompte()
	{
		this.setTitle("Création de compte");
		//this.setLayout(new GridLayout(5,1));
		
		this.setSize(300,300);
		
		this.panelPrc = new JPanel() ;
		this.panelPrc.setLayout(new GridLayout(5,1));
		
		this.txtNom = new JTextField();
		
		
		this.btnValider = new JButton("Valider");
		
		
		
		this.panelPrc.add(this.btnValider);
		this.panelPrc.add(this.txtNom);
		
		this.add(this.panelPrc);
		
		
		this.setVisible(true);
		
		
	}
	
	public static void main(String[] args)
	{
		new CreationCompte();
	}
}

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.*;
import javax.swing.border.Border;

public class PanelHaut extends JPanel
{
	private ImageIcon img;
	private JLabel lblDescription;
	private JLabel lblImg;
	private JButton btnLike;
	private JButton btnSwipe;

	public PanelHaut()
	{
		JPanel panelPrinc = new JPanel(new BorderLayout());
		JPanel panelBtn = new JPanel();
		JPanel panelHaut = new JPanel();
		JPanel panelCentre = new JPanel();

		this.img = new ImageIcon("hello-world.jpg");
		this.lblDescription = new JLabel("Test de la Description");
		this.lblImg = new JLabel(this.img);
		this.btnLike = new JButton("Like");
		this.btnSwipe = new JButton("Swipe");

		panelHaut.add(this.lblImg);
		panelPrinc.add(panelHaut, BorderLayout.NORTH);

		panelCentre.add(this.lblDescription);
		panelPrinc.add(panelCentre, BorderLayout.CENTER);

		panelBtn.add(this.btnLike);
		panelBtn.add(this.btnSwipe);
		panelPrinc.add(panelBtn, BorderLayout.SOUTH);

		this.add(panelPrinc);
		
		this.setVisible(true);
	}
}

import java.awt.FlowLayout;

import javax.swing.*;

public class PanelHaut extends JPanel
{
	private ImageIcon img;
	private JLabel lblDescription;
	private JLabel lblImg;
	private JButton btnLike;
	private JButton btnSwipe;

	public PanelHaut()
	{
		JPanel panelBtn = new JPanel();
		this.img = new ImageIcon();
		this.lblDescription = new JLabel("Test de la Description");
		this.lblImg = new JLabel(this.img);
		this.btnLike = new JButton("Like");
		this.btnSwipe = new JButton("Swipe");

		this.add(this.lblDescription);
		this.add(this.lblImg);

		panelBtn.add(this.btnLike);
		panelBtn.add(this.btnSwipe);

		this.add(panelBtn, new FlowLayout(FlowLayout.LEFT) );
		
		this.setVisible(true);
	}
}

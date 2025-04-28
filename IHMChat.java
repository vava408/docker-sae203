
import java.awt.GridLayout;
import javax.swing.*;


public class IHMChat extends JFrame
{
	private Panel1Chat panel;
	public IHMChat()
	{
		this.panel =new Panel1Chat();
		this.setSize(500, 600);

		// Positionnement des composants
		this.add(panel);

		this.setVisible(true);
	}
}
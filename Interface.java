import javax.swing.*;
import java.awt.BorderLayout;

public class Interface extends JFrame
{
	private PanelHaut panelHaut;

	public Interface()
	{
		this.setTitle("Zinner");
		this.setSize(500, 900);
		//this.setLayout(new BorderLayout());

		this.panelHaut = new PanelHaut();
		
		this.add(panelHaut);

		this.setVisible(true);
	}

	public static void main(String[] a)
	{
		new Interface();
	}
}
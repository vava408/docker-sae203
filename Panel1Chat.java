import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class Panel1Chat extends JPanel implements ActionListener
{
	private JTextField envoieMessage;
	private JButton btnEnvoyer;
	private JTextArea historiqueMessages; // Zone d'affichage des messages reçus
	private JScrollPane scrollHistorique; // Utilisation correcte de JScrollPane
	private String message;

	public Panel1Chat()
	{
		this.setLayout(new BorderLayout());

		// Zone d'affichage des messages avec JScrollPane
		this.historiqueMessages = new JTextArea(10, 30);
		this.historiqueMessages.setEditable(false);
		this.scrollHistorique = new JScrollPane(this.historiqueMessages);
		this.scrollHistorique.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		// Champ de texte et bouton d'envoi
		JPanel panelBas = new JPanel();
		this.envoieMessage = new JTextField(20);
		this.btnEnvoyer = new JButton("Envoyer");
;

		panelBas.add(this.envoieMessage);
		panelBas.add(this.btnEnvoyer);


		// Ajout des composants au panel
		this.add(scrollHistorique, BorderLayout.CENTER);
		this.add(panelBas, BorderLayout.SOUTH);


		// Listeners pour envoyer le message
		this.envoieMessage.addActionListener(this);
		this.btnEnvoyer.addActionListener(this);


	}

	public void actionPerformed(ActionEvent e)
	{
		message = this.envoieMessage.getText().trim();

		//pour envoyer avec le boutton 
		if(e.getSource() == this.btnEnvoyer)
		{
			message = this.envoieMessage.getText().trim();
			if (!message.isEmpty())
			{
				System.out.println("Message envoyé : " + message);
				//envoyerMessage(message); 
				this.envoieMessage.setText(""); 
				this.update();
			}
		}
	}






	//uptadde les messges envoyer par le serveur
	public void update(String serveurMessage, String clientServeur)
	{
		// Ajoute le message reçu du serveur
		this.historiqueMessages.append(clientServeur + serveurMessage + "\n");
		// Faire défiler automatiquement vers le bas après l'ajout d'un message
		this.historiqueMessages.setCaretPosition(this.historiqueMessages.getDocument().getLength());
	}

	//update les messages envoyer par le client
	public void update()
	{
		//ajoute le message envoyer par le client
		this.historiqueMessages.append("Client : " + message + "\n");
		// Faire défiler automatiquement vers le bas après l'ajout d'un message
		this.historiqueMessages.setCaretPosition(this.historiqueMessages.getDocument().getLength());
	}
}

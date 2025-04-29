import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class Chat extends JFrame
{
	private Controleur controleur;
	private JTextArea  conversationArea;
	private JTextField messageField;
	private JButton    sendButton;
	private JButton    nextButton;
	private JButton    previousButton;
	private JLabel     interlocuteurLabel;
	private JLabel     photoLabel;
	
	private Profil interlocuteur;
	private int    currentMatchIndex = 0;
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
	
	public Chat(Controleur controleur)
	{
		this.controleur = controleur;
		setupUI();
	}
	
	private void setupUI()
	{
		setTitle("ZINDER - Chat");
		setSize(450, 600);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		JPanel headerPanel = new JPanel(new BorderLayout());
		
		JPanel navigationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JLabel backButton = new JLabel("← Retour");
		backButton.setFont(new Font("Arial", Font.BOLD, 16));
		backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		backButton.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				setVisible(false);
			}
		});
		
		previousButton = new JButton("◀ Précédent");
		previousButton.addActionListener(e -> afficherMatchPrecedent());
		
		nextButton = new JButton("Suivant ▶");
		nextButton.addActionListener(e -> afficherMatchSuivant());
		
		navigationPanel.add(previousButton);
		navigationPanel.add(nextButton);
		
		JPanel interlocuteurPanel = new JPanel();
		interlocuteurPanel.setLayout(new BoxLayout(interlocuteurPanel, BoxLayout.Y_AXIS));
		
		photoLabel = new JLabel();
		photoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		photoLabel.setPreferredSize(new Dimension(100, 100));
		
		interlocuteurLabel = new JLabel("Aucun match");
		interlocuteurLabel.setFont(new Font("Arial", Font.BOLD, 16));
		interlocuteurLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		interlocuteurPanel.add(photoLabel);
		interlocuteurPanel.add(Box.createVerticalStrut(10));
		interlocuteurPanel.add(interlocuteurLabel);
		
		headerPanel.add(backButton, BorderLayout.WEST);
		headerPanel.add(navigationPanel, BorderLayout.NORTH);
		headerPanel.add(interlocuteurPanel, BorderLayout.CENTER);
		
		conversationArea = new JTextArea();
		conversationArea.setEditable(false);
		conversationArea.setLineWrap(true);
		conversationArea.setWrapStyleWord(true);
		JScrollPane scrollPane = new JScrollPane(conversationArea);
		
		JPanel sendPanel = new JPanel(new BorderLayout(10, 0));
		messageField     = new JTextField();
		sendButton       = new JButton("Envoyer");
		sendButton.addActionListener(e -> envoyerMessage());
		
		messageField.addActionListener(e -> envoyerMessage());
		
		sendPanel.add(messageField, BorderLayout.CENTER);
		sendPanel.add(sendButton, BorderLayout.EAST);
		
		mainPanel.add(headerPanel, BorderLayout.NORTH);
		mainPanel.add(scrollPane, BorderLayout.CENTER);
		mainPanel.add(sendPanel, BorderLayout.SOUTH);
		
		add(mainPanel);
		setLocationRelativeTo(null);
	}
	
	private void envoyerMessage()
	{
		String texteMessage = messageField.getText().trim();
		if (!texteMessage.isEmpty() && interlocuteur != null)
		{
			controleur.envoyerMessage(interlocuteur.getId(), texteMessage);

			afficherMessageSortant(texteMessage);
			messageField.setText("");
		}
	}
	
	public void setInterlocuteur(Profil interlocuteur)
	{
		this.interlocuteur = interlocuteur;
		
		if (interlocuteur != null)
		{
			setTitle("ZINDER - Chat avec " + interlocuteur.getNom());
			interlocuteurLabel.setText(interlocuteur.getNom());
			
			if (interlocuteur.getPhoto() != null)
			{
				Image img    = interlocuteur.getPhoto().getImage();
				Image newImg = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
				ImageIcon resizedIcon = new ImageIcon(newImg);
				photoLabel.setIcon(resizedIcon);
			}
			else
			{
				photoLabel.setIcon(null);
			}
			
			
			chargerHistoriqueMessages();
			
			
			mettreAJourNavigation();
		} 
		else
		{
			interlocuteurLabel.setText("Aucun match");
			photoLabel.setIcon(null);
			conversationArea.setText(""); 
		}
	}
	
	private void afficherMatchSuivant() 
	{
		List<Profil> matchs = controleur.getMatchs();
		if (matchs.isEmpty()) return;
		
		currentMatchIndex = (currentMatchIndex + 1) % matchs.size();
		setInterlocuteur(matchs.get(currentMatchIndex));
	}
	
	private void afficherMatchPrecedent() 
	{
		List<Profil> matchs = controleur.getMatchs();
		if (matchs.isEmpty()) return;
		
		currentMatchIndex = (currentMatchIndex - 1 + matchs.size()) % matchs.size();
		setInterlocuteur(matchs.get(currentMatchIndex));
	}
	
	public String getIdInterlocuteur()
	{
		return interlocuteur != null ? interlocuteur.getId() : "";
	}
	
	public void afficherMessage(Message message) 
	{
		if (interlocuteur != null && message.getExpediteur().equals(interlocuteur.getId())) 
			afficherMessageEntrant(message.getContenu(), message.getDateEnvoi());
	}
	
	private void afficherMessageEntrant(String message, Date date)
	{
		conversationArea.append("\n" + interlocuteur.getNom() + " [" + dateFormat.format(date) + "]: " + message);
		conversationArea.setCaretPosition(conversationArea.getDocument().getLength());
	}
	
	private void afficherMessageSortant(String message)
	{
		conversationArea.append("\nMoi [" + dateFormat.format(new Date()) + "]: " + message);
		conversationArea.setCaretPosition(conversationArea.getDocument().getLength());
	}
	
	public void mettreAJourNavigation()
	{
		List<Profil> matchs = controleur.getMatchs();
		previousButton.setEnabled(matchs.size() > 1);
		nextButton.setEnabled(matchs.size() > 1);
	}
	
	private void chargerHistoriqueMessages()
	{
		if (interlocuteur == null || controleur.getUtilisateurCourant() == null) return;
		
		List<Message> historique = controleur.getMessagesAvecInterlocuteur(interlocuteur.getId());
		if (historique != null && !historique.isEmpty())
		{
			historique.sort(Comparator.comparing(Message::getDateEnvoi));
			
			conversationArea.setText("");
			
			for (Message message : historique)
			{
				if (message.getExpediteur().equals(controleur.getUtilisateurCourant().getId()))
				{
					conversationArea.append("\nMoi [" + dateFormat.format(message.getDateEnvoi()) + "]: " + message.getContenu());
				}
				else
				{
					afficherMessageEntrant(message.getContenu(), message.getDateEnvoi());
				}
			}
			
			conversationArea.setCaretPosition(conversationArea.getDocument().getLength());
		}
	}
}

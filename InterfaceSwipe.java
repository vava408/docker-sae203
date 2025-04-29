import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class InterfaceSwipe extends JFrame
{
	private Controleur controleur;
	private JPanel     mainPanel;
	private JLabel     photoLabel;
	private JLabel     nomLabel;
	private JTextArea  descriptionArea;
	private JButton    likeButton;
	private JButton    passButton;
	private JButton    chatButton;
	
	private List<Profil> profils;
	private int          indexProfilCourant = 0;
	private Random       random             = new Random();
	
	public InterfaceSwipe(Controleur controleur) {
		this.controleur = controleur;
		this.profils    = new ArrayList<>();
		setupUI();
	}
	
	private void setupUI() {
		setTitle("ZINDER - Découvrir");
		setSize(450, 700);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		mainPanel = new JPanel(new BorderLayout(10, 10));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel logoLabel   = new JLabel("Z");
		logoLabel.setFont(new Font("Arial", Font.BOLD, 28));
		logoLabel.setForeground(Color.WHITE);
		logoLabel.setOpaque(true);
		logoLabel.setBackground(Color.MAGENTA);
		logoLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		logoLabel.setPreferredSize(new Dimension(40, 40));
		logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		JLabel appNameLabel = new JLabel("INDER");
		appNameLabel.setFont(new Font("Arial", Font.BOLD, 24));
		
		chatButton = new JButton("Chat");
		chatButton.addActionListener(e -> {
			controleur.ouvrirChat();
		});
		
		headerPanel.add(logoLabel);
		headerPanel.add(appNameLabel);
		headerPanel.add(Box.createHorizontalGlue());
		headerPanel.add(chatButton);
		
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
		
		photoLabel = new JLabel();
		photoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		photoLabel.setPreferredSize(new Dimension(350, 350));
		photoLabel.setMinimumSize(new Dimension(350, 350));
		photoLabel.setMaximumSize(new Dimension(350, 350));
		photoLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		
		nomLabel = new JLabel("");
		nomLabel.setFont(new Font("Arial", Font.BOLD, 20));
		nomLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		descriptionArea = new JTextArea();
		descriptionArea.setEditable(false);
		descriptionArea.setLineWrap(true);
		descriptionArea.setWrapStyleWord(true);
		descriptionArea.setAlignmentX(Component.CENTER_ALIGNMENT);
		descriptionArea.setBackground(mainPanel.getBackground());
		descriptionArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		JScrollPane scrollPane = new JScrollPane(descriptionArea);
		scrollPane.setBorder(null);
		scrollPane.setPreferredSize(new Dimension(350, 120));
		scrollPane.setMinimumSize(new Dimension(350, 120));
		scrollPane.setMaximumSize(new Dimension(350, 120));
		scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		centerPanel.add(Box.createVerticalStrut(10));
		centerPanel.add(photoLabel);
		centerPanel.add(Box.createVerticalStrut(15));
		centerPanel.add(nomLabel);
		centerPanel.add(Box.createVerticalStrut(10));
		centerPanel.add(scrollPane);
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
		
		passButton = new JButton("Passer");
		passButton.setFont(new Font("Arial", Font.BOLD, 16));
		passButton.addActionListener(e -> {
			if (indexProfilCourant < profils.size()) 
			{
				controleur.passerProfil(profils.get(indexProfilCourant));
				afficherProfilSuivant();
			}
		});
		
		likeButton = new JButton("Like");
		likeButton.setFont(new Font("Arial", Font.BOLD, 16));
		likeButton.setBackground(new Color(255, 105, 180)); // Rose
		likeButton.setForeground(Color.WHITE);
		likeButton.addActionListener(e -> {
			if (indexProfilCourant < profils.size()) 
			{
				controleur.likerProfil(profils.get(indexProfilCourant));
				afficherProfilSuivant();
			}
		});
		
		buttonPanel.add(passButton);
		buttonPanel.add(likeButton);
		
		mainPanel.add(headerPanel, BorderLayout.NORTH);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);
		
		add(mainPanel);
		setLocationRelativeTo(null);
	}
	
	public void mettreAJourProfils(List<Profil> profils) {
		this.profils = profils;
		
		List<Profil> profilsInfinis = new ArrayList<>();
		
		if (!profils.isEmpty()) 
		{
			
			for (int i = 0; i < 10; i++) 
				profilsInfinis.addAll(profils);
			
			this.profils = melanger(profilsInfinis);
		}
		
		indexProfilCourant = 0;
		
		if (!this.profils.isEmpty())
		{
			afficherProfil(this.profils.get(0));
		} 
		else
		{
			photoLabel.setIcon(null);
			nomLabel.setText("Aucun profil disponible");
			descriptionArea.setText("Revenez plus tard!");
		}
	}
	
	private List<Profil> melanger(List<Profil> liste)
	{
		List<Profil> resultat = new ArrayList<>(liste);
		for (int i = resultat.size() - 1; i > 0; i--)
		{
			int j = random.nextInt(i + 1);
			Profil temp = resultat.get(i);
			resultat.set(i, resultat.get(j));
			resultat.set(j, temp);
		}
		return resultat;
	}
	
	public void afficherProfilSuivant()
	{
		indexProfilCourant++;
		
		if (indexProfilCourant >= profils.size()) 
		{
			profils = melanger(profils);
			indexProfilCourant = 0;
		}
		
		if (!profils.isEmpty())
		{
			afficherProfil(profils.get(indexProfilCourant));
		}
	}
	
	private void afficherProfil(Profil profil) 
	{
		if (profil.getPhoto() != null)
		{
			Image img = profil.getPhoto().getImage();
			Image newImg = img.getScaledInstance(350, 350, Image.SCALE_SMOOTH);
			ImageIcon resizedIcon = new ImageIcon(newImg);
			photoLabel.setIcon(resizedIcon);
		}
		else
		{
			photoLabel.setIcon(null);
		}
		
		nomLabel.setText(profil.getNom());
		descriptionArea.setText(profil.getDescription());
	}
	
	public void afficherNotificationMatch(Profil profil)
	{
		JOptionPane.showMessageDialog(this,
			"Vous avez un match avec " + profil.getNom() + "!\nVous pouvez maintenant discuter ensemble.",
			"Nouveau Match!",
			JOptionPane.INFORMATION_MESSAGE,
			profil.getPhoto());
	}
	
	public void afficherNotificationMessage(Message message) 
	{
		JOptionPane.showMessageDialog(this,
			"Nouveau message reçu!",
			"Message",
			JOptionPane.INFORMATION_MESSAGE);
	}
}

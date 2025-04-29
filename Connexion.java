import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;

public class Connexion extends JFrame 
{
	private JTextField identifiantField;
	private JPasswordField motDePasseField;
	private JButton connexionButton;
	private JButton creerCompteButton;
	private Controleur controleur;

	public Connexion(Controleur controleur)
	{
		this.controleur = controleur;
		
		setTitle("ZINDER - Connexion");
		setSize(400, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout(10, 10));
		mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
		
		JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JLabel logoLabel = new JLabel("Z");
		logoLabel.setFont(new Font("Arial", Font.BOLD, 28));
		logoLabel.setForeground(Color.WHITE);
		logoLabel.setOpaque(true);
		logoLabel.setBackground(Color.MAGENTA);
		logoLabel.setBorder(new LineBorder(Color.BLACK, 2));
		logoLabel.setPreferredSize(new Dimension(40, 40));
		logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		JLabel appNameLabel = new JLabel("INDER");
		appNameLabel.setFont(new Font("Arial", Font.BOLD, 24));
		
		headerPanel.add(logoLabel);
		headerPanel.add(appNameLabel);
		
		JPanel formPanel = new JPanel();
		formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
		
		JPanel identifiantPanel = new JPanel(new BorderLayout(10, 5));
		JLabel identifiantLabel = new JLabel("Identifiant");
		identifiantField = new JTextField();
		identifiantField.setPreferredSize(new Dimension(250, 30));
		identifiantPanel.add(identifiantLabel, BorderLayout.NORTH);
		identifiantPanel.add(identifiantField, BorderLayout.CENTER);
		
		JPanel motDePassePanel = new JPanel(new BorderLayout(10, 5));
		JLabel motDePasseLabel = new JLabel("Mot de passe");
		motDePasseField        = new JPasswordField();
		motDePasseField.setPreferredSize(new Dimension(250, 30));
		motDePassePanel.add(motDePasseLabel, BorderLayout.NORTH);
		motDePassePanel.add(motDePasseField, BorderLayout.CENTER);
		
		formPanel.add(Box.createVerticalStrut(50));
		formPanel.add(identifiantPanel);
		formPanel.add(Box.createVerticalStrut(20));
		formPanel.add(motDePassePanel);
		formPanel.add(Box.createVerticalStrut(50));
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		
		connexionButton = new JButton("SE CONNECTER");
		connexionButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		connexionButton.setBackground(new Color(50, 205, 50));
		connexionButton.setForeground(Color.BLACK);
		connexionButton.setFont(new Font("Arial", Font.BOLD, 16));
		connexionButton.setPreferredSize(new Dimension(200, 40));
		connexionButton.setMaximumSize(new Dimension(200, 40));
		
		creerCompteButton = new JButton("CRÉER UN COMPTE");
		creerCompteButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		creerCompteButton.setBackground(new Color(135, 206, 250));
		creerCompteButton.setForeground(Color.BLACK);
		creerCompteButton.setFont(new Font("Arial", Font.BOLD, 16));
		creerCompteButton.setPreferredSize(new Dimension(200, 40));
		creerCompteButton.setMaximumSize(new Dimension(200, 40));
		
		buttonPanel.add(connexionButton);
		buttonPanel.add(Box.createVerticalStrut(20));
		buttonPanel.add(creerCompteButton);
		
		connexionButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				String identifiant = identifiantField.getText().trim();
				String motDePasse  = new String(motDePasseField.getPassword());
				
				if (identifiant.isEmpty() || motDePasse.isEmpty()) 
				{
					JOptionPane.showMessageDialog(Connexion.this, 
						"Veuillez remplir tous les champs", 
						"Erreur", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				controleur.connecterUtilisateur(identifiant, motDePasse);
			}
		});
		
		creerCompteButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				controleur.afficherCreationCompte();
				setVisible(false);
			}
		});
		
		// Assembler les panels
		mainPanel.add(headerPanel, BorderLayout.NORTH);
		mainPanel.add(formPanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);
		
		add(mainPanel);
	}
	
	public void afficherErreurConnexion(String message)
	{
		JOptionPane.showMessageDialog(this, message, "Erreur de connexion", JOptionPane.ERROR_MESSAGE);
	}
}

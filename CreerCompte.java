import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.border.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class CreerCompte extends JFrame {
	private JTextField     nomPrenomField;
	private JTextArea      descriptionArea;
	private JPasswordField motDePasseField;
	private JLabel         photoLabel;
	private JButton        uploadButton;
	private JButton        confirmButton;
	private File           selectedImage = null;
	private ImageIcon      profileImage  = null;
	
	private Controleur controleur;

	public CreerCompte(Controleur controleur)
	{
		this.controleur = controleur;
		
		setTitle("ZINDER");
		setSize(400, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout(10, 10));
		mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
		
		JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel logoLabel = new JLabel("Z");
		logoLabel.setFont(new Font("Arial", Font.BOLD, 28));
		logoLabel.setForeground(Color.WHITE);
		logoLabel.setOpaque(true);
		logoLabel.setBackground(Color.MAGENTA);
		logoLabel.setBorder(new LineBorder(Color.BLACK, 2));
		logoLabel.setPreferredSize(new Dimension(40, 40));
		logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		JLabel appNameLabel = new JLabel("ZINDER");
		appNameLabel.setFont( new Font("Arial", Font.BOLD, 24));
		
		headerPanel.add(logoLabel);
		headerPanel.add(appNameLabel);
		
		JPanel formPanel  = new JPanel();
		formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
		
		JPanel photoPanel = new JPanel(new BorderLayout(10, 10));
		photoLabel        = new JLabel();
		photoLabel.setPreferredSize(new Dimension(150, 150));
		photoLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		photoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		uploadButton = new JButton("Upload");
		uploadButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				selectImage();
			}
		});
		
		photoPanel.add(photoLabel, BorderLayout.CENTER);
		photoPanel.add(uploadButton, BorderLayout.SOUTH);
		
		JPanel nomPanel = new JPanel(new BorderLayout(10, 5));
		JLabel nomLabel = new JLabel("Nom et prénom");
		nomPrenomField  = new JTextField();
		nomPrenomField.setPreferredSize(new Dimension(250, 30));
		nomPanel.add(nomLabel, BorderLayout.NORTH);
		nomPanel.add(nomPrenomField, BorderLayout.CENTER);
		
		JPanel descPanel = new JPanel(new BorderLayout(10, 5));
		JLabel descLabel = new JLabel("Description");
		descriptionArea  = new JTextArea();
		descriptionArea.setPreferredSize(new Dimension(250, 150));
		descriptionArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		descPanel.add(descLabel, BorderLayout.NORTH);
		descPanel.add(new JScrollPane(descriptionArea), BorderLayout.CENTER);
		
		JPanel mdpPanel = new JPanel(new BorderLayout(10, 5));
		JLabel mdpLabel = new JLabel("Mot de passe");
		motDePasseField = new JPasswordField();
		motDePasseField.setPreferredSize(new Dimension(250, 30));
		mdpPanel.add(mdpLabel, BorderLayout.NORTH);
		mdpPanel.add(motDePasseField, BorderLayout.CENTER);
		
		formPanel.add(Box.createVerticalStrut(20));
		formPanel.add(photoPanel);
		formPanel.add(Box.createVerticalStrut(20));
		formPanel.add(nomPanel);
		formPanel.add(Box.createVerticalStrut(20));
		formPanel.add(descPanel);
		formPanel.add(Box.createVerticalStrut(20));
		formPanel.add(mdpPanel);
		
		confirmButton = new JButton("CONFIRM");
		confirmButton.setBackground(new Color(50, 205, 50));
		confirmButton.setForeground(Color.BLACK);
		confirmButton.setFont(new Font("Arial", Font.BOLD, 16));
		confirmButton.setPreferredSize(new Dimension(150, 50));
		confirmButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e){ creerCompte(); }
		});

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.add(confirmButton);
		
		mainPanel.add(headerPanel, BorderLayout.NORTH);
		mainPanel.add(formPanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);
		
		getContentPane().add(mainPanel);
	}
	
	private void selectImage()
	{
		JFileChooser            fileChooser = new JFileChooser();
		FileNameExtensionFilter filter      = new FileNameExtensionFilter(
				"Images", "jpg", "jpeg", "png", "gif");

		fileChooser.setFileFilter(filter);
		
		int result = fileChooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION)
		{
			selectedImage = fileChooser.getSelectedFile();
			try
			{
				ImageIcon originalIcon = new ImageIcon(selectedImage.getPath());
				Image originalImage = originalIcon.getImage();
				Image resizedImage = originalImage.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
				profileImage = new ImageIcon(resizedImage);
				photoLabel.setIcon(profileImage);
			}
			catch (Exception e)
			{
				JOptionPane.showMessageDialog(this, 
					"Erreur lors du chargement de l'image: " + e.getMessage(), 
					"Erreur", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	private void creerCompte()
	{
		String nomPrenom   = nomPrenomField.getText().trim();
		String description = descriptionArea.getText().trim();
		String motDePasse  = new String(motDePasseField.getPassword());
		
		if (nomPrenom.isEmpty())
		{
			JOptionPane.showMessageDialog(this, "Veuillez entrer votre nom et prénom", "Champ requis", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		if (profileImage == null) 
		{
			JOptionPane.showMessageDialog(this, "Veuillez ajouter une photo de profil", "Photo requise", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		if (motDePasse.isEmpty()) 
		{
			JOptionPane.showMessageDialog(this, "Veuillez entrer un mot de passe", "Champ requis", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		controleur.creerCompte(nomPrenom, description, profileImage, motDePasse);
		

		dispose();
	}
}

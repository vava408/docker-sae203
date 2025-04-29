import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.SwingUtilities;

public class Client {
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private Controleur controleur;
	private String idUtilisateurConnecte;
	
	private static final String SERVER_ADDRESS = "localhost";
	private static final int SERVER_PORT = 5000;
	
	public Client() 
	{
		connecterAuServeur();
		
		this.controleur = new Controleur(this);
		
		SwingUtilities.invokeLater(() -> {
			controleur.afficherConnexion();
		});
	}
	
	private void connecterAuServeur()
	{
		try
		{
			socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			
			new Thread(this::ecouterServeur).start();
			
			System.out.println("Connecté au serveur");
		} 
		catch (IOException e)
		{
			System.err.println("Erreur de connexion au serveur: " + e.getMessage());
		}
	}
	
	private void ecouterServeur()
	{
		try 
		{
			while (true) 
			{
				Object messageRecu = in.readObject();
				traiterMessageServeur(messageRecu);
			}
		}
		catch (IOException | ClassNotFoundException e)
		{
			System.err.println("Erreur communication serveur: " + e.getMessage());
		}
	}
	
	private void traiterMessageServeur(Object message) 
	{
		if (message instanceof List)
		{
			@SuppressWarnings("unchecked")
			List<Profil> profils = (List<Profil>) message;
			controleur.recevoirProfils(profils);
		} 
		else if (message instanceof Message) 
		{
			Message msgChat = (Message) message;
			controleur.recevoirMessage(msgChat);
		} 
		else if (message instanceof Profil) 
		{
			Profil profilMatch = (Profil) message;
			controleur.nouveauMatch(profilMatch);
		} 
		else if (message instanceof Map)
		{
			@SuppressWarnings("unchecked")
			Map<String, Object> responseMap = (Map<String, Object>) message;
			String type = (String) responseMap.get("type");
			
			if (type.equals("PROFIL_CREE"))
			{
				Profil profilMisAJour = (Profil) responseMap.get("profil");
				idUtilisateurConnecte = profilMisAJour.getId();
				controleur.mettreAJourProfilUtilisateur(profilMisAJour);
			} 
			else if (type.equals("CONNEXION_REUSSIE")) 
			{
				Profil profilConnecte = (Profil) responseMap.get("profil");
				idUtilisateurConnecte = profilConnecte.getId();
				controleur.mettreAJourProfilUtilisateur(profilConnecte);
				
				@SuppressWarnings("unchecked")
				List<Message> historiqueMessages = (List<Message>) responseMap.get("historique_messages");
				if (historiqueMessages != null) {
					controleur.recevoirHistoriqueMessages(historiqueMessages);
				}
			} 
			else if (type.equals("ERREUR_CONNEXION"))
			{
				String message_erreur = (String) responseMap.get("message");
				controleur.afficherErreurConnexion(message_erreur);
			}
		}
	}
	
	public void envoyerNouveauProfil(Profil profil)
	{
		envoyerAuServeur("NOUVEAU_PROFIL", profil);
	}
	
	public void envoyerDemandeConnexion(String identifiant, String motDePasse)
	{
		Map<String, String> infoConnexion = new HashMap<>();
		infoConnexion.put("identifiant", identifiant);
		infoConnexion.put("motDePasse", motDePasse);
		envoyerAuServeur("CONNEXION", infoConnexion);
	}
	
	public void envoyerLike(String idUtilisateur, String idProfilLike)
	{
		envoyerAuServeur("LIKE", new String[]{idUtilisateur, idProfilLike});
	}
	
	public void envoyerMessage(Message message)
	{
		envoyerAuServeur("MESSAGE", message);
	}
	
	private void envoyerAuServeur(String type, Object contenu) 
	{
		try
		{
			Map<String, Object> messageMap = new HashMap<>();
			messageMap.put("type", type);
			messageMap.put("contenu", contenu);
			out.writeObject(messageMap);
			out.flush();
		}
		catch (IOException e)
		{
			System.err.println("Erreur envoi au serveur: " + e.getMessage());
		}
	}
	
	public static void main(String[] args) 
	{
		new Client();
	}
}
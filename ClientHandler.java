import java.io.*;
import java.net.*;
import java.util.*;

public class ClientHandler implements Runnable 
{
	private Socket socket;
	private Server serveur;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private String idUtilisateur;
	
	public ClientHandler(Socket socket, Server serveur) 
	{
		this.socket = socket;
		this.serveur = serveur;
		try 
		{
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() 
	{
		try 
		{
			while (true)
			 {
				@SuppressWarnings("unchecked")
				Map<String, Object> message = (Map<String, Object>) in.readObject();
				traiterMessage(message);
			}
		}
		catch (IOException | ClassNotFoundException e) 
		{
			System.out.println("Client déconnecté: " + e.getMessage());
		} 
		finally 
		{
			if (idUtilisateur != null) 
				serveur.deconnecterClient(idUtilisateur);
		}
	}
	
	private void traiterMessage(Map<String, Object> message) {
		String type = (String) message.get("type");
		Object contenu = message.get("contenu");
		
		switch (type)
		{
			case "NOUVEAU_PROFIL":
				Profil nouveauProfil = (Profil) contenu;
				serveur.ajouterProfil(nouveauProfil);
				idUtilisateur = nouveauProfil.getId();
				serveur.enregistrerClient(idUtilisateur, this);
				
				Map<String, Object> reponse = new HashMap<>();
				reponse.put("type", "PROFIL_CREE");
				reponse.put("profil", nouveauProfil);
				envoyerObjet(reponse);
				
				envoyerProfils();
				break;
				
			case "CONNEXION":
				@SuppressWarnings("unchecked")
				Map<String, String> infoConnexion = (Map<String, String>) contenu;
				String identifiant = infoConnexion.get("identifiant");
				String motDePasse = infoConnexion.get("motDePasse");
				
				Profil profilConnecte = serveur.authentifierUtilisateur(identifiant, motDePasse);
				
				if (profilConnecte != null)
				{
					idUtilisateur = profilConnecte.getId();
					serveur.enregistrerClient(idUtilisateur, this);
					
					List<Message> historiqueMessages = serveur.getHistoriqueMessagesUtilisateur(idUtilisateur);
					
					Map<String, Object> reponseConnexion = new HashMap<>();
					reponseConnexion.put("type", "CONNEXION_REUSSIE");
					reponseConnexion.put("profil", profilConnecte);
					reponseConnexion.put("historique_messages", historiqueMessages);
					envoyerObjet(reponseConnexion);
					
					envoyerProfils();
				}
				else
				{
					Map<String, Object> reponseErreur = new HashMap<>();
					reponseErreur.put("type", "ERREUR_CONNEXION");
					reponseErreur.put("message", "Identifiant ou mot de passe incorrect");
					envoyerObjet(reponseErreur);
				}
				break;
				
			case "LIKE":
				String[] ids = (String[]) contenu;
				serveur.enregistrerLike(ids[0], ids[1]);
				break;
				
			case "MESSAGE":
				Message messageChat = (Message) contenu;
				serveur.envoyerMessage(messageChat);
				break;
		}
	}
	
	public void envoyerProfils() 
	{
		try 
		{
			out.writeObject(serveur.getProfilsPourUtilisateur(idUtilisateur));
			out.flush();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void envoyerMatch(Profil profil)
	{
		try 
		{
			out.writeObject(profil);
			out.flush();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void envoyerMessage(Message message) 
	{
		try 
		{
			out.writeObject(message);
			out.flush();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void envoyerObjet(Object objet)
	{
		try 
		{
			out.writeObject(objet);
			out.flush();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
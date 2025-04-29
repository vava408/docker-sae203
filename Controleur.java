import javax.swing.ImageIcon;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Controleur 
{
	private Client         client;
	private InterfaceSwipe interfaceSwipe;
	private Chat           chat;
	private Connexion      connexion;
	
	private List<Profil>               profils                  = new ArrayList<>();
	private List<Profil>               matchs                   = new ArrayList<>();
	private Map<String, List<Message>> messagesParInterlocuteur = new HashMap<>();
	private Profil                     utilisateurCourant;
	
	public Controleur(Client client)
	{
		this.client         = client;
		this.chat           = new Chat(this);
		this.interfaceSwipe = new InterfaceSwipe(this);
		this.connexion      = new Connexion(this);
	}
	
	public void creerCompte(String nom, String description, ImageIcon profile, String motDePasse)
	{
		Profil nouveauProfil = new Profil(nom, description, profile);
		nouveauProfil.setMotDePasse(motDePasse);
		utilisateurCourant = nouveauProfil;
		
		client.envoyerNouveauProfil(nouveauProfil);
		
		afficherInterfaceSwipe();
	}
	
	public void connecterUtilisateur(String identifiant, String motDePasse)
	{
		client.envoyerDemandeConnexion(identifiant, motDePasse);
	}
	
	public void recevoirProfils(List<Profil> profilsServeur)
	{
		this.profils = profilsServeur;
		interfaceSwipe.mettreAJourProfils(profils);
	}
	
	public void likerProfil(Profil profil) {

		if (utilisateurCourant != null && utilisateurCourant.getId() != null && profil.getId() != null) 
		{
			System.out.println("Envoi de like: " + utilisateurCourant.getId() + " -> " + profil.getId());
			client.envoyerLike(utilisateurCourant.getId(), profil.getId());
		}
		else
		{
			System.err.println("Erreur: Impossible d'envoyer le like - ID utilisateur ou ID profil manquant");
			if (utilisateurCourant == null)
			{
				System.err.println("utilisateurCourant est null");
			} 
			else if (utilisateurCourant.getId() == null)
			{
				System.err.println("ID de l'utilisateur courant est null");
			}
			if (profil.getId() == null) 
			{
				System.err.println("ID du profil liké est null");
			}
		}
	}
	
	public void passerProfil(Profil profil) { interfaceSwipe.afficherProfilSuivant();}
	
	public void nouveauMatch(Profil profilMatch)
	{
		matchs.add(profilMatch);
		interfaceSwipe.afficherNotificationMatch(profilMatch);
	}
	
	public List<Profil> getMatchs()
	{
		return matchs;
	}
	
	public void envoyerMessage(String idDestinataire, String message)
	{
		Message nouveauMessage = new Message(utilisateurCourant.getId(), idDestinataire, message);
		
		messagesParInterlocuteur.computeIfAbsent(idDestinataire, k -> new ArrayList<>()).add(nouveauMessage);
		
		client.envoyerMessage(nouveauMessage);
		
		if (chat.isVisible())
		{
			chat.afficherMessage(nouveauMessage);
		}
	}
	
	public void recevoirMessage(Message message) {
		String idInterlocuteur;
		
		if (utilisateurCourant != null && message.getExpediteur().equals(utilisateurCourant.getId()))
		{
			idInterlocuteur = message.getDestinataire();
			messagesParInterlocuteur.computeIfAbsent(idInterlocuteur, k -> new ArrayList<>()).add(message);

			if (chat.isVisible() && chat.getIdInterlocuteur().equals(idInterlocuteur))
				chat.afficherMessage(message);

		}
		else
		{
			idInterlocuteur = message.getExpediteur();
			messagesParInterlocuteur.computeIfAbsent(idInterlocuteur, k -> new ArrayList<>()).add(message);
			
			if (chat.isVisible() && chat.getIdInterlocuteur().equals(idInterlocuteur))
			{
				chat.afficherMessage(message);
			}
			else 
			{
				interfaceSwipe.afficherNotificationMessage(message);
			}
		}
	}
	
	public void recevoirHistoriqueMessages(List<Message> historiqueMessages)
	{
		for (Message message : historiqueMessages) 
		{
			String idInterlocuteur;
			
			if (message.getExpediteur().equals(utilisateurCourant.getId()))
			{
				idInterlocuteur = message.getDestinataire();
			} 
			else 
			{
				idInterlocuteur = message.getExpediteur();
			}
			
			messagesParInterlocuteur.computeIfAbsent(idInterlocuteur, k -> new ArrayList<>()).add(message);
		}
	}
	
	public void afficherConnexion() { connexion.setVisible(true); }
	
	public void afficherCreationCompte()
	{
		connexion.setVisible(false);
		CreerCompte creerCompte = new CreerCompte(this);
		creerCompte.setVisible(true);
	}
	
	public void afficherInterfaceSwipe() { interfaceSwipe.setVisible(true); }
	
	public void ouvrirChat()
	{
		if (!matchs.isEmpty())
			chat.setInterlocuteur(matchs.get(0));
		
		chat.setVisible(true);
		chat.mettreAJourNavigation();
	}
	
	public Profil getUtilisateurCourant() { return utilisateurCourant; }
	
	public void mettreAJourProfilUtilisateur(Profil profilMisAJour) {
		if (profilMisAJour != null) 
		{
			utilisateurCourant = profilMisAJour;
			System.out.println("Profil utilisateur mis à jour avec ID: " + utilisateurCourant.getId());
			
			if (connexion.isVisible())
			{
				connexion.setVisible(false);
				afficherInterfaceSwipe();
			}
		}
	}
	
	public void afficherErreurConnexion(String message) { connexion.afficherErreurConnexion(message); }
	
	public List<Message> getMessagesAvecInterlocuteur(String idInterlocuteur)
	{
		return messagesParInterlocuteur.getOrDefault(idInterlocuteur, new ArrayList<>());
	}
}

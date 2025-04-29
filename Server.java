
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Server
{
	private ServerSocket               serverSocket;
	private Map<String, ClientHandler> clientsConnectes   = new ConcurrentHashMap<>();
	private List<Profil>               profils            = new ArrayList<>();
	private Map<String, List<String>>  likes              = new HashMap<>(); 
	private List<Message>              historiqueMessages = new ArrayList<>();
	private Map<String, String>        identifiantsParId  = new HashMap<>();
	
	private static final int PORT = 5000;
	
	public void demarrer() 
	{
		try 
		{
			serverSocket = new ServerSocket(PORT);
			System.out.println("Serveur démarré sur le port " + PORT);
			
			while (true) 
			{
				Socket clientSocket = serverSocket.accept();
				ClientHandler handler = new ClientHandler(clientSocket, this);
				new Thread(handler).start();
			}
		}
		catch (IOException e)
		{
			System.err.println("Erreur serveur: " + e.getMessage());
		}
	}
	
	public synchronized void ajouterProfil(Profil profil) 
	{
		if (profil.getId() == null || profil.getId().isEmpty()) 
			profil.setId(UUID.randomUUID().toString());
		
		profils.add(profil);
		
		identifiantsParId.put(profil.getId(), profil.getNom());
		
		System.out.println("Nouveau profil ajouté: " + profil.getNom() + " avec ID: " + profil.getId());
		
		diffuserProfilsATous();
	}
	
	public synchronized void enregistrerClient(String idUtilisateur, ClientHandler handler) 
	{
		clientsConnectes.put(idUtilisateur, handler);
	}
	
	public synchronized void enregistrerLike(String idUtilisateur, String idProfilLike) 
	{

		likes.computeIfAbsent(idUtilisateur, k -> new ArrayList<>()).add(idProfilLike);
		
		List<String> likesAutreProfil = likes.getOrDefault(idProfilLike, new ArrayList<>());
		if (likesAutreProfil.contains(idUtilisateur)) {
			System.out.println("Match entre " + idUtilisateur + " et " + idProfilLike);
			
			Profil profilMatch1 = getProfilParId(idUtilisateur);
			Profil profilMatch2 = getProfilParId(idProfilLike);
			
			if (profilMatch1 != null && profilMatch2 != null) 
			{
				ClientHandler handler1 = clientsConnectes.get(idUtilisateur);
				ClientHandler handler2 = clientsConnectes.get(idProfilLike);
				
				if (handler1 != null) handler1.envoyerMatch(profilMatch2);
				if (handler2 != null) handler2.envoyerMatch(profilMatch1);
			}
		}
	}
	
	public synchronized void envoyerMessage(Message message) 
	{
		historiqueMessages.add(message);
		
		ClientHandler destinataire = clientsConnectes.get(message.getDestinataire());
		if (destinataire != null) 
			destinataire.envoyerMessage(message);

	}
	

	public synchronized Profil authentifierUtilisateur(String identifiant, String motDePasse) 
	{
		for (Profil profil : profils) 
			if ((profil.getNom().equals(identifiant) || 
				(identifiantsParId.containsKey(profil.getId()) && identifiantsParId.get(profil.getId()).equals(identifiant))) && 
				profil.getMotDePasse() != null && profil.getMotDePasse().equals(motDePasse)) 
				return profil;
			
		
		return null;
	}
	
	public synchronized List<Message> getHistoriqueMessagesUtilisateur(String idUtilisateur) {
		return historiqueMessages.stream()
			.filter(msg -> msg.getExpediteur().equals(idUtilisateur) || msg.getDestinataire().equals(idUtilisateur))
			.collect(Collectors.toList());
	}
	
	public void deconnecterClient(String idUtilisateur) 
	{
		clientsConnectes.remove(idUtilisateur);
		System.out.println("Client déconnecté: " + idUtilisateur);
	}
	
	public synchronized List<Profil> getProfilsPourUtilisateur(String idUtilisateur)
	{
		List<Profil> profilsFiltres = new ArrayList<>();

		for (Profil profil : profils) 
			if (!profil.getId().equals(idUtilisateur)) 
				profilsFiltres.add(profil);
		
		return profilsFiltres;
	}
	
	// Trouver un profil par son ID
	private Profil getProfilParId(String id) 
	{

		for (Profil profil : profils) 
			if (profil.getId().equals(id)) 
				return profil;
		
		return null;
	}
	
	public synchronized void diffuserProfilsATous() {
		for (Map.Entry<String, ClientHandler> entry : clientsConnectes.entrySet()) 
		{
			String idUtilisateur = entry.getKey();
			ClientHandler handler = entry.getValue();
			
			handler.envoyerProfils();
		}
	}
	
	public static void main(String[] args) 
	{
		Server serveur = new Server();
		serveur.demarrer();
	}
	
}
import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String expediteur;
	private String destinataire;
	private String contenu;
	private Date   dateEnvoi;
	
	public Message(String expediteur, String destinataire, String contenu) 
	{
		this.expediteur   = expediteur;
		this.destinataire = destinataire;
		this.contenu      = contenu;
		this.dateEnvoi    = new Date();
	}
	
	// Getters
	public String getExpediteur()   { return expediteur;   }
	public String getDestinataire() { return destinataire; }
	public String getContenu()      { return contenu;      }
	public Date   getDateEnvoi()    { return dateEnvoi;    }
}

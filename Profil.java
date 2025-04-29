import javax.swing.ImageIcon;
import java.io.Serializable;

public class Profil implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String    id;
	private String    nom;
	private String    description;
	private ImageIcon photo;
	private String    motDePasse;
	
	public Profil(String nom, String description, ImageIcon photo)
	{
		this.nom         = nom;
		this.description = description;
		this.photo       = photo;
	}
	
	public Profil(String nom, String description, ImageIcon photo, String motDePasse)
	{
		this.nom         = nom;
		this.description = description;
		this.photo       = photo;
		this.motDePasse  = motDePasse;
	}
	
	// Getters et setters
	public String    getId()          { return id;          }
	public String    getNom()         { return nom;         }
	public String    getDescription() { return description; }
	public ImageIcon getPhoto()       { return photo;       }
	public String    getMotDePasse()  { return motDePasse;  }
	
	public void setId(String id)                   { this.id          = id;          }
	public void setNom(String nom)                 { this.nom         = nom;         }
	public void setDescription(String description) { this.description = description; }
	public void setPhoto(ImageIcon photo)          { this.photo       = photo;       }
	public void setMotDePasse(String motDePasse)   { this.motDePasse  = motDePasse;  }

}
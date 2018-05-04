package client.corp;

/**
 *
 * @author Julien
 */
public class Message {
    //Attribut

    protected int Id;
    protected int IdDisc;
    protected String message;
    protected Utilisateur auteur;

    //Constructeur
    public Message(int id, int idDisc, Utilisateur auteur, String message) {
        if (id > 0)
            this.Id = id;
        else
            this.Id = 0;
        this.IdDisc = idDisc;
        this.message = message;
        this.auteur = auteur;
    }

    //Methodes
    public int getId() {
        return this.Id;
    }

    public int getIdDisc() {
        return this.IdDisc;
    }

    public void setId(int i) {
        if (i >= 0 && this.Id < 0) {
            this.Id = i;
        }
    }

    public String getRespond() {
        return this.message;
    }
    
    public Utilisateur getUser(){
        return this.auteur;
    }

    public String getAutorId() {
        return this.auteur.getIdentifiant();
    }

    public String getMessage() {
        return this.message;
    }
}

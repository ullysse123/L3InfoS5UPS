package Serveur.corp;

import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author Julien
 */
public class Discussion {

    //Attributs
    protected int Id;
    protected String titre;
    protected Utilisateur auteur;
    protected String contenu;
    protected String groupe;
    protected List<Message> messages;

    //Constructeur
    public Discussion(int id, String titre, String texteMessage, Utilisateur auteur, String groupe) {
        //TODO voir pour initialisation de l'id
        this.messages = new ArrayList<>();
        this.contenu = texteMessage;
        this.auteur = auteur;
        this.titre = titre;
        this.groupe = groupe;
        if (id > 0)
            this.Id = id;
        else
            this.Id = 0;
    }

    //Methode
    public int getId() {
        return this.Id;
    }

    public void setId(int i) {
        if (i >= 0 && this.Id < 0) {
            this.Id = i;
        }
    }

    public String getTitre() {
        return this.titre;
    }

    public Utilisateur getUser() {
        return this.auteur;
    }

    public String getAutorId() {
        return this.auteur.getIdentifiant();
    }

    public String getContenu() {
        return this.contenu;
    }

    public String getGroupe() {
        return this.groupe;
    }

    public List<Message> getMessages() {
        return this.messages;
    }

    public void ajouterMessage(Message message) {
        this.messages.add(message);
    }

}

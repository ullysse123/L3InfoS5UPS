package Serveur.server;

/**
 *
 * @author Julien
 */
import Serveur.corp.*;

public class ServeurMessage {

    //Differents types de message :
    // 0-> Message
    // 1-> Demande de synchro
    // 2-> Utilisateur
    // 3-> Discussion
    // 4-> Fin
    // 5-> Groupe
    // 6->
    // 7->
    // 8->
    // 9->
    protected int type;
    protected String corpDuMessage;

    //Constructeur pour produire un message
    public ServeurMessage(int type, String corpDuMessage) {
        this.type = type;
        this.corpDuMessage = corpDuMessage;
    }

    //Decode le message
    public ServeurMessage(String messageEntier) {
        this.type = Character.getNumericValue(messageEntier.charAt(0));
        this.corpDuMessage = messageEntier.substring(1);
    }

    //Permet d'encode le message pour envoie
    public String encodeMessage() {
        return type + corpDuMessage;
    }

    //Permet de renvoyer le type de message recu
    public int typeOfMessage() {
        return type;
    }

    //Permet d'encoder un Message en serveurMessage
    public void encodeTypeMessage(Message mess) {
        //ID Discussion + ID Message + login + message
        //ID Disc = 5 char
        //ID Mess = 5 char
        //login = 50 char
        //message = NON Defini
        String idDisc = Integer.toString(mess.getIdDisc());
        String idMess = Integer.toString(mess.getId());
        this.encodeTypeUtilisateur(mess.getUser());
        String login = this.corpDuMessage;
        String messEncode = new String();
        //Encode l'IDDisc
        for (int i = 0; i < (5 - idDisc.length()); i++) {
            messEncode = messEncode + 0;
        }
        messEncode = messEncode + idDisc;
        //Char 5
        //Encode l'IDMess
        for (int i = 0; i < (5 - idMess.length()); i++) {
            messEncode = messEncode + 0;
        }
        messEncode = messEncode + idMess;
        //Char 10
        //MODIF ICI
        messEncode = messEncode + login;
       
        //Char 60
        //Encode le message
        messEncode = messEncode + mess.getRespond();
        this.corpDuMessage = messEncode;
    }

    //Permet d'encoder un Utilisateur en serveurMessage
    public void encodeTypeUtilisateur(Utilisateur user) {
        String groupe = user.getType().toString();
        String nom = user.getNom();
        String prenom = user.getPrenom();
        String login = user.getIdentifiant();
        String messEncode = new String();
        //groupe 30 char
        //nom 30 char
        //prenom 30 char
        //login 50 char
        messEncode = messEncode + login;
        for (int i = 0; i < (50 - login.length()); i++) {
            messEncode = messEncode + " ";
        }
        //Char 50
        messEncode = messEncode + nom;
        for (int i = 0; i < (30 - nom.length()); i++) {
            messEncode = messEncode + " ";
        }
        //Char 80
        messEncode = messEncode + prenom;
        for (int i = 0; i < (30 - prenom.length()); i++) {
            messEncode = messEncode + " ";
        }
        //Char 110
        messEncode = messEncode + groupe;
        for (int i = 0; i < (30 - groupe.length()); i++) {
            messEncode = messEncode + " ";
        }
        //Char 140
        this.corpDuMessage = messEncode;
    }

    //Permet d'encoder une Discussion en serveurMessage
    public void encodeTypeDiscussion(Discussion disc) {
        String id = Integer.toString(disc.getId());
        this.encodeTypeUtilisateur(disc.getUser());
        String user = this.corpDuMessage;
        String titre = disc.getTitre();
        String contenu = disc.getContenu();
        String groupe = disc.getGroupe();
        String messEncode = new String();
        //id 5
        //user 140
        //titre 80
        //contenu ND
        //groupe 30
        for (int i = 0; i < (5 - id.length()); i++) {
            messEncode = messEncode + 0;
        }
        messEncode = messEncode + id;
        messEncode = messEncode + groupe;
        for (int i = 0; i < (30 - groupe.length()); i++) {
            messEncode = messEncode + " ";
        }
        messEncode = messEncode + user;
        messEncode = messEncode + titre;
        for (int i = 0; i < (80 - titre.length()); i++) {
            messEncode = messEncode + " ";
        }
        messEncode = messEncode + contenu;
        this.corpDuMessage = messEncode;
    }

    //Permet de convertir en type Message
    public Message convertToMessage() {
        System.out.println(corpDuMessage);
        int idDisc = Integer.parseInt(this.corpDuMessage.substring(0, 5));
        int idMess = Integer.parseInt(this.corpDuMessage.substring(5, 10));
        Utilisateur auteur = (new ServeurMessage(2, this.corpDuMessage.substring(10, 150))).convertToUtilisateur();
        String mess = this.corpDuMessage.substring(150);
        return new Message(idMess, idDisc, auteur, mess);
    }

    //Permet de convertir en type Discussion
    public Discussion convertToDiscussion() {
        System.out.println("Corps : " + this.corpDuMessage);
        //int id = Integer.parseInt(this.corpDuMessage.substring(0, 4));
        Integer id_p = Integer.valueOf(this.corpDuMessage.substring(0, 5));
        int id = id_p.intValue();
        System.out.println("Id disc convert : " + id);
        Utilisateur user = (new ServeurMessage(2, this.corpDuMessage.substring(35, 174))).convertToUtilisateur();
        String titre = this.corpDuMessage.substring(175, 254);
        String contenue = this.corpDuMessage.substring(255);
        String groupe = this.corpDuMessage.substring(5, 34);
        return new Discussion(id, titre, contenue, user, groupe);
    }

    //Permet de convertir en type Utilisateur
    public Utilisateur convertToUtilisateur() {
        String login = this.corpDuMessage.substring(0, 49).replaceAll(" ", "");
        String nom = this.corpDuMessage.substring(50, 79);
        String prenom = this.corpDuMessage.substring(80, 109);
        TypeUtilisateur groupe = TypeUtilisateur.valueOf(this.corpDuMessage.substring(110, 139).replaceAll(" ", ""));
        return new Utilisateur(login, nom, prenom, groupe);
    }

    //Permet de convertir en Groupe (String)
    public String convertToGroupe() {
        return this.corpDuMessage;
    }

    //Permet d'encoder un Groupe (String) en ServeurMessage
    public void encodeTypeGroupe(String grp) {
        this.corpDuMessage = grp;
    }
}

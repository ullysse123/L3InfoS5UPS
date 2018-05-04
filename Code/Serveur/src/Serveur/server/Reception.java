package Serveur.server;

/**
 *
 * @author Julien
 */
import Serveur.corp.Discussion;
import Serveur.corp.Message;
import Serveur.corp.Utilisateur;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Reception implements Runnable {

    private BufferedReader in;
    private final Socket socket;
    private String message = null, login = null;
    Thread Tsynchro;
    Database db;

    public Reception(Socket socket, String login) {

        this.socket = socket;
        this.login = login;
    }

    //Il faudra totalement revoir le code ici, utilisation d'un exemple pour faire des tests.
    //Voir schema 
    @Override
    public void run() {

        try {
            db = new Database();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Reception.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.err.println("Connexion avec le serveur perdu.");
        }

        while (!socket.isClosed()) {
            try {
                message = in.readLine();
                System.out.println("Reception d'un message : " + message);
                ServeurMessage current_mess = new ServeurMessage(message);
                switch (current_mess.typeOfMessage()) {
                    case 0:
                        //Reception d'un message
                        System.out.println("Reception demande message");
                        db.ajouterMessage(current_mess.convertToMessage());
                        break;
                        
                    case 1:
                        //Demande de synchronisation
                        System.out.println("Reception demande synchro");
                        /*
                        Tsynchro = new Thread(new Synchronisation(socket, login));
                        Tsynchro.join();
                        */
                        this.synchronisation();
                        break;
                        
                    case 2:
                        //Utilisateur
                        //Cas qui n'arrive pas hors synchro
                        System.out.println("Reception demande utilisateur");
                        break;
                        
                    case 3:
                        //Discussion
                        //Nouvelle discussion
                        System.out.println("Reception demande discussion");
                        db.ajouterDiscussion(current_mess.convertToDiscussion());
                        break;
                        
                    default:
                        System.out.println("Reception d'un message inconnu");
                        break;
                }
            } catch (IOException ex) {
                Logger.getLogger(Reception.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
    public void synchronisation() {
        ServeurMessage mess_env;
        List<Discussion> list_disc;
        List<Message> list_mess;
        Utilisateur user;
        Database bdd;
        List<String> groupes;
        PrintWriter out;

        try {
            //Initialisation de la sortie 
            out = new PrintWriter(socket.getOutputStream());
            //Connexion a la Base de Donnees
            bdd = new Database();

            //Utilisateur
            user = bdd.recupUtilisateur(login);
            mess_env = new ServeurMessage(2, "");
            mess_env.encodeTypeUtilisateur(user);
            out.println(mess_env.encodeMessage());
            out.flush();

            //Groupes
            groupes = bdd.recupTousGroupes();
            for (String grp : groupes) {
                //System.out.println("Envoi du groupe " + grp);
                mess_env = new ServeurMessage(5, "");
                mess_env.encodeTypeGroupe(grp);
                out.println(mess_env.encodeMessage());
                out.flush();
            }

            //Discussion + messages
            list_disc = bdd.recupDiscussionsUtilisateur(user);
            for (Discussion d : bdd.recupDiscussions(user)){
                list_disc.add(d);
            }
            System.out.println("Liste disc bdd");
            for (Discussion d : list_disc)
                System.out.println(d.getId() + " : " + d.getTitre());
            
            if (list_disc != null) {
                for (Discussion disc : list_disc) {
                    //Discussion
                    //System.out.println("Discussion " + disc);
                    mess_env = new ServeurMessage(3, "");
                    mess_env.encodeTypeDiscussion(disc);
                    System.out.println("Disc encode : " + mess_env.encodeMessage());
                    out.println(mess_env.encodeMessage());
                    out.flush();
                    //Messages de la discussion
                    list_mess = disc.getMessages();
                    for (Message mess : list_mess) {
                        //System.out.println("Message " + mess);
                        mess_env = new ServeurMessage(0, "");
                        mess_env.encodeTypeMessage(mess);
                        out.println(mess_env.encodeMessage());
                        out.flush();
                    }
                }
            }
            //Message de fin
            mess_env = new ServeurMessage(4, "");
            out.println(mess_env.encodeMessage());
            out.flush();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Synchronisation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException e) {
            System.err.println("Le serveur ne repond pas");
        }
    }
}

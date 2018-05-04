package Serveur.server;

/**
 *
 * @author Julien
 */
import Serveur.corp.*;
import java.io.*;
import java.net.*;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Synchronisation implements Runnable {

    private PrintWriter out;
    private final Socket socket;
    private final String login;

    public Synchronisation(Socket socket, String login) {
        this.socket = socket;
        this.login = login;
    }

    //Objectif, envoyer l'integralite des donnees que doit utiliser le client ( toutes ses discussions, tous les groupes, ... )
    @Override
    public void run() {
        ServeurMessage mess_env;
        List<Discussion> list_disc;
        List<Message> list_mess;
        Utilisateur user;
        Database bdd;
        List<String> groupes;

        try {
            //Initialisation de la sortie 
            out = new PrintWriter(socket.getOutputStream());
            //Connexion a la Base de Donnees
            bdd = new Database();

            //Utilisateur
            user = bdd.recupUtilisateur(login);
            mess_env = new ServeurMessage(2, "");
            mess_env.encodeTypeUtilisateur(user);
            System.out.println("Envoi user");
            out.println(mess_env.encodeMessage());
            out.flush();

            //Groupes
            groupes = bdd.recupTousGroupes();
            for (String grp : groupes) {
                //System.out.println("Envoi du groupe " + grp);
                mess_env = new ServeurMessage(5, "");
                mess_env.encodeTypeGroupe(grp);
                System.out.println("Envoi groupe");
                out.println(mess_env.encodeMessage());
                out.flush();
            }

            //Discussion + messages
            list_disc = bdd.recupDiscussions(user);
            if (list_disc != null) {
                for (Discussion disc : list_disc) {
                    //Discussion
                    //System.out.println("Discussion " + disc);
                    mess_env = new ServeurMessage(3, "");
                    mess_env.encodeTypeDiscussion(disc);
                    System.out.println("Envoi disc");
                    out.println(mess_env.encodeMessage());
                    out.flush();
                    //Messages de la discussion
                    list_mess = bdd.recupMessages(disc);
                    for (Message mess : list_mess) {
                        //System.out.println("Message " + mess);
                        mess_env = new ServeurMessage(0, "");
                        mess_env.encodeTypeMessage(mess);
                        System.out.println("Envoi mess");
                        out.println(mess_env.encodeMessage());
                        out.flush();
                    }
                }
            }
            //Message de fin
            mess_env = new ServeurMessage(4, "");
            System.out.println("Envoi fin");
            out.println(mess_env.encodeMessage());
            out.flush();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Synchronisation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException e) {
            System.err.println("Le serveur ne repond pas");
        }
    }
}

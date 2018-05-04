package client.client;

/**
 *
 * @author Julien
 */
import client.corp.Discussion;
import client.corp.Message;
import client.corp.Utilisateur;
import java.net.*;
import java.io.*;
import java.util.*;

public class Synchronisation implements Runnable {

    private final Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private final boolean finSynchro = false;
    private final List<Discussion> list_disc;
    private final String login;
    private Utilisateur user;
    private final List<String> groupes;

    public Synchronisation(Socket socket, List<Discussion> list_disc, String login, Utilisateur user, List<String> groupes) {
        this.socket = socket;
        this.list_disc = list_disc;
        this.login = login;
        this.user = user;
        this.groupes = groupes;
    }

    @Override
    public void run() {
        ServeurMessage mess_emis;
        ServeurMessage mess_recu;
        Discussion current_disc = null;
        Message current_mess;
        try {
            //On initialise nos entree sortie.
            out = new PrintWriter(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //On envoi une requete de synchronisation
            mess_emis = new ServeurMessage(1, "");
            out.println(mess_emis);
            out.flush();
            mess_recu = new ServeurMessage(in.readLine());

            //reception de l'utilisateur
            if (mess_recu.typeOfMessage() == 2) {
                this.user = mess_recu.convertToUtilisateur();
                mess_recu = new ServeurMessage(in.readLine());
                //on recois tout les groupes contenu dans la bdd
                while (mess_recu.typeOfMessage() == 5) {
                    this.groupes.add(mess_recu.convertToGroupe());
                }
                //On re�ois et stocke tous nos message sous la bonne forme tant que on a pas recu requete de fin
                while (mess_recu.typeOfMessage() != 4) {
                    //reception d'une discussion
                    if (mess_recu.typeOfMessage() == 3) {
                        if (current_disc != null) {
                            this.list_disc.add(current_disc);
                        }
                        current_disc = mess_recu.convertToDiscussion();
                        mess_recu = new ServeurMessage(in.readLine());
                        while (mess_recu.typeOfMessage() == 0) {
                            current_mess = mess_recu.convertToMessage();
                            current_disc.ajouterMessage(current_mess);
                            mess_recu = new ServeurMessage(in.readLine());
                        }
                    }
                    mess_recu = new ServeurMessage(in.readLine());
                }
            } else {
                System.err.println("Utilisateur non recu");
            }
        } catch (IOException e) {
            System.err.println("Le serveur ne répond plus");
        }
    }

}

package Serveur.server;

/**
 *
 * @author Julien
 */
import java.net.*;
import java.io.*;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Authentification implements Runnable {

    private final Socket socket;
    //Outils nous permettant de lire des message recu et d'ecrire des messages a envoyer
    private PrintWriter out = null;
    private BufferedReader in = null;
    //Nous permet de tester si le compte est valide
    private String login = "test", pass = null;
    //Nous previent quand la personne est bien authentifie
    public boolean authentifier = false;
    //base de donnees
    public Database bdd;

    public Thread t2;

    public Authentification(Socket s) {
        socket = s;
    }

    @Override
    public void run() {

        try {
            this.bdd = new Database();
            //On configure nos entrees sortie
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            //On boucle tant que la personne n'est pas authentifie
            while (!authentifier) {

                //On envoi un message pour demander le login puis on enregistre la reponse 
                /*out.println("Entrez votre login :"); //Envoi du message
				out.flush(); //Nettoyage du buffer*/
                login = in.readLine(); //On enregistre dans un string la reponse ( readLine transforme le flux binaire en String )

                //On effectue la meme operation avec le mot de passe
                //out.println("Entrez votre mot de passe :");
                //out.flush();
                pass = in.readLine();

                //On test la validite du mot de passe
                if (isValid(login, pass)) {

                    //On lui envoi un message indiquant qu'il est connecte
                    out.println("connecte");
                    //On affiche sur la console qui vient de se connecter
                    System.out.println(login + " vient de se connecter");
                    out.flush();
                    authentifier = true;

                } else {

                    //On le previent qu'il y a une erreur
                    out.println("Erreur");
                    out.flush();

                }
            }
            //On lance ensuite notre Thread qui gere la communication entre notre client et notre serveur
            t2 = new Thread(new Chat_ClientServeur(socket, login));
            t2.start();

        } catch (IOException e) {
            System.err.println(login + "ne repond pas.");
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Authentification.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Methode verifiant la validite d'un login devra etre definie une fois la BDD prete
    private boolean isValid(String login, String pass) {
        return (this.bdd.verifierUtilisateur(login, pass) != null);
    }
}

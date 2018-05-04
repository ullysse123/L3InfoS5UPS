package Serveur.server;

/**
 *
 * @author Julien
 */
import java.io.*;
import java.net.*;

public class Connexion implements Runnable {

    //Attribues permettant de mettre en place la connexion
    private ServerSocket socketserver = null;
    private Socket socket = null;
    //Attribue permettant de lancer notre Thread Authentification
    public Thread t1;

    //Lors de la construction de l'objet on récupère notre ServerSocket
    public Connexion(ServerSocket socketserver) {
        this.socketserver = socketserver;
    }

    //Code qui sera executé par le Thread
    @Override
    public void run() {

        try {
            while (true) {
                //On accepte toutes les connexions entrante
                socket = socketserver.accept();

                //Print présent pours les tests console
                System.out.println("Tentative de connexion.");

                //On lance notre Thread qui va gérer l'authentification
                t1 = new Thread(new Authentification(socket));
                t1.start();

            }
        } catch (IOException e) {
            System.err.println("Erreur serveur");
        }
    }
}

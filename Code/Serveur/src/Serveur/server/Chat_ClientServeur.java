package Serveur.server;

/**
 *
 * @author Julien
 */
import java.io.*;
import java.net.*;

public class Chat_ClientServeur implements Runnable {

    private Socket socket = null;
    private BufferedReader in = null;
    private final PrintWriter out = null;
    private String login = "zero";

    public Chat_ClientServeur(Socket s, String log) {
        socket = s;
        login = log;
    }

    @Override
    public void run() {

        try {
            //on configure les entrée et sortie de notre flux
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //on lance un thread qui gere la réception
            Thread t3 = new Thread(new Reception(socket, login));
            t3.start();

        } catch (IOException e) {
            System.err.println(login + "s'est déconnecté ");
        }
    }
}

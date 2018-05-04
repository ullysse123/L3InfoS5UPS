/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author Julien
 */
public class Emission implements Runnable {

    private final Socket socket;
    private final String mess;
    private PrintWriter out;

    public Emission(Socket socket, String messageEncode) {
        this.socket = socket;
        this.mess = messageEncode;
    }

    @Override
    public void run() {
        try {
            //On initialise notre sortie
            out = new PrintWriter(socket.getOutputStream());

            //On envoie le message encodé
            out.println(mess);
            out.flush();

        } catch (IOException e) {
            System.err.println("Le serveur ne répond pas");
        }
    }

}

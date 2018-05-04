/*
 * Projet realiser dans le cadre de l'UE Projet du semestre 5
 * Projet realise par ROUMILI Morjana, BOURGEOIS Julien, ZARAGOZA Jeremy
 * Partie Serveur faite par ZARAGOZA Jeremy
 */
package Serveur.server;

import Serveur.corp.*;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * Projet realiser dans le cadre de l'UE Projet du semestre 5
 * Projet realise par ROUMILI Morjana, BOURGEOIS Julien, ZARAGOZA Jeremy
 * Partie Serveur faite par ZARAGOZA Jeremy
 */
/**
 *
 * @author jeremy
 *
 * TODO FERMER LES REQUETES DES FONCTIONS
 */
public class Database {

    private final String url;
    private final String user;
    private final String passwd;
    private final Connection conn;
    private final Statement state;

    /**
     * Permet de se connecter a la base de donnees
     *
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public Database() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        this.url = "jdbc:mysql://localhost:3306/Projet_S5";
        this.user = "root";
        this.passwd = "jeremy";
        this.conn = DriverManager.getConnection(url, user, passwd);
        this.state = this.conn.createStatement();
    }

    /* ================================== *\
     * ==== GESTION DES UTILISATEURS ==== *
    \* ================================== */
    /**
     * Ajoute un utilisateur a la base de donnees avec son mot de passe
     *
     * @param user Utilisateur a ajouter
     * @param pwd Mot de passe de l'utilisateur
     * @return 1 si succes, 0 sinon
     */
    public int ajouterUtilisateur(Utilisateur user, String pwd) {
        String user_name = user.getNom();
        String user_first_name = user.getPrenom();
        String user_id = user.getIdentifiant();
        TypeUtilisateur user_type = user.getType();
        String user_grp;
        switch (user_type) {
            case ENSEIGNANT:
                user_grp = "'groupe_enseignant'";
                break;
            case ADMINISTRATION:
                user_grp = "'groupe_administration'";
                break;
            case PLOMBIER:
                user_grp = "'groupe_plombier'";
                break;
            case INFORMATICIEN:
                user_grp = "'groupe_informaticien'";
                break;
            default:
                user_grp = "'groupe_etudiant'";
                break;
        }
        //ensemble des valeurs
        String values_user = "'" + user_id + "','" + user_name + "','" + user_first_name + "','" + pwd + "','" + user_type + "'";
        String values_groupe = "'" + user_id + "'," + user_grp;
        //requete
        String request_user = "INSERT INTO `UTILISATEUR`(`identifiant`, `nom_utilisateur`, `prenom_utilisateur`, `mot_de_passe`, `type_utilisateur`) VALUES (" + values_user + ")";
        String request_grp = "INSERT INTO `APPARTIENT` (`identifiant`, `nom_groupe`) VALUES (" + values_groupe + ")";
        int result;
        try {
            state.executeUpdate(request_user);
            result = state.executeUpdate(request_grp);
        } catch (java.sql.SQLException ex) {
            //Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            result = 0;
        }
        return (result);
    }

    /**
     * Supprime un utilisateur de la base de donnees
     *
     * @param user Utilisateur a supprimer
     * @return 1 si succes, 0 sinon
     */
    public int supprimerUtilisateur(Utilisateur user) {
        String user_id = user.getIdentifiant();
        String request_user = "DELETE FROM `UTILISATEUR` WHERE identifiant = '" + user_id + "'";
        int result_user;
        try {
            result_user = state.executeUpdate(request_user);
        } catch (java.sql.SQLException ex) {
            //Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            result_user = 0;
        }
        return result_user;
    }

    /**
     * Modifie le mot de passe d'un utilisateur
     *
     * @param user Utilisateur a modifier
     * @param pwd Nouveau mot de passe
     * @return 1 si succes, 0 sinon
     */
    public int modifierUtilisateur(Utilisateur user, String pwd) {
        String user_id = user.getIdentifiant();
        String request_user = "UPDATE `UTILISATEUR` SET mot_de_passe = '" + pwd + "' WHERE identifiant = '" + user_id + "'";
        int result_user;
        try {
            result_user = state.executeUpdate(request_user);
        } catch (java.sql.SQLException ex) {
            //Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            result_user = 0;
        }
        return result_user;
    }

    /**
     * Permet de recuperer un Utilisateur a partir de son identifiant
     *
     * @param identifiant Identifiant de l'Utilisateur a recuperer
     * @return L'Utilisateur correspondant si trouve, null sinon
     */
    public Utilisateur recupUtilisateur(String identifiant) {
        Utilisateur local_user = null;
        String request_user = "SELECT `nom_utilisateur`,`prenom_utilisateur`,`type_utilisateur` FROM `UTILISATEUR` WHERE identifiant = '" + identifiant + "'";
        try {
            ResultSet result_user = state.executeQuery(request_user);
            if (result_user.next()) {
                local_user = new Utilisateur(identifiant, result_user.getString(1), result_user.getString(2), TypeUtilisateur.valueOf(result_user.getString(3)));
            }
        } catch (java.sql.SQLException ex) {
            //Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return local_user;
    }

    /**
     * Permet de recuperer tout les utilisateur contenu dans la base de donnees
     *
     * @return La liste des Utilisateurs (possiblement vide) si succes, null
     * sinon
     */
    public List<Utilisateur> recupTousUtilisateur() {
        List<Utilisateur> all_user = new ArrayList<>();
        String request_user = "SELECT `identifiant`,`nom_utilisateur`,`prenom_utilisateur`,`type_utilisateur` FROM `UTILISATEUR`";
        try {
            ResultSet result_user = state.executeQuery(request_user);
            while (result_user.next()) {
                all_user.add(new Utilisateur(result_user.getString(1), result_user.getString(2), result_user.getString(3), TypeUtilisateur.valueOf(result_user.getString(4))));
            }
        } catch (java.sql.SQLException ex) {
            //Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            all_user = null;
        }
        return all_user;
    }

    /**
     * Verifie la correspondance entre un utilisateur et son mot de passe
     *
     * @param user_id Identifiant de l'Utilisateur
     * @param pwd Mot de passe saisie
     * @return L'objet Utilisateur correspondant si l'identification reussi,
     * false null
     */
    public Utilisateur verifierUtilisateur(String user_id, String pwd) {
        String mdp;
        Utilisateur user_verif = null;
        String request_user = "SELECT `nom_utilisateur`,`prenom_utilisateur`,`mot_de_passe`,`type_utilisateur` FROM `UTILISATEUR` WHERE `identifiant` = '" + user_id + "'";
        try {
            ResultSet result_user = state.executeQuery(request_user);
            if (result_user.next()) {
                mdp = result_user.getString(3);
                if (mdp.equals(pwd)) {
                    user_verif = new Utilisateur(user_id, result_user.getString(1), result_user.getString(2), TypeUtilisateur.valueOf(result_user.getString(4)));
                }
            }
        } catch (java.sql.SQLException ex) {
            //Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return user_verif;
    }

    /* ================================= *\
     * ==== GESTION DES DISCUSSIONS ==== *
    \* ================================= */
    /**
     * Ajoute une discussion dans la base de donnees
     *
     * @param disc Discussion a ajouter
     */
    public void ajouterDiscussion(Discussion disc) {
        String disc_content = disc.getContenu();
        String disc_autor = disc.getAutorId();
        String disc_title = disc.getTitre();
        String disc_group = disc.getGroupe();
        int result_id;
        //ensemble des valeurs
        String values = "'" + disc_title + "','" + disc_content + "','" + disc_autor + "','" + disc_group + "'";
        //requete
        String request_discussion = "INSERT INTO `DISCUSSION` (`titre`,`contenu`,`auteur_discussion`,`nom_groupe`) VALUES (" + values + ")";
        try {
            int result_discussion = state.executeUpdate(request_discussion);
            /*
            request_discussion = "SELECT `numero_discussion` FROM `DISCUSSION` WHERE (`titre` = '" + disc_title + "' AND `contenu` = '" + disc_content + "' AND `nom_groupe` = '" + disc_group + "' AND `auteur_discussion` = '" + disc_autor + "') ORDER BY `numero_discussion` DESC";
            ResultSet result_disc = state.executeQuery(request_discussion);
            result_id = result_disc.getInt(1);
            */
        } catch (SQLException ex) {
            //Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Database add disc : ERREUR");
            result_id = -1;
        }
    }

    /**
     * Supprime une discussion dans la base de donnees
     *
     * @param disc Discussion a supprimer
     * @return 1 si succes, 0 sinon
     */
    public int supprimerDiscussion(Discussion disc) {
        int val;
        try {
            //supprimer tout les messages de la discussion
            List<Message> liste_reponse = this.recupMessages(disc);
            for (Message m : liste_reponse) {
                this.supprimerMessage(m);
            }
            int disc_id = disc.getId();
            String request_discussion = "DELETE FROM `DISCUSSION` WHERE numero_discussion = '" + disc_id + "'";
            val = state.executeUpdate(request_discussion);
        } catch (SQLException ex) {
            //Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            val = 0;
        }
        return val;
    }
    
    /**
     * Recupere toute les discussions dont l'Utilisateur est l'auteur mais qui ne sont pas dans ses groupes accessibles
     * @param user Utilisateur auteur des discussions
     * @return la liste des discussions faites par l'Utilisateur (possiblement vide)
     */
    public List<Discussion> recupDiscussionsUtilisateur(Utilisateur user){
        List<Discussion> list_disc = new ArrayList<>();
        List<String> list_grp = new ArrayList<>();
        String user_id = user.getIdentifiant();
        
        String request_appartient = "SELECT `nom_groupe` FROM `APPARTIENT` WHERE `identifiant` = '" + user_id + "'";
        int numero_disc;
        String titre_disc, contenu_disc, groupe_disc;
        List<Message> mess_list = recupTousMessages();
        
        try (ResultSet result_appartient = state.executeQuery(request_appartient)){
            while (result_appartient.next()) {
                list_grp.add(result_appartient.getString(1));
            }
            for (String s : list_grp){
                String request_disc = "SELECT `numero_discussion`,`titre`,`contenu`,`auteur_discussion`,`nom_groupe` FROM `DISCUSSION` WHERE `auteur_discussion` = '" + user_id + "' AND `nom_groupe` != '" + s +"' ORDER BY `numero_discussion` DESC";
                ResultSet result_disc = state.executeQuery(request_disc);
                while(result_disc.next()){
                    numero_disc = result_disc.getInt(1);
                    titre_disc = result_disc.getString(2);
                    contenu_disc = result_disc.getString(3);
                    groupe_disc = result_disc.getString(5);
                    
                    //creation de la discussion
                    Discussion current_disc = new Discussion(numero_disc, titre_disc, contenu_disc, user, groupe_disc);
                    //recuperation des messages
                    for (Message m : mess_list) {
                        if (m.getIdDisc() == current_disc.getId())
                            current_disc.ajouterMessage(m);
                    }
                    list_disc.add(current_disc);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list_disc;
    }

    /**
     * Recupere les discussions d'un utilisateur
     *
     * @param user Utilisateur dont on recupere les discussions
     * @return Liste de discussion trie par groupe accessible par l'utilisateur
     */
    public List<Discussion> recupDiscussions(Utilisateur user) {
        List<Discussion> list_disc = new ArrayList<>();
        List<String> list_grp = new ArrayList<>();
        List<Utilisateur> user_list;
        List<Message> mess_list;
        String user_id = user.getIdentifiant();
        String request_appartient = "SELECT `nom_groupe` FROM `APPARTIENT` WHERE `identifiant` = '" + user_id + "'";
        ResultSet result_appartient;
        Utilisateur current_autor = null;
        int numero_disc;
        String titre_disc, contenu_disc, auteur_disc, user_disc, groupe_disc;
        
        try {
            result_appartient = state.executeQuery(request_appartient);
            while (result_appartient.next()) {
                list_grp.add(result_appartient.getString(1));
            }
            user_list = recupTousUtilisateur();
            mess_list = recupTousMessages();
            for (String s : list_grp) {
                //recuperation des discussions par groupe
                String request_disc = "SELECT `numero_discussion`,`titre`,`contenu`,`auteur_discussion`,`nom_groupe` FROM `DISCUSSION` WHERE `nom_groupe` = '" + s + "' ORDER BY `numero_discussion` DESC";
                ResultSet result_disc = state.executeQuery(request_disc);
                //defilement des discussions
                while (result_disc.next()) {
                    numero_disc = result_disc.getInt(1);
                    titre_disc = result_disc.getString(2);
                    contenu_disc = result_disc.getString(3);
                    user_disc = result_disc.getString(4);
                    groupe_disc = result_disc.getString(5);
                    //recuperation de l'auteur de chaque discussions
                    for(Utilisateur u : user_list){
                        if (u.getIdentifiant().equals(user_disc)){
                            current_autor = u;
                        }
                    }
                    
                    //creation de la discussion
                    Discussion current_disc = new Discussion(numero_disc, titre_disc, contenu_disc, current_autor, groupe_disc);
                    //recuperation des messages
                    for (Message m : mess_list) {
                        if (m.getIdDisc() == current_disc.getId())
                            current_disc.ajouterMessage(m);
                    }
                    list_disc.add(current_disc);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            list_disc = null;
        }
        return list_disc;
    }

    /* ============================== *\
     * ==== GESTION DES MESSAGES ==== *
    \* ============================== */
    /**
     * Ajoute un message a une discussion dans la base de donnees
     *
     * @param mess Message a ajouter
     * @return L'id du message ajouter, -1 si echec
     */
    public int ajouterMessage(Message mess) {
        int id_desc = mess.getIdDisc();
        String mess_respond = mess.getRespond();
        String mess_autor = mess.getAutorId();
        int mess_id;
        System.out.println("Desc id : " +id_desc);
        //ensemble des valeurs
        String values = "'" + mess_respond + "','" + id_desc + "','" + mess_autor + "'";
        //requete
        String request_message = "INSERT INTO `MESSAGE` (`reponse`,`numero_discussion`,`auteur_message`) VALUES (" + values + ")";
        try {
            int result_message = state.executeUpdate(request_message);
            request_message = "SELECT `numero_message` FROM `MESSAGE` WHERE `reponse` = '" + mess_respond + "' AND `numero_discussion` = '" + id_desc + "' AND `auteur_message` = '" + mess_autor + "' ORDER BY `numero_message` DESC";
            ResultSet result_mess = state.executeQuery(request_message);
            result_mess.next();
            mess_id = result_mess.getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            mess_id = -1;
        }
        return mess_id;
    }

    /**
     * Supprime un message de la base de donnees
     *
     * @param mess Message a supprimer
     * @return 1 si succes, 0 sinon
     */
    public int supprimerMessage(Message mess) {
        int result_message;
        int mess_id = mess.getId();
        String request_message = "DELETE FROM `MESSAGE` WHERE `numero_message` = '" + mess_id + "'";
        try {
            result_message = state.executeUpdate(request_message);
        } catch (SQLException ex) {
            //Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            result_message = 0;
        }
        return result_message;
    }

    /**
     * Recupere les messages d'une discussion donnee
     *
     * @param disc Discussion dont on recupere les messages
     * @return Liste de message tries dans l'ordre chronologique (eventuellement
     * vide) si succes, null sinon
     */
    public List<Message> recupMessages(Discussion disc) {
        List<Message> messagesDiscussions = new ArrayList<>();
        int disc_id = disc.getId();
        String request_message = "SELECT `numero_message`,`reponse`,`auteur_message` FROM `MESSAGE` WHERE `numero_discussion` = '" + disc_id + "' ORDER BY `numero_message` DESC";

        try {
            ResultSet result_message = state.executeQuery(request_message);

            while (result_message.next()) {
                int mess_id = result_message.getInt(1);
                String content = result_message.getString(2);
                String autor = result_message.getString(3);
                //recuperation de l'auteur
                String request_user = "SELECT `identifiant`,`nom_utilisateur`,`prenom_utilisateur`,`type_utilisateur` FROM `UTILISATEUR` WHEN `identifiant` = '" + autor + "'";
                ResultSet result_user = state.executeQuery(request_user);
                //traitement pour l'auteur
                TypeUtilisateur type = TypeUtilisateur.valueOf(result_user.getString(1));
                //result_user.next();
                Utilisateur autor_user = new Utilisateur(result_user.getString(1), result_user.getString(2), result_user.getString(3), type);
                //creation du message
                Message new_message = new Message(mess_id, disc_id, autor_user, content);
                messagesDiscussions.add(new_message);
            }
            return messagesDiscussions;
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            messagesDiscussions = null;
        }

        return messagesDiscussions;
    }
    
    
    public List<Message> recupTousMessages() {
        List<Message> messagesDiscussions = new ArrayList<>();
        String request_message = "SELECT `numero_message`,`reponse`,`numero_discussion`,`auteur_message` FROM `MESSAGE` ORDER BY `numero_message` DESC";
        List<Utilisateur> user_list = recupTousUtilisateur();
        Utilisateur autor_user = null;
        try {
            ResultSet result_message = state.executeQuery(request_message);

            while (result_message.next()) {
                int mess_id = result_message.getInt(1);
                String content = result_message.getString(2);
                int disc_id = result_message.getInt(3);
                String autor = result_message.getString(4);
                //recuperation de l'auteur
                for (Utilisateur u : user_list){
                    if (u.getIdentifiant().equals(autor))
                        autor_user = u;
                }
                //creation du message
                Message new_message = new Message(mess_id, disc_id, autor_user, content);
                messagesDiscussions.add(new_message);
            }
            return messagesDiscussions;
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            messagesDiscussions = null;
        }

        return messagesDiscussions;
    }

    /* ============================= *\
     * ==== GESTION DES GROUPES ==== *
    \* ============================= */
    /**
     * Ajoute un groupe dans la base de donnees
     *
     * @param groupe Nom du groupe a ajouter
     * @return 1 si succes, 0 sinon
     */
    public int ajouterGroupe(String groupe) {
        String request_groupe = "INSERT INTO `GROUPE` (`nom_groupe`) VALUES ('" + groupe + "')";
        int result_groupe;
        try {
            result_groupe = state.executeUpdate(request_groupe);
        } catch (java.sql.SQLException ex) {
            //Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            result_groupe = 0;
        }
        return result_groupe;
    }

    /**
     * Supprime un groupe dans la base de donnees
     *
     * @param groupe Nom du groupe a supprimer
     * @return 1 si succes, 0 sinon
     */
    public int supprimerGroupe(String groupe) {
        String request_groupe = "DELETE FROM `GROUPE` WHERE `nom_groupe` = '" + groupe + "')";
        int result_groupe;
        try {
            result_groupe = state.executeUpdate(request_groupe);
        } catch (java.sql.SQLException ex) {
            //Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            result_groupe = 0;
        }
        return result_groupe;
    }

    /**
     * Permet de recuperer les groupes d'un utilisateur
     *
     * @param user Utilisateur dont on recupere les groupes
     * @return Une liste des groupes de l'utilisateur dans l'ordre alphabetique,
     * null si il y a une erreur
     */
    public List<String> recupGroupes(Utilisateur user) {
        List<String> list_grp;
        String user_id = user.getIdentifiant();
        String request_appartient = "SELECT `nom_groupe` FROM `APPARTIENT` WHERE `identifiant` = '" + user_id + "'ORDER BY `nom_groupe`";
        try {
            list_grp = new ArrayList<>();
            ResultSet result_appartient = state.executeQuery(request_appartient);
            while (result_appartient.next()) {
                list_grp.add(result_appartient.getString(1));
            }
        } catch (SQLException ex) {
            //Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            list_grp = null;
        }
        return list_grp;
    }

    /**
     * Permet de recuperer tout les groupes contenu dans la base de donnees
     *
     * @return La liste des groupes de la base de donnees si succes, null sinon
     */
    public List<String> recupTousGroupes() {
        List<String> list_grp = new ArrayList<>();
        String request_groupe = "SELECT `nom_groupe` FROM `GROUPE` ORDER BY `nom_groupe`";
        try {
            ResultSet result_groupe = state.executeQuery(request_groupe);
            while (result_groupe.next()) {
                list_grp.add(result_groupe.getString(1));
            }
        } catch (SQLException ex) {
            //Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            list_grp = null;
        }
        return list_grp;
    }

    /**
     * Ajouter des membres a un groupe existant
     *
     * @param groupe Nom du groupe source
     * @param utilisateurs Utilisateur(s) a ajouter
     * @return Le nombre d'utilisateur ajoute
     */
    public int ajouterMembre(String groupe, Utilisateur... utilisateurs) {
        int nb_ajout = 0;
        for (Utilisateur u : utilisateurs) {
            String user_id = u.getIdentifiant();

            String request_groupe = "INSERT INTO `APPARTIENT` (`identifiant`,`nom_groupe`) VALUES ('" + user_id + "','" + groupe + "'";
            int result_groupe;
            try {
                result_groupe = state.executeUpdate(request_groupe);
            } catch (SQLException ex) {
                //Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
                result_groupe = 0;
            }
            if (result_groupe == 1) {
                nb_ajout++;
            }
        }
        return nb_ajout;
    }

    /**
     * Permet de recuperer tous les membres d'un groupe
     *
     * @param groupe Groupe dont on veut recuperer les membres
     * @return Une liste d'Utilisateur (possiblement vide) si succes, null sinon
     */
    public List<Utilisateur> recupMembres(String groupe) {
        List<Utilisateur> users = new ArrayList<>();
        Utilisateur usr_recup;
        String request_appartient = "SELECT `identifiant` FROM `APPARTIENT` WHERE `nom_groupe` = '" + groupe + "'";
        try (ResultSet result_appartient = state.executeQuery(request_appartient)) {
            while (result_appartient.next()) {
                usr_recup = this.recupUtilisateur(result_appartient.getString(1));
                if (usr_recup != null) {
                    users.add(usr_recup);
                }
            }
        } catch (SQLException ex) {
            //Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            users = null;
        }
        return users;
    }

    /**
     * Supprime des membres d'un groupe existant
     *
     * @param groupe Nom du groupe source
     * @param utilisateurs Utilisateur(s) a supprimer
     * @return Le nombre d'utilisateur supprime
     */
    public int supprimerMembre(String groupe, Utilisateur... utilisateurs) {
        int nb_suppr = 0;
        for (Utilisateur u : utilisateurs) {
            String user_id = u.getIdentifiant();

            String request_groupe = "DELETE FROM `APPARTIENT` WHERE (`identifiant` = '" + user_id + "' AND `nom_groupe` = '" + groupe + "'";
            int result_groupe;
            try {
                result_groupe = state.executeUpdate(request_groupe);
            } catch (SQLException ex) {
                //Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
                result_groupe = 0;
            }
            if (result_groupe == 1) {
                nb_suppr++;
            }
        }
        return nb_suppr;
    }

    /* ==================================== *\
     * ==== GESTION DE LA DECONNECTION ==== *
    \* ==================================== */
    /**
     * Permet de se deconnecter de la base de donnees
     *
     * @throws SQLException
     */
    public void deconnection() throws SQLException {
        this.state.close();
    }
}

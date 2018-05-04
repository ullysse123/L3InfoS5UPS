package Serveur.corp;

import java.util.*;

/**
 *
 * @author Julien
 */
public class Utilisateur {

    //Attribue
    String identifiant;
    String nom;
    String prenom;
    TypeUtilisateur type;
    List<String> groupes = new ArrayList<>();

    //Constructeur
    public Utilisateur(String id, String nom, String prenom, TypeUtilisateur type) {
        this.identifiant = id;
        this.nom = nom;
        this.prenom = prenom;
        this.type = type;
        String grp = "groupe_" + type.toString().toLowerCase();
        this.groupes.add(grp);

    }

    //Methode
    public String getNom() {
        return this.nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public String getIdentifiant() {
        return this.identifiant;
    }

    public TypeUtilisateur getType() {
        return this.type;
    }

    public List<String> getGroupeUser() {
        return this.groupes;
    }

    public void ajouterGroupe(String nom_groupe) {
        if (!nom_groupe.equals("") && !this.groupes.contains(nom_groupe)) {
            this.groupes.add(nom_groupe);
        }
    }

}

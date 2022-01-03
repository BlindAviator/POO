package fr.univ.modele;

import java.io.Serial;
import java.io.Serializable;

/**
 * Classe mère abstraite représentant un membre de l'école. Stocke des informations communes à tous les membres de
 * l'école.
 */
public abstract class SchoolMember implements Serializable
{
    /**
     * Constante nécessaire afin de permettre la sérialisation de la classe Gala.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Identifiant du membre de l'école.
     */
    protected int id;
    /**
     * Nom de famille du membre de l'école.
     */
    protected final String lastName;
    /**
     * Prénom du membre de l'école.
     */
    protected final String firstName;
    /**
     * Numéro de téléphone du membre de l'école.
     */
    protected final String phoneNumber;
    /**
     * Email du membre de l'école.
     */
    protected final String mail;

    /**
     * Constructeur en charge d'initialiser les attributs. Etant donné le caractère abstrait de la classe SchoolMember,
     * ce constructeur n'engendrera jamais l'instanciation d'un objet SchoolMember.
     * @param id l'identifiant du membre de l'école
     * @param lastName le nom du membre de l'école
     * @param firstName le prénom du membre de l'école
     * @param phoneNumber le numéro de téléphone du membre de l'école
     * @param mail l'adresse e-mail du membre de l'école
     */
    public SchoolMember(int id, String lastName, String firstName, String phoneNumber, String mail){
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.phoneNumber = phoneNumber;
        this.mail = mail;
    }

    /**
     * Retourne l'identifiant du membre de l'école
     * @return l'identifiant du membre de l'école
     */
    public int getId() {
        return id;
    }

    /**
     * Retourne le nom du membre de l'école
     * @return le nom du membre de l'école
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Retourne le prébom du membre de l'école
     * @return le prébom du membre de l'école
     */
    public String getFirstName() {
        return firstName;
    }
}

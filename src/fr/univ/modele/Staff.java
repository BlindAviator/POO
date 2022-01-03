package fr.univ.modele;

import java.io.Serial;
import java.io.Serializable;

/**
 * Classe représentant un membre du personnel
 */
public class Staff extends SchoolMember implements Serializable
{
    /**
     * Constante nécessaire afin de permettre la sérialisation de la classe Gala.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur en charge d'instancier un membre du personnel.
     * @param id l'identifiant du membre du personnel
     * @param lastName le nom du membre du personnel
     * @param firstName le prénom du membre du personnel
     * @param phoneNumber le numéro de téléphone du membre du personnel
     * @param mail l'adresse e-mail du membre du personnel
     */
    public Staff(int id, String lastName, String firstName, String phoneNumber, String mail){
        super(id, lastName, firstName, phoneNumber, mail);
    }
}

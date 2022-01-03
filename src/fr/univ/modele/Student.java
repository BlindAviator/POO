package fr.univ.modele;

import java.io.Serial;
import java.io.Serializable;

/**
 * Constante nécessaire afin de permettre la sérialisation de la classe Gala.
 */
public class Student extends SchoolMember implements Serializable
{
    /**
     * Constante nécessaire afin de permettre la sérialisation de la classe Gala.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * L'année d'étude de l'étudiant. 1 = L1, 5 = M2.
     */
    private final int year;

    /**
     * Constructeur en charge d'instancier un étudiant.
     * @param id l'identifiant de l'étudiant
     * @param lastName le nom de l'étudiant
     * @param firstName le prénom de l'étudiant
     * @param phoneNumber le numéro de téléphone de l'étudiant
     * @param mail l'adresse e-mail de l'étudiant
     */
    public Student(int id, String lastName, String firstName, String phoneNumber, String mail, int year){
        super(id, lastName, firstName, phoneNumber, mail);
        this.year = year;
    }

    /**
     * L'année d'étude de l'étudiant. 1 = L1, 5 = M2
     * @return L'année d'étude de l'étudiant
     */
    public int getYear() {
        return year;
    }
}

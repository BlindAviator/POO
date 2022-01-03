package fr.univ.modele;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Classe représentant une réservation faite par un membre de l'école, pour une table donnée.
 * Stocke le nombre de places occupées par la réservation, le coût total de la réservation et la date du jour auquel la
 * réservation a été passé.
 */
public class Reservation implements Serializable
{
    /**
     * Constante nécessaire afin de permettre la sérialisation de la classe Gala.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * L'identifiant de la table
     */
    private final int tableId;
    /**
     * Une référence vers le membre de l'école à l'origine de la réservation
     */
    private final SchoolMember owner;
    /**
     * Nombre de places nécessaire pour la réservation. Inclu le membre de l'école et ses accompagnateurs.
     */
    private final int nbrSeatsOccupied; // include member + accompanists
    /**
     * Le coût total de la réservation
     */
    private final int reservationPrice;
    /**
     * Devait initialement être utilisé pour permettre le bon fonctionnement de la PriorityQueue.
     */
    private final LocalDate reservationDate;

    /**
     * Constructeur en charge d'initialiser une instance de Reservation.
     * @param tableId l'identifiant de la table
     * @param owner le propriétaire de la réservation
     * @param nbrSeatsOccupied le nombre de places requises par cette réservation
     * @param reservationPrice le coût total de la réservation (prix du membre de l'école + prix des accompagnateurs)
     * @param reservationDate date à laquelle la réservation a été faite
     */
    public Reservation(int tableId, SchoolMember owner, int nbrSeatsOccupied, int reservationPrice, LocalDate reservationDate)
    {
        this.tableId = tableId;
        this.owner = owner;
        this.nbrSeatsOccupied = nbrSeatsOccupied;
        this.reservationPrice = reservationPrice;
        this.reservationDate = reservationDate;
    }

    /**
     * Retourne l'identifiant de la table
     * @return l'identifiant de la table
     */
    public int getTableId() {
        return tableId;
    }

    /**
     * Retourne le coût total de la réservation (prix du membre de l'école + prix des accompagnateurs)
     * @return le coût total de la réservation (prix du membre de l'école + prix des accompagnateurs)
     */
    public int getReservationPrice() {
        return reservationPrice;
    }

    /**
     * Nombre de places occupées par la réservation
     * @return Nombre de places occupées par la réservation
     */
    public int getNbrSeatsOccupied() {
        return nbrSeatsOccupied;
    }

    /**
     * Une référence vers le propriétaire de la réservation
     * @return Une référence vers le propriétaire de la réservation
     */
    public SchoolMember getOwner() {
        return owner;
    }

    @Override
    public String toString() {
        return "\n\033[4mNombre total de places réservées :\033[0m \033[1m" + nbrSeatsOccupied + "\033[0m\n" +
               "\033[4mNuméro de table :\033[0m \033[1m" + tableId + "\033[0m";
    }
}

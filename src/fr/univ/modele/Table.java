package fr.univ.modele;

import fr.univ.exception.NoSeatRemainingException;
import fr.univ.exception.NoSuchReservationException;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Classe représetant une table du Gala. Stocke les réservations et le nombre de places restantes.
 */
public class Table implements Serializable
{
    /**
     * Constante nécessaire afin de permettre la sérialisation de la classe Gala.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * L'identifiant de la table.
     */
    private final int tableId;
    /**
     * La liste des réservations associées à la table.
     */
    private final ArrayList<Reservation> reservations;
    /**
     * Le nombre de places restantes, permettant la réalisation de nouvelles réservations (ou pas).
     */
    private int seatsRemaining;

    /**
     * Le constructeur en charge de l'initialisation d'une instance de la classe Table.
     * @param idTable l'identifiant de la table
     * @param placesLibres le nombre de places disponibles autour de la table
     */
    public Table(int idTable, int placesLibres){
        this.tableId = idTable;
        this.reservations = new ArrayList<>();
        this.seatsRemaining = placesLibres;
    }

    /**
     * Ajoute une réservation à la table. Cela a pour effet de modifier le nombre de places restantes.
     * @param reservation Une réservation à ajouter à la table
     * @throws NoSeatRemainingException Nombre de place insuffisant pour permetre l'ajout d'une réservation
     */
    public void addReservation(Reservation reservation) throws NoSeatRemainingException
    {
        if(seatsRemaining - reservation.getNbrSeatsOccupied() < 0)
            throw new NoSeatRemainingException("Il n'y a plus de place disponible sur cette table");

        seatsRemaining -= reservation.getNbrSeatsOccupied();

        reservations.add(reservation);
    }

    /**
     * Suppression d'une réservation de la table. Cela a pour effet de modifier le nombre de places restantes.
     * @param memberId L'identifiant du membre de l'école à l'origine de la réservation
     * @throws NoSuchReservationException La réservation à supprimer n'existe pas
     */
    public void removeReservation(int memberId) throws NoSuchReservationException
    {
        for(int i = 0; i < reservations.size(); i++)
            if( reservations.get(i).getOwner().getId() == memberId )
            {
                seatsRemaining += reservations.get(i).getNbrSeatsOccupied();
                reservations.remove(i);
                return;
            }

        throw new NoSuchReservationException("La réservation n'existe pas");
    }

    /**
     * Retourne l'identifiant de la table
     * @return l'identifiant de la table
     */
    public int getTableId()
    {
        return tableId;
    }

    /**
     * Retourne la liste des réservations associées à la table
     * @return la liste des réservations associées à la table
     */
    public ArrayList<Reservation> getReservations()
    {
        return reservations;
    }

    /**
     * Le nombre de places encore restantes
     * @return Le nombre de places encore restantes
     */
    public int getSeatsRemaining()
    {
        return seatsRemaining;
    }
}

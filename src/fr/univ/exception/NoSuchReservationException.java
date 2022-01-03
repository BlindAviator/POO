package fr.univ.exception;

/**
 * Exception levée quand aucune réservation ne correspond à un id donné.
 * Cette exception ne devrait jamais être levée.
 */
public class NoSuchReservationException extends Exception
{
    public NoSuchReservationException() {}

    public NoSuchReservationException(String message) {
        super(message);
    }

    public NoSuchReservationException(Throwable cause) {
        super(cause);
    }

    public NoSuchReservationException(String message, Throwable cause) {
        super(message, cause);
    }
}



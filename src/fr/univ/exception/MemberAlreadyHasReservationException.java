package fr.univ.exception;

/**
 * Exception levée lorsque le membre de l'école a déjà une réservation et qu'il en fait une nouvelle.
 * Cette exception ne devrait jamais être levée.
 */
public class MemberAlreadyHasReservationException extends Exception
{
    public MemberAlreadyHasReservationException() {}

    public MemberAlreadyHasReservationException(String message) {
        super(message);
    }

    public MemberAlreadyHasReservationException(Throwable cause) {
        super(cause);
    }

    public MemberAlreadyHasReservationException(String message, Throwable cause) {
        super(message, cause);
    }
}



package fr.univ.exception;

/**
 * Exception levée lorsqu'une réservation ne peut aboutir faute de place restantes, que cela soit pour un numéro de
 * table choisit par l'utilisateur, ou lorsque l'utilisateur remet le choix du numéro de table au programme mais qu'il
 * n'y a manifestement plus une place de disponible.
 */
public class NoSeatRemainingException extends Exception
{
    public NoSeatRemainingException() {}

    public NoSeatRemainingException(String message) {
        super(message);
    }

    public NoSeatRemainingException(Throwable cause) {
        super(cause);
    }

    public NoSeatRemainingException(String message, Throwable cause) {
        super(message, cause);
    }
}



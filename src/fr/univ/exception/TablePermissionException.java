package fr.univ.exception;

/**
 * Exception levée lorsqu'un utilisateur (typiquement un membre du personnel) fait une réservation pour une table
 * d'étudiants.
 */
public class TablePermissionException extends Exception
{
    public TablePermissionException() {}

    public TablePermissionException(String message) {
        super(message);
    }

    public TablePermissionException(Throwable cause) {
        super(cause);
    }

    public TablePermissionException(String message, Throwable cause) {
        super(message, cause);
    }
}



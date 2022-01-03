package fr.univ.exception;

/**
 * Exception levée lorsque la table choisit par l'utilisateur n'existe pas.
 */
public class NoSuchTableException extends Exception
{
    public NoSuchTableException() {}

    public NoSuchTableException(String message) {
        super(message);
    }

    public NoSuchTableException(Throwable cause) {
        super(cause);
    }

    public NoSuchTableException(String message, Throwable cause) {
        super(message, cause);
    }
}



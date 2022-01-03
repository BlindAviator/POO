package fr.univ.exception;

/**
 * Exception levée lorsque l'utilisateur tente d'effectuer des actions "d'après" alors qu'il n'est même pas encore
 * inscris.
 * Cette exception ne devrait jamais être levée.
 */
public class NoSuchSubscriptionException extends Exception
{
    public NoSuchSubscriptionException() {}

    public NoSuchSubscriptionException(String message) {
        super(message);
    }

    public NoSuchSubscriptionException(Throwable cause) {
        super(cause);
    }

    public NoSuchSubscriptionException(String message, Throwable cause) {
        super(message, cause);
    }
}



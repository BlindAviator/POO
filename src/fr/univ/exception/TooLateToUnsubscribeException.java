package fr.univ.exception;

/**
 * Exception levée lorsque nous sommes à <10j du début du Gala et que l'utilisateur essaye de se désinscrire (bah ouais
 * c'est un peu tard, fallait y penser avant quoi)!
 */
public class TooLateToUnsubscribeException extends Exception
{
    public TooLateToUnsubscribeException() {}

    public TooLateToUnsubscribeException(String message) {
        super(message);
    }

    public TooLateToUnsubscribeException(Throwable cause) {
        super(cause);
    }

    public TooLateToUnsubscribeException(String message, Throwable cause) {
        super(message, cause);
    }
}



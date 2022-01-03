package fr.univ.exception;

/**
 * Exception levée lorsque l'utilisateur choisit un nombre d'accompagnants trop élevé ou négatif.
 */
public class IllegalAccompanistNumberException extends Exception
{
    public IllegalAccompanistNumberException() {}

    public IllegalAccompanistNumberException(String message) {
        super(message);
    }

    public IllegalAccompanistNumberException(Throwable cause) {
        super(cause);
    }

    public IllegalAccompanistNumberException(String message, Throwable cause) {
        super(message, cause);
    }
}



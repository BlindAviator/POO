package fr.univ.exception;

/**
 * Exception levée lorsqu'un membre ne répond à un identifiant donné.
 */
public class NoSuchMemberException extends Exception
{
    public NoSuchMemberException() {}

    public NoSuchMemberException(String message) {
        super(message);
    }

    public NoSuchMemberException(Throwable cause) {
        super(cause);
    }

    public NoSuchMemberException(String message, Throwable cause) {
        super(message, cause);
    }
}



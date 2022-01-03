package fr.univ.exception;

/**
 * Exception levée lorsque l'utilisateur s'inscrit au Gala alors qu'il était déjà inscris.
 * Cette exception ne devrait jamais être levée.
 */
public class MemberIsAlreadySubscribeException extends Exception
{
    public MemberIsAlreadySubscribeException() {}

    public MemberIsAlreadySubscribeException(String message) {
        super(message);
    }

    public MemberIsAlreadySubscribeException(Throwable cause) {
        super(cause);
    }

    public MemberIsAlreadySubscribeException(String message, Throwable cause) {
        super(message, cause);
    }
}



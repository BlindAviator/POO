package fr.univ.exception;

/**
 * Exception levée lorsque l'utilisateur déclare être d'un type de membre de l'école incohérent avec l'identifiant donné
 * en entrée.
 * Par exemple, un utilisateur déclare qu'il est étudiant alors que l'instant d'après, il rentre un identifiant d'un
 * membre du personnel. L'identification doit échouer dans de telles circonstantes et lever cette exception permet
 * de recommencer l'opération en totalité. Cela évite que l'utilisateur reste bloqué indéfiniment sur un message
 * d'erreur tel que "Identifiant incorrect, essayez un nouvel identifiant".
 */
public class WrongMemberTypeException extends Exception
{
    public WrongMemberTypeException() {}

    public WrongMemberTypeException(String message) {
        super(message);
    }

    public WrongMemberTypeException(Throwable cause) {
        super(cause);
    }

    public WrongMemberTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}



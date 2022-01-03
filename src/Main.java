import fr.univ.controller.Controller;

import java.time.LocalDate;

/**
 * Classe principale contenant le point d'entrée du programme.
 */
public class Main
{
    /**
     * La méthode main, point d'entrée du programme
     * @param args paramètres d'entrée de l'application. Non utilisé.
     */
    public static void main(String [] args)
    {
        new Controller(LocalDate.of(2022, 1, 1));
    }
}

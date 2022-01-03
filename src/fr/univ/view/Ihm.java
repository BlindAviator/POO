package fr.univ.view;

import fr.univ.modele.Reservation;
import fr.univ.modele.Table;

import java.util.*;

/**
 * Classe de type Vue dont le but est d'être l'interface utilisateur.
 * Affiche du texte et récupère les entrées de l'utilsiateur.
 * Presque toutes les méthodes intègrent des bornes permettant de s'assurer de la cohérence des entrées de l'utilisateur.
 */
public class Ihm {

    /**
     * Objet chargé de récupérer les entrées de l'utilisateur.
     */
    private final Scanner sc = new Scanner( System.in);

    /**
     * Menu qui récupère le type de membre de l'école qu'est l'utilisateur. 1 = membre du personnel, 2 = étudiant
     * @return le type de membre de l'école qu'est l'utilisateur
     */
    public int chooseTypeOfMemberMenu()
    {
        int choice;
        do
        {
            // TODO explain the triple quote to Momo
            System.out.println("""
                               Veuillez saisir si vous êtes :
                                   (\033[1m1\033[0m) Membre du personnel
                                   (\033[1m2\033[0m) Étudiant""");
            choice = integerInputRequired();

            if( choice <= 0 || choice >= 3 )
                System.err.println("\nVeuillez saisir 1 ou 2.\n");

        } while(choice <= 0 || choice >= 3);
        return choice;
    }

    /**
     * Récupère l'identifiant du membre de l'école qu'est l'utilisateur.
     * @return l'identifiant du membre de l'école qu'est l'utilisateur
     */
    public int inputLoginNumber()
    {
        System.out.print("\nVeuillez saisir votre identifiant : ");
        return integerInputRequired();
    }

    /**
     * Affiche un menu permettant à l'utilisateur de s'inscrire au Gala ou de quitter l'application.
     * @return le choix de l'utilsiateur
     */
    public int chooseSubscription()
    {
        int choice;
        do
        {
            System.out.println("""
                               
                               Veuillez saisir pour :
                                   (\033[1m1\033[0m) Vous inscrire au gala
                                   (\033[1m2\033[0m) Quitter l'application""");
            choice = integerInputRequired();

            if( choice <= 0 || choice >= 3 )
                System.err.println("\nVeuillez saisir 1 ou 2.\n");

        }while(choice <= 0 || choice >= 3);
        return choice;
    }

    /**
     * Un menu permettant à l'utilisateur d'effectuer ou de consulter une réservation.
     * Permet également à l'utilisateur de confirmer sa réservation s'il y en a une ou de l'annuler s'il n'est pas trop
     * tard (càd <10j avant le début du Gala).
     * @param displayConfirmOption un indicateur permettant de savoir s'il faut proposer à l'utilisateur de pouvoir
     * confirmer sa réservation pour le gala (uniquement si le Gala débute dans <30j)
     * @return le choix de l'utilisateur
     */
    public int chooseMainMenu(boolean displayConfirmOption)
    {
        int choice;
        do
        {
            System.out.println("""
                               
                               Veuillez saisir pour :
                                   (\033[1m1\033[0m) Gérer les places du dîner
                                   (\033[1m2\033[0m) Vous désincrire
                                   (\033[1m3\033[0m) Quitter l'application""");
            if(displayConfirmOption)
                System.out.println("\t(\033[1m4\033[0m) Confirmer sa réservation"); // TODO Momo \t

            choice = integerInputRequired();

            if((choice <= 0 || choice >= 5) && displayConfirmOption)
                System.err.println("\nVeuillez saisir 1, 2, 3 ou 4.\n");
            else if(choice <= 0 || choice >= 5)
                System.err.println("\nVeuillez saisir 1, 2 ou 3.\n");

        }while(choice <= 0 || choice >= 5);
        return choice;
    }

    /**
     * Affiche quelles tables ont été réservé par quelles membres de l'école.
     * Uniquement les tables adressées aux membres du personnel sont affichées puisque le personnel ne peut pas avoir
     * accès aux tables des étudiants.
     * @param tables la liste des tables adressées aux membres du personnel
     */
    public void dispTables(List<Table> tables)
    {
        for(Table table : tables)
        {
            System.out.println("Numéro de table : " + table.getTableId());
            for( Reservation reservation : table.getReservations() )
            {
                System.out.print( reservation.getOwner().getFirstName() + " " + reservation.getOwner().getLastName());
                if( reservation.getNbrSeatsOccupied() > 1 )
                {
                    System.out.print(" + " + (reservation.getNbrSeatsOccupied()-1) + " accompagnants");
                }
                System.out.println();
            }
        }
    }

    /**
     * Indique à l'utilisateur le nombre maximal de places qu'il peut réserver pour des accompagnateurs.
     * @param maxNbrAccompanist le nombre maximal d'accompagnateurs
     */
    public void dispMaxNbrAccompanistStr(int maxNbrAccompanist)
    {
        System.out.println("\nVous pouvez réserver jusqu'à " + maxNbrAccompanist + " places.");
    }

    /**
     * Menu spécifique au personnel pour réaliser une réservation.
     * @return le choix de l'utilisateur, indiquant s'il souhaite ou non consulter le plan des tables
     */
    public char chooseReservationMenuStaff()
    {
        char choice;
        do
        {
            System.out.println("Consulter le plan des tables ? (O/N)");
            choice = charInputRequired();

            if(choice != 'o' && choice != 'n')
                System.err.println("\nVeuillez saisir O ou N.");

        }while(choice != 'o' && choice != 'n');
        return choice;
    }

    /**
     * Affiche une réservation.
     * @param reservation la réservation à afficher
     */
    public void dispReservation(Reservation reservation)
    {
        System.out.println(reservation);
    }

    /**
     * Affiche le coût total d'une réservation. Incluant le coût du membre de l'école et de ses utilisateurs.
     * @param price le coût total de la réservation.
     */
    public void dispReservationPrice(int price)
    {
        System.out.println("\nVous en aurez pour " + price + "€");
    }

    /**
     * Permet à l'utilisateur d'indiquer son nombre d'utilisateur.
     * @param maxNbrAccompanist le nombre maximal d'accompagnateurs afin d'assurer la cohérence de l'entrée de
     * l'utilisateur
     * @return le nombre d'accompagnateurs choisit par l'utilisateur
     */
    public int chooseNbrAccompanist(int maxNbrAccompanist)
    {
        int choice;
        do
        {

            System.out.print("\nVeuillez saisir le nombre d'accompagnants : ");
            choice = integerInputRequired();

            if( choice < 0 || choice > maxNbrAccompanist )
                System.err.println("Vous devez saisir entre 0 et " + maxNbrAccompanist + " accompagnants\n");

        }while(choice < 0 || choice > maxNbrAccompanist);
        return choice;
    }

    /**
     * Récupère le numéro de table ciblée par l'utilisateur.
     * @return le numéro de table ciblée par l'utilisateur
     */
    public int chooseTableId()
    {
        System.out.print("Veuillez saisir le numéro de table : ");
        return integerInputRequired();
    }

    /**
     * Indique à l'utilisateur que sa réservation a bien été confirmé. (Il apprend donc qu'il pourra participer au Gala).
     */
    public void dispConfirmation()
    {
        System.out.println("Votre participation au Gala a bien été enregistré.");
    }

    /**
     * Affiche un message.
     * @param msg le message à afficher
     */
    public void dispMessage(String msg)
    {
        System.out.println(msg);
    }

    /**
     * Assure qu'un entier est entré par l'utilisateur.
     * @return l'entier entré par l'utilisateur
     */
    public int integerInputRequired()
    {
        int integerInput;
        do
        {
            try {
                integerInput = sc.nextInt();
            } catch(NoSuchElementException nsee)
            {
                System.out.println("Veuillez saisir une valeur entière.");
                continue;
            }
            break;
        }while(true);
        return integerInput;
    }

    /**
     * Assure qu'un charactère est entré par l'utilisateur.
     * @return le caractère entré par l'utilisateur
     */
    public char charInputRequired()
    {
        char charInput;
        do
        {
            try {
                charInput = sc.nextLine().toLowerCase().charAt(0);
            } catch(NoSuchElementException | IndexOutOfBoundsException e)
            {
                System.err.println("\nVeuillez saisir une lettre.\n");
                continue;
            }
            break;
        }while(true);
        return charInput;
    }
}

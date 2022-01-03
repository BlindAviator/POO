package fr.univ.controller;

import fr.univ.exception.*;
import fr.univ.io.ServiceStockage;
import fr.univ.modele.Gala;
import fr.univ.modele.Student;
import fr.univ.view.Ihm;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;

/**
 * Classe de type Controller, typique des architectures MVC pour faire le lien entre la vue et le modèle.
 */
public class Controller
{
    /**
     * Attribut permettant d'échanger avec le disque dur afin de sérialiser et charger une instance Gala + les instances
     * qui y sont attachées.
     */
    private ServiceStockage io;
    /**
     * Attribut contenant une instance du modèle Gala.
     */
    private Gala gala;
    /**
     * Attribut contenant une instance de la vue Ihm.
     */
    private final Ihm ihm;

    /**
     * Stocke pour toute la durée du programme l'identifiant de l'utilisateur.
     */
    private int userId;
    /**
     * Stocke le type de membre de l'école. 1 = membre du personnel ; 2 = étudiant.
     */
    private int memberType;

    /**
     * Constructeur de la classe Controller, en charge d'initialiser correctement nos attributs modèle et vue.
     * Peut varier efficacement à combien de jours du gala nous sommes puis démarre l'application.
     * @param galaDate La date à laquelle débute le Gala. Pratique si le fichier de sérialisation n'existe pas encore
     * (comme c'est le cas pour une première execution) et qu'il faut donc instancier une nouvelle Classe Gala.
     */
    public Controller(LocalDate galaDate)
    {
        initServiceStockage();
        initGala(galaDate);
        this.ihm = new Ihm();

        gala.setCurrentDate(galaDate.minusDays(15)); // Gala begin in 15 days

        launchApplication();
    }

    //---------------------------------- INITIALISATION AND I/O METHODS

    /**
     * Initialisation d'une instance ServiceStockage.
     */
    private void initServiceStockage()
    {
        try {
            io = new ServiceStockage();
        } catch(IOException e) {
            System.err.println("Un problème est survenu lors de l'initialisation avec le fichier \033[1mgala.ser\033[0m");
            e.printStackTrace();
        }
    }

    /**
     * Initialisation d'une instance Gala. Si loadGala() ne trouve pas de fichier de sérialisation, alors Gala est
     * instancié.
     * @param galaDate
     */
    private void initGala(LocalDate galaDate)
    {
        this.gala = loadGala(); // Try to load Gala from file gala.ser
        if( gala == null ) // if we launch the application for the first time ever
        {
            try
            {
                gala = new Gala(galaDate);
            } catch( FileNotFoundException e )
            {
                System.err.println("Un fichier permettant l'initialisation des membres de l'école est introuvable.");
                e.printStackTrace();
                System.exit(1);
            }
            saveGala(gala); // Write on disk the new instance Gala.
        }
    }

    /**
     * Charge une instance Gala depuis le disque dur
     * @return Une instance Gala sérialisée
     */
    private Gala loadGala()
    {
        try{
            Object obj = io.charger();
            if(obj == null)
            {
                return null;
            }
            else
            {
                return (Gala)obj;
            }
        } catch(IOException | ClassNotFoundException e){
            System.err.println("Une erreur est survenue lors d'un traitement de serialisation");
            e.printStackTrace();
            System.exit(1);
            return null; // This line can't happen. However, remove it generate compilation error.
        }
    }

    /**
     * Sauvegarde une instance Gala sur le disque dur
     * @param gala l'instant à sauvegarder
     */
    private void saveGala(Gala gala)
    {
        try
        {
            io.enregistrer(gala);
        } catch(IOException e)
        {
            System.err.println("Une erreur est survenue lors de la sauvegarde de l'instance \033[1;31mGala\033[0m.");
            e.printStackTrace();
            System.exit(1);
        }
    }

    //--------------------------- "USER" METHOD

    /**
     * Méthode principale en charge d'executer toute la partie visible pour l'utilisateur.
     */
    private void launchApplication()
    {
        boolean repeatLoop;

        // we need this loop, because if a student input accidentally that he's in staff, he would be blocked...
        do
        {
            memberType = ihm.chooseTypeOfMemberMenu(); // Retrieve if the user is a Student or a Staff
            repeatLoop = connecterUser();
        }while(repeatLoop);

        do
        {
            if(gala.isMemberSubscribed(userId))
            {
                repeatLoop = mainMenu(); // Disp the menu with the options like "Gérer les tables"
            }
            else
            {
                repeatLoop = subscriptionMenu(); // Disp the menu with options like "S'inscrire au Gala"
            }
        }while(repeatLoop);

        System.out.println("\nMerci d'avoir utilisé notre application de Gala !");
    }

    /**
     * Connecte un utilisateur, en s'assurant que son identifiant est un identifiant valide
     * @return true si l'opération de connexion est un échec et nécessite d'être réitérée. False sinon.
     */
    private boolean connecterUser()
    {
        do
        {
            userId = ihm.inputLoginNumber();

            if(memberType == 1) // If the member is a Staff
            {
                try {
                    gala.getStaffById(userId); // allow the program to know if the user is really a Staff as he has chosen previously
                } catch(WrongMemberTypeException wmte) // If the user is a Student, we return true to repeat connection procedure
                {
                    System.err.println("\n"+wmte.getMessage()+"\n");
                    return true;
                } catch(NoSuchMemberException nsme)
                {
                    System.err.println("Identifiant invalide. Réessayez.\n");
                    continue;
                }
            }
            else // otherwise, the user is a Student
            {
                try
                {
                    gala.getStudentById(userId); // allow the program to know if the user is really a Student as he has chosen previously
                } catch(WrongMemberTypeException wmte) // If the user is a Staff, we return true to repeat connection procedure
                {
                    System.err.println("\n"+wmte.getMessage()+"\n");
                    return true;
                } catch(NoSuchMemberException e)
                {
                    System.err.println("Identifiant invalide. Réessayez.\n");
                    continue;
                }
            }

            break;
        }while(true);

        return false; // The connection operation is a success and do not need to be repeated.
    }

    /**
     * Affiche le 1er menu après la connexion. C'est aussi le 1er menu si l'utilisateur n'est pas encore inscris pour
     * participer au Gala.
     * @return true si l'execution du programme doit continuer. false si l'utilisateur décide de quitter l'application.
     */
    private boolean subscriptionMenu()
    {
        int choice = ihm.chooseSubscription();
        if(choice == 1)
            subscribeMember();
        else
            return false;

        return true;
    }

    /**
     * Affiche le menu principal. Celui où, après s'être inscris, on fait des demandes de réservation.
     * @return true si l'execution du programme doit continuer, false sinon.
     */
    private boolean mainMenu()
    {
        int choice = ihm.chooseMainMenu( haveToDisplayConfirmOption() );
        if(choice == 1) // Choice "Gérer les tables"
        {
            if(gala.hasReserved(userId)) // If the member already has a reservation...
            {
                showReservation(); // ...we show the reservation
            }
            else // If the member hasn't a reservation...
            {
                if(memberType == 1)
                    reservationMenuStaff(); // We start a reservation form for a staff
                else
                    reservationMenuStudent(); // We start a reservation form for a student
                return false;
            }
        }
        else if(choice == 2) // Choice "Se désinscrire"
        {
            unsubscribeMember();
        }
        else if(choice == 3) // Choice "Quitter l'application"
        {
            return false;
        }
        else if(choice == 4) // Choice "Confirmer"
        {
            confirmMemberParticipation();
        }

        return true;
    }

    /**
     * Inscris l'utilisateur au Gala
     */
    private void subscribeMember()
    {
        try
        {
            gala.subscribeMember(userId);
            saveGala(gala); // serialize Gala because the model has juste changed.
        } catch(MemberIsAlreadySubscribeException mhre)
        {
            System.err.println("\n"+mhre.getMessage()+"\n");
        }
    }

    /**
     * Désincris l'utilisateur du Gala. Supprime également toutes ses réservations.
     */
    private void unsubscribeMember()
    {
        try
        {
            gala.removeReservation(userId);
            gala.unsubscribeMember(userId);
            saveGala(gala); // serialize Gala because the model has juste changed.
            ihm.dispMessage("\nVous avez été désinscris.");
        } catch(NoSuchSubscriptionException | TooLateToUnsubscribeException e)
        {
            System.err.println("\n"+e.getMessage()+"\n");
        } catch(NoSuchReservationException ignored){} // Member doesn't necessarily have a reservation
    }

    private void confirmMemberParticipation()
    {
        if(gala.isMemberOnHold(userId))
        {
            try{
                gala.acceptMember(userId); // confirm member participation
                saveGala(gala); // serialize Gala because the model has juste changed.
                ihm.dispConfirmation();
            } catch( NoSuchMemberException e ){e.printStackTrace();} // Can't happen
        }
    }

    /**
     * Etant donné que le choix "(4) Confirmer" est optionnel, il nous faut une méthode booléenne afin de déterminer
     * si cette option doit être affichée. L'option est affichée si nous sommes à <=30j du gala et qu'il s'agit d'un
     * étudiant ayant une réservation qui n'a pas encore été confirmé par ce dernier.
     * @return true si l'option "(4) Confirmer" doit être affichée.
     */
    private boolean haveToDisplayConfirmOption()
    {
        if(gala.getNbrDaysBeforeGala() > 30 || memberType == 1)
            return false;

        return gala.isMemberOnHold(userId);
    }

    /**
     * Menu de réservation spécifique aux étudiants étant donné que les options de base dont ils disposent ne sont pas
     * les mêmes que les options du personnel.
     */
    private void reservationMenuStudent()
    {
        // according to the type of member school and the student's year, the maximum accompanist number
        int maxNbrAccompanist = getMaxNbrAccompanist(memberType, userId);
        ihm.dispMaxNbrAccompanistStr(maxNbrAccompanist);

        do
        {
            try {
                int price = gala.addReservation(userId, ihm.chooseNbrAccompanist(maxNbrAccompanist));
                ihm.dispReservationPrice(price);
                saveGala(gala); // serialize Gala because the model has juste changed.
            } catch(MemberAlreadyHasReservationException | NoSeatRemainingException e)
            {
                System.err.println("\n"+e.getMessage()+"\n");
                return;
            } catch(IllegalAccompanistNumberException e) // Too much or too few accompanists
            {
                System.err.println("\n"+e.getMessage()+"\n");
                continue;
            }
            break;
        }while(true);

        System.out.println("Réservation enregistrée.");
    }


    /**
     * Menu de réservation spécifique au personnel étant donné que les options de base dont ils disposent ne sont pas
     * les mêmes que les options des étudiants.
     */
    private void reservationMenuStaff()
    {
        // according to the type of member school and the student's year, the maximum accompanist number
        int maxNbrAccompanist = getMaxNbrAccompanist(memberType, userId);
        ihm.dispMaxNbrAccompanistStr(maxNbrAccompanist);

        char yesNoAnswer = ihm.chooseReservationMenuStaff(); // Retrieve if the user want to choose his table
        if( yesNoAnswer == 'o' )
        {
            do
            {
                ihm.dispTables(gala.getStaffTables()); // display tables
                try
                {
                    // retrieve 2 inputs : the number of accompanists, but also the table chosen
                    int price = gala.addReservation(userId, ihm.chooseNbrAccompanist(maxNbrAccompanist), ihm.chooseTableId());
                    // Auto-confirm staff participation. By default, they don't have a "(4) Confirmer" option.
                    gala.acceptMember(userId);
                    ihm.dispReservationPrice(price);
                    saveGala(gala); // serialize Gala because the model has juste changed.
                } catch(MemberAlreadyHasReservationException e)
                {
                    System.err.println("\n"+e.getMessage()+"\n");
                    return;
                } catch(TablePermissionException | IllegalAccompanistNumberException | NoSuchTableException | NoSeatRemainingException e)
                {
                    System.err.println("\n"+e.getMessage()+"\n");
                    continue;
                } catch(NoSuchMemberException e){e.printStackTrace();} // Can't happen
                break;
            }while(true);
        }
        else
        {
            do
            {
                try {
                    // retrieve 1 input : the number of accompanists
                    int price = gala.addReservation(userId, ihm.chooseNbrAccompanist(maxNbrAccompanist));
                    // Auto-confirm staff participation. By default, they don't have a "(4) Confirmer" option.
                    gala.acceptMember(userId);
                    ihm.dispReservationPrice(price);
                    saveGala(gala); // serialize Gala because the model has juste changed.
                } catch(MemberAlreadyHasReservationException | NoSeatRemainingException e)
                {
                    System.err.println("\n"+e.getMessage()+"\n");
                    return;
                } catch(IllegalAccompanistNumberException e)
                {
                    System.err.println("\n"+e.getMessage()+"\n");
                    continue;
                } catch(NoSuchMemberException e){e.printStackTrace();}
                break;
            }while(true);
        }

        System.out.println("Réservation enregistrée.");
    }

    /**
     * Affiche la réservation actuelle de l'utilisateur (lorsqu'il choisit l'option "(1) Gérer les tables").
     */
    private void showReservation()
    {
        try {
            ihm.dispReservation(gala.getReservationById(userId));
        } catch(NoSuchReservationException nsre)
        {
            System.err.println(nsre.getMessage());
        }
    }

    /**
     * Calcule le nombre maximal d'accompagnants selon que l'utilisateur soit un membre du staff, un étudiant en M2 ou
     * un étudiant en <M2
     * @param memberType Type de membre de l'école. Vaut 1 pour un membre du staff, sinon 2 pour un étudiant.
     * @param userId Identifiant de l'utilisateur
     * @return le nombre maximal d'accompagnant pour l'utilisateur
     */
    private int getMaxNbrAccompanist(int memberType, int userId)
    {
        if(memberType == 1) // If the user is a staff
            return gala.getMAX_STAFF_ACCOMPANIST();
        else { // if the membre is a student
            try{
                Student student = gala.getStudentById(userId);
                if( student.getYear() == 5 ) // The student is in M2
                    return gala.getMAX_ACCOMPANIST_M2();
                else // The student is in <M2
                    return gala.getMAX_ACCOMPANIST_LESSER_THAN_M2();
            } catch( WrongMemberTypeException | NoSuchMemberException e ) // Both exceptions shouldn't happen
            {
                e.printStackTrace();
                return 0; // Shouldn't return 0
            }
        }
    }
}



package fr.univ.modele;

import fr.univ.exception.*;

import java.io.FileNotFoundException;
import java.io.Serial;
import java.io.Serializable;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;
import java.io.File;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import java.util.Map;

/**
 * Classe représentant le modèle de l'application. Elle contient les constantes principales et est en charge de la
 * gestion générale des données. Que ce soit pour stocker les membres de l'école, la date du jour ou la date de début du
 * gala, mais aussi les différents inscrits au Gala, leurs demandes de réservations, la file d'attente, etc.
 */
public class Gala implements Serializable {

    /**
     * Constante nécessaire afin de permettre la sérialisation de la classe Gala.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Date de début du Gala
     */
    private final LocalDate galaDate;
    /**
     * Date du jour
     */
    private LocalDate currentDate;

    /**
     * Map des membres de l'école
     */
    private final Map<Integer, SchoolMember> members;

    /**
     * Map des membres de l'école qui se sont inscrits au Gala
     */
    private final Map<Integer, SchoolMember> subscribedMembers;
    /**
     * Map des membres de l'école qui ont fait une réservation pour participer au Gala
     */
    private final Map<Integer, Reservation> reservations;

    /**
     * Map des membres de l'école pour lesquels une réservation est actuellement en attente
     */
    private final Map<Integer, SchoolMember> membersOnHold;
    /**
     * Map des membres de l'école pour lesquels leur réservation a bien été validé, leur participation au Gala pourra
     * donc avoir lieu
     */
    private final Map<Integer, SchoolMember> acceptedMembers;

    /**
     * Map des tables du Gala
     */
    private final Map<Integer, Table> tables;

    /**
     * Prix d'entrée pour un M2
     */
    private final int PRICE_M2 = 10;
    /**
     * Prix d'entrée pour un <M2
     */
    private final int PRICE_LESSER_THAN_M2 = 15;
    /**
     * Prix d'entrée pour un membre du personnel
     */
    private final int PRICE_STAFF = 20;
    /**
     * Nombre d'accompagnant maximal pour un membre du personnel
     */
    private final int MAX_STAFF_ACCOMPANIST = 1;
    /**
     * Nombre d'accompagnant maximal pour un M2
     */
    private final int MAX_ACCOMPANIST_M2 = 3;
    /**
     * Nombre d'accompagnant maximal pour un <M2
     */
    private final int MAX_ACCOMPANIST_LESSER_THAN_M2 = 1;
    /**
     * Nombre de places maximales autour d'une table
     */
    private final int MAX_SEATS_NBR = 8;
    /**
     * Nombre de tables allouées aux étudiants
     */
    private final int STUDENTS_TABLES = 15;
    /**
     * Nombre de tables allouées aux membres du personnel
     */
    private final int STAFF_TABLES = 10;

    /**
     * Constructeur de la classe Gala. Est en chargé d'initialisé la totalité des variables membres.
     * @param galaDate Date de début du Gala
     * @throws FileNotFoundException Un jeu de donné (de type fichier texte) n'existe pas. L'initialisation échoue.
     */
    public Gala(LocalDate galaDate) throws FileNotFoundException
    {
        this.galaDate = galaDate;
        this.currentDate = galaDate.minusMonths(2);

        this.members = new HashMap<>();
        this.membersOnHold = new HashMap<>();
        this.subscribedMembers = new HashMap<>();
        this.acceptedMembers = new HashMap<>();
        this.tables = new HashMap<>();
        this.reservations = new HashMap<>();

        studentsFileReading();
        staffsFileReading();
        tablesCreation();
    }

    //-------------------------- INITIALIZATION

    // Weird to find an io method in Gala class... Would it really be useful to create a new class in IO package ? Probably not.

    /**
     * Méthode transformant un fichier texte en une liste des lignes du fichier.
     * @param filePath Un fichier
     * @return Une liste avec une ligne par indice
     * @throws FileNotFoundException Le fichier devant être lu n'existe pas
     */
    private List<String> readFile(String filePath) throws FileNotFoundException
    {
        Scanner fileReader = new Scanner(new File(filePath));
        List<String> lines = new ArrayList<>();

        while (fileReader.hasNextLine())
            lines.add(fileReader.nextLine());

        return lines;
    }

    /**
     * Initialise les étudiants, depuis un jeu de données donné sous forme de fichier texte.
     * @throws FileNotFoundException Le fichier n'existe pas.
     */
    private void studentsFileReading() throws FileNotFoundException
    {
        List<String> studentsInformations = readFile("resources/dataset/etudiants.txt");
        for(String line : studentsInformations)
        {
            String[] studentInfos = line.split("\t");
            members.put(
                    Integer.parseInt(studentInfos[0]), // key
                    new Student( // value
                                 Integer.parseInt(studentInfos[0]), studentInfos[1], studentInfos[2], studentInfos[3],
                                 studentInfos[4], Integer.parseInt(studentInfos[5])
                    )
            );
        }
    }

    /**
     * Initialise le personnel, depuis un jeu de données donné sous forme de fichier texte.
     * @throws FileNotFoundException Le fichier n'existe pas.
     */
    private void staffsFileReading() throws FileNotFoundException
    {
        List<String> staffsInformations = readFile("resources/dataset/personnel.txt");
        for(String line : staffsInformations)
        {
            String[] staffInfos = line.split( "\t" );
            members.put(
                    Integer.parseInt( staffInfos[0] ), // key
                    new Staff( // value
                               Integer.parseInt( staffInfos[0] ), staffInfos[1], staffInfos[2], staffInfos[3],
                               staffInfos[4]
                    )
            );
        }
    }

    /**
     * Initialise les tables du Gala.
     */
    private void tablesCreation()
    {
        // init tables, with a numbering beginning at 1, as described in the subject.
        for (int i = 1; i <= (STUDENTS_TABLES + STAFF_TABLES); ++i )
            tables.put(i, new Table(i, MAX_SEATS_NBR));
    }

    //---------------------------------- MEMBERS

    /**
     * Récupère un membre de l'école depuis son identifiant.
     * @param memberId L'identifiant du membre que l'on recherche
     * @return Un membre de l'école
     * @throws NoSuchMemberException Aucun membre n'existe pour l'identifiant donné
     */
    public SchoolMember getMemberById(int memberId) throws NoSuchMemberException
    {
        if (!members.containsKey(memberId))
            throw new NoSuchMemberException("La personne recherchée est introuvable");

        return members.get(memberId);
    }

    /**
     * Récupère un étudiant depuis son identifiant.
     * @param studentId L'identifiant de l'étudiant que l'on recherche
     * @return Un étudiant
     * @throws NoSuchMemberException Aucun étudiant n'existe pour l'identifiant donné
     */
    public Student getStudentById(int studentId) throws WrongMemberTypeException, NoSuchMemberException
    {
        SchoolMember member = getMemberById(studentId);

        if( member instanceof Staff )
            throw new WrongMemberTypeException("L'étudiant recherché est en fait un membre du personnel");

        return (Student)member;
    }

    /**
     * Récupère un membre du personnel depuis son identifiant.
     * @param staffId L'identifiant du membre du personnel que l'on recherche
     * @return Un membre du personnel
     * @throws NoSuchMemberException Aucun membre du personnel n'existe pour l'identifiant donné
     */
    public Staff getStaffById(int staffId) throws WrongMemberTypeException, NoSuchMemberException
    {
        SchoolMember member = getMemberById(staffId);

        if( member instanceof Student )
            throw new WrongMemberTypeException("Le membre du personnel recherché est en fait un étudiant");

        return (Staff)member;
    }

    //-------------------------- SUBSCRIPTION

    /**
     * Inscrit un membre de l'école au Gala.
     * @param membreId l'identifiant du membre de l'école
     * @throws MemberIsAlreadySubscribeException L'utilisateur est déjà inscrit au Gala et ne peut se réinscrire
     */
    public void subscribeMember(int membreId) throws MemberIsAlreadySubscribeException
    {
        if (subscribedMembers.containsKey(membreId))
            throw new MemberIsAlreadySubscribeException( "Vous êtes déjà inscrit");

        subscribedMembers.put(membreId, members.get(membreId));
    }

    /**
     * Cherche à savoir si le membre de l'école est déjà inscrit au Gala.
     * @param memberId l'identifiant du membre de l'école dont on cherche a connaître son inscription
     * @return true si le membre de l'école est effectivement inscrit, false sinon
     */
    public boolean isMemberSubscribed(int memberId)
    {
        return subscribedMembers.containsKey(memberId);
    }

    /**
     * Désinscris le membre de l'école du Gala.
     * @param userId l'identifiant du membre de l'école que l'on souhaite désinscrire
     * @throws NoSuchSubscriptionException Le membre de l'école n'est pas inscrit et ne peut donc pas être désinscrit
     */
    public void unsubscribeMember(int userId) throws NoSuchSubscriptionException
    {
        if(!subscribedMembers.containsKey(userId))
            throw new NoSuchSubscriptionException("Vous n'avez aucune réservation");

        subscribedMembers.remove(userId);
    }

    //------------------------- RESERVATION

    /**
     * Ajoute une réservation au Gala. Cela a également pour effet :
     * - d'ajouter la réservation à la table ciblé par la réservation,
     * - d'ajouter le membre de l'école en liste d'attente.
     * Effectue beaucoup de vérification (voir exceptions ci-dessous).
     * @param memberId l'identifiant du membre de l'école effectuant la réservation
     * @param nbrAccompanist le nombre d'accompagnants du membre de l'école
     * @return Le coût total de la réservation
     * @throws MemberAlreadyHasReservationException Le membre de l'école dispose déjà d'une réservation et ne peut en
     * avoir une seconde
     * @throws IllegalAccompanistNumberException Le nombre d'accompagnants est incorrect : soit trop grand, soit négatif
     * @throws NoSeatRemainingException La table n'a pas assez de place pour permettre la réservation
     */
    public int addReservation(int memberId, int nbrAccompanist) throws MemberAlreadyHasReservationException,
            IllegalAccompanistNumberException, NoSeatRemainingException
    {
        if (reservations.containsKey(memberId))
            throw new MemberAlreadyHasReservationException( "Vous avez déjà une reservation effectuée avec cet identifiant.");

        SchoolMember member = members.get(memberId);
        Reservation reservation = null;
        int tableId = -1;

        if(member instanceof Staff)
        {
            if ( nbrAccompanist > MAX_STAFF_ACCOMPANIST )
                throw new IllegalAccompanistNumberException( "Vous ne pouvez pas réserver plus de " + MAX_ACCOMPANIST_M2 +
                                                             "places en tant que membre du personnel.");

            // Find a table with enough seats to receive the reservation
            List<Table> staffTables = getStaffTables(); 
            for(Table table : staffTables) {
                if(table.getSeatsRemaining() >= nbrAccompanist + 1) {
                    tableId = table.getTableId();
                    break;
                }
            }
            if( tableId == -1 )
                throw new NoSeatRemainingException("Aucune place restante");

            reservation = new Reservation(tableId, member, nbrAccompanist+1, (nbrAccompanist+1) * PRICE_STAFF, currentDate);
        }
        else if(member instanceof Student student)
        {
            if (student.getYear() == 5 && nbrAccompanist > MAX_ACCOMPANIST_M2 )
                throw new IllegalAccompanistNumberException( "Vous ne pouvez pas réserver plus de " +
                                                             MAX_ACCOMPANIST_M2 + "places en tant que M2");
            if (student.getYear() < 5 && nbrAccompanist > MAX_ACCOMPANIST_LESSER_THAN_M2 )
                throw new IllegalAccompanistNumberException( "Vous ne pouvez pas réserver plus de " +
                                                             MAX_ACCOMPANIST_LESSER_THAN_M2 + "places en tant que <M2");

            // Find a table with enough seats to receive the reservation
            List<Table> studentTables = getStudentTables();
            for(Table table : studentTables) {
                if(table.getSeatsRemaining() >= nbrAccompanist + 1) {
                    tableId = table.getTableId();
                    break;
                }
            }
            if( tableId == -1 )
                throw new NoSeatRemainingException("Aucune place restante");

            if(student.getYear() == 5)
                reservation = new Reservation(tableId, member, nbrAccompanist+1, (nbrAccompanist+1) * PRICE_M2, currentDate);
            else
                reservation = new Reservation(tableId, member, nbrAccompanist+1, (nbrAccompanist+1) * PRICE_LESSER_THAN_M2, currentDate);
        }

        // Add the reservation to the table
        tables.get(tableId).addReservation(reservation); // don't trust IntelliJ :  can't be null --- Could you explain me why Momo?
        reservations.put(memberId, reservation);
        membersOnHold.put( memberId, member);
        return reservation.getReservationPrice();
    }

    /**
     * Ajoute une réservation au Gala. Cela a également pour effet :
     * - d'ajouter la réservation à la table ciblé par la réservation,
     * - d'ajouter le membre de l'école en liste d'attente.
     * Cette méthode est surchargée d'un paramètre "tableId". Dans ce scénario de réservation, la table recevant la
     * réservation est choisie.
     * Effectue beaucoup de vérification (voir exceptions ci-dessous).
     * @param memberId l'identifiant du membre de l'école effectuant la réservation
     * @param nbrAccompanist le nombre d'accompagnants du membre de l'école
     * @param tableId l'identifiant de la table ciblée par la réservation
     * @return Le coût total de la réservation
     * @throws MemberAlreadyHasReservationException Le membre de l'école dispose déjà d'une réservation et ne peut en
     * avoir une seconde
     * @throws TablePermissionException La table choisit n'est pas compatible avec le type de membre de l'école (un
     * membre du personnel ne peut pas réserver pour une table destinée aux étudiants)
     * @throws IllegalAccompanistNumberException Le nombre d'accompagnants est incorrect : soit trop grand, soit négatif
     * @throws NoSuchTableException La table ciblée n'existe pas
     * @throws NoSeatRemainingException La table n'a pas assez de place pour permettre la réservation
     */
    public int addReservation(int memberId, int nbrAccompanist, int tableId) throws MemberAlreadyHasReservationException,
            TablePermissionException, IllegalAccompanistNumberException, NoSuchTableException, NoSeatRemainingException
    {
        if (reservations.containsKey(memberId))
            throw new MemberAlreadyHasReservationException( "Vous avez déjà une reservation effectuée avec cet identifiant.");

        SchoolMember member = members.get(memberId);
        Reservation reservation = null;

        if(member instanceof Staff)
        {
            if(tableId <= STUDENTS_TABLES)
                throw new TablePermissionException("Un membre du personnel ne peut pas faire de réservation pour une " +
                                                   "table destinée aux étudiants" );

            if ( nbrAccompanist > MAX_STAFF_ACCOMPANIST )
                throw new IllegalAccompanistNumberException( "Vous ne pouvez pas réserver plus de " +
                                                             MAX_ACCOMPANIST_M2 + "places");

            reservation = new Reservation(tableId, member, nbrAccompanist+1, (nbrAccompanist+1) * PRICE_STAFF, currentDate);
        }
        else if(member instanceof Student student)
        {
            if(tableId > STUDENTS_TABLES)
                throw new TablePermissionException("Un étudiant ne peut pas faire de réservation pour une table " +
                                                   "destinée aux membres du personnel");

            if (student.getYear() == 5 && nbrAccompanist > MAX_ACCOMPANIST_M2 )
                throw new IllegalAccompanistNumberException( "Vous ne pouvez pas réserver plus de " +
                                                             MAX_ACCOMPANIST_M2 + "places");
            if (student.getYear() < 5 && nbrAccompanist > MAX_ACCOMPANIST_LESSER_THAN_M2 )
                throw new IllegalAccompanistNumberException( "Vous ne pouvez pas réserver plus de " +
                                                             MAX_ACCOMPANIST_LESSER_THAN_M2 + "places");

            if(student.getYear() == 5)
                reservation = new Reservation(tableId, member, nbrAccompanist+1, (nbrAccompanist+1) * PRICE_M2, currentDate);
            else
                reservation = new Reservation(tableId, member, nbrAccompanist+1, (nbrAccompanist+1) * PRICE_LESSER_THAN_M2, currentDate);
        }

        if(!tables.containsKey(tableId))
            throw new NoSuchTableException("La table demandée n'existe pas.");

        // Add the reservation to the table
        tables.get(tableId).addReservation(reservation); // don't trust IntelliJ :  can't be null --- Could you explain me why Momo?
        reservations.put(memberId, reservation);
        membersOnHold.put( memberId, member);
        return reservation.getReservationPrice();
    }

    /**
     * Récupère une réservation à partir de l'identifiant du membre de l'école propriétaire de cette réservation.
     * @param memberId identifiant du membre de l'école possédant la réservation
     * @return La réservation du membre de l'école donné en paramètre
     * @throws NoSuchReservationException La réservation n'existe pas
     */
    public Reservation getReservationById(int memberId) throws NoSuchReservationException
    {
        if (!reservations.containsKey(memberId))
            throw new NoSuchReservationException("La réservation demandée n'existe pas");

        return reservations.get(memberId);
    }

    /**
     * Cherche à savoir si le membre de l'école donné en paramètre possède une réservation pour le Gala.
     * @param memberId le membre de l'école
     * @return true si une réservation est trouvée, false sinon
     */
    public boolean hasReserved(int memberId)
    {
        return reservations.containsKey(memberId);
    }

    /**
     * Supprime une réservation d'un membre de l'école donné en paramètre. Cela engendre diverses opérations :
     * - La réservation est supprimée de la table ciblée par ladite réservation,
     * - Le membre de l'école est supprimé de la liste d'attente,
     * - Enfin, la réservation est supprimée de la map des réservations.
     * @param userId L'identifiant de l'utilisateur dont on souhaite supprimer sa réservation
     * @throws NoSuchReservationException l'utilisateur en question ne dispose d'aucune réservation
     * @throws TooLateToUnsubscribeException Nous sommes à <10j du Gala et il est donc impossible de supprimer la
     * réservation
     */
    public void removeReservation(int userId) throws NoSuchReservationException, TooLateToUnsubscribeException
    {
        if(!reservations.containsKey(userId))
            throw new NoSuchReservationException("La réservation demandée n'existe pas");

        if(currentDate.plusDays(10).isAfter(galaDate))
            throw new TooLateToUnsubscribeException("Il est impossible de se désincrire à moins de 10 jours du début du gala");

        // Remove the reservation from the table
        tables.get(reservations.get(userId).getTableId()).removeReservation(userId);
        membersOnHold.remove(userId);
        reservations.remove(userId);
    }

    //----------------------------- HOLD AND ACCEPTED

    /**
     * Cherche à savoir si le membre de l'école donné en paramètre est actuellement en attente.
     * @param memberId le membre de l'école ciblé par la méthode
     * @return vrai s'il est en attente, faux sinon
     */
    public boolean isMemberOnHold(int memberId)
    {
        return membersOnHold.containsKey( memberId);
    }

    /**
     * Confirme la réservation d'un membre de l'école et valide ainsi sa participation au Gala.
     * @param memberId le membre de l'école dont on souhaite confirmer la réservation
     * @throws NoSuchMemberException Le membre en question n'existe pas
     */
    public void acceptMember(int memberId) throws NoSuchMemberException
    {
        if (!membersOnHold.containsKey( memberId))
            throw new NoSuchMemberException("Impossible de trouver le membre demandé");

        SchoolMember member = membersOnHold.get( memberId);
        membersOnHold.remove( memberId);
        acceptedMembers.put( memberId, member);
    }

    //------------------------------------- GETTERS

    /**
     * Récupère exclusivement les tables adressées aux étudiants.
     * @return Les tables des étudiants
     */
    public List<Table> getStudentTables()
    {
        // students tables are numbered from 1 to 15
        List<Table> studentsTables = new ArrayList<>();
        for (int i = 1; i <= STUDENTS_TABLES; ++i)
            studentsTables.add(tables.get(i));

        return studentsTables;
    }

    /**
     * Récupère exclusivement les tables adressées au personnel.
     * @return Les tables du personnel
     */
    public List<Table> getStaffTables()
    {
        // students tables are numbered from 11 to 25
        List<Table> staffTables = new ArrayList<>();
        for (int i = (STUDENTS_TABLES+1); i <= (STUDENTS_TABLES+STAFF_TABLES); ++i)
            staffTables.add( tables.get( i));

        return staffTables;
    }

    /**
     * Récupère le nombre de jours avant le début du gala.
     * @return le nombre de jours avant le début du gala
     */
    public int getNbrDaysBeforeGala()
    {
        return (int)currentDate.until(galaDate, ChronoUnit.DAYS);
    }

    /**
     * Renvoie la constante statuant du nombre maximal d'accompagnateurs pour un membre du personnel
     * @return le nombre maximal d'accompagnateurs que peut avoir un membre du personnel
     */
    public int getMAX_STAFF_ACCOMPANIST() {
        return MAX_STAFF_ACCOMPANIST;
    }

    /**
     * Renvoie la constante statuant du nombre maximal d'accompagnateurs pour un M2
     * @return le nombre maximal d'accompagnateurs que peut avoir un M2
     */
    public int getMAX_ACCOMPANIST_M2() {
        return MAX_ACCOMPANIST_M2;
    }

    /**
     * Renvoie la constante statuant du nombre maximal d'accompagnateurs pour un <M2
     * @return le nombre maximal d'accompagnateurs que peut avoir un <M2
     */
    public int getMAX_ACCOMPANIST_LESSER_THAN_M2() {
        return MAX_ACCOMPANIST_LESSER_THAN_M2;
    }

    /**
     * Modifie la date du jour/la date actuelle.
     * @param currentDate la nouvelle date représentant le date du jour
     */
    public void setCurrentDate(LocalDate currentDate)
    {
        this.currentDate = currentDate;
    }
}




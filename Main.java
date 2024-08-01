package NyamiMakouet;

import java.io.IOException;
import java.util.Date;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Admin admin = null;

        // Charger les données si elles existent
        try {
            admin = Admin.chargerDonnees("time_log_data.json");
            if (admin == null) {
                System.out.println("Donnees non chargees, creation d'un nouvel admin.");
                admin = new Admin("admin", "admin");
                sauvegarderDonnees(admin); // Sauvegarder les données initialisées
            } else {
                System.out.println("Donnees chargees avec succes.");
            }
        } catch (Exception e) {
            System.out.println("Erreur inattendue lors du chargement des donnees: " + e.getMessage());
            admin = new Admin("admin", "admin");
            sauvegarderDonnees(admin); // Sauvegarder les données initialisées
        }

        // Menu interactif
        while (true) {
            System.out.println("\nBienvenue dans le systeme TimeLog");
            System.out.println(
                    "Etes-vous un Admin ou un Employe? (Tapez 'admin' ou 'employe' pour continuer, 'exit' pour quitter)");
            String userType = scanner.nextLine().toLowerCase();

            if (userType.equals("exit")) {
                // Sauvegarde des données avant de quitter
                sauvegarderDonnees(admin);
                break;
            }

            if (userType.equals("admin")) {
                handleAdminActions(scanner, admin);
            } else if (userType.equals("employe")) {
                handleEmployeeActions(scanner, admin);
            } else {
                System.out.println("Option invalide, veuillez reessayer.");
            }
        }

        scanner.close();
    }

    private static void handleAdminActions(Scanner scanner, Admin admin) {
        while (true) {
            System.out.println("\nMenu Admin:");
            System.out.println("1. Afficher le rapport d'etat de chaque projet");
            System.out.println("2. Afficher le rapport d'etat global");
            System.out.println("3. Ajouter un projet");
            System.out.println("4. Supprimer un projet");
            System.out.println("5. Generer un talon de paie pour un employe");
            System.out.println("6. Modifier un employe");
            System.out.println("7. Assigner un employe a un projet");
            System.out.println("8. Ajouter un employe");
            System.out.println("9. Afficher tous les employes");
            System.out.println("10. Afficher tous les projets");
            System.out.println("13. Generer les totaux des salaires");
            System.out.println("14. Modifier le NPE");
            System.out.println("11. Sauvegarder et quitter");
            System.out.println("12. Retour au menu principal");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    for (Projet proj : admin.getProjets()) {
                        proj.genererRapportEtat(admin);
                    }
                    break;
                case 2:
                    admin.genererRapportEtatGlobal();
                    break;
                case 3:
                    System.out.println("Entrez le nom du projet:");
                    String nomProjet = scanner.nextLine();
                    System.out.println("Entrez la date de debut (yyyy-MM-dd):");
                    Date dateDebut = DateTypeAdapter.parseDate(scanner.nextLine());
                    System.out.println("Entrez la date de fin (yyyy-MM-dd):");
                    Date dateFin = DateTypeAdapter.parseDate(scanner.nextLine());
                    System.out.println("Entrez les heures prevues:");
                    int heuresPrevues = Integer.parseInt(scanner.nextLine());
                    Projet projetAjout = new Projet(admin.getProjets().size() + 1, nomProjet, dateDebut, dateFin,
                            heuresPrevues);

                    projetAjout.ajouterDiscipline(new Discipline("Design"));
                    projetAjout.ajouterDiscipline(new Discipline("Developpement"));
                    projetAjout.ajouterDiscipline(new Discipline("Test"));
                    projetAjout.ajouterDiscipline(new Discipline("Documentation"));
                    projetAjout.ajouterDiscipline(new Discipline("Deploiement"));

                    admin.ajouterProjet(projetAjout);
                    sauvegarderDonnees(admin);
                    break;
                case 4:
                    System.out.println("Entrez l'ID du projet a supprimer:");
                    int projetIdSuppr = Integer.parseInt(scanner.nextLine());
                    Projet projetSuppr = admin.getProjetById(projetIdSuppr);
                    if (projetSuppr != null) {
                        admin.supprimerProjet(projetSuppr);
                        sauvegarderDonnees(admin);
                    } else {
                        System.out.println("Projet non trouve.");
                    }
                    break;
                case 5:
                    System.out.println("Entrez l'ID de l'employe pour generer le talon de paie:");
                    int employeIdTalon = Integer.parseInt(scanner.nextLine());
                    Employe employeTalon = admin.getEmployeById(employeIdTalon);
                    if (employeTalon != null) {
                        admin.genererTalonPaieGlobal(employeTalon, scanner);
                    } else {
                        System.out.println("Employe non trouve.");
                    }
                    break;
                case 6:
                    System.out.println("Entrez l'ID de l'employe à modifier:");
                    int employeIdModif = Integer.parseInt(scanner.nextLine());
                    System.out.println("Entrez le nouveau nom de l'employe:");
                    String nouveauNom = scanner.nextLine();
                    System.out.println("Entrez le nouvel ID de l'employe:");
                    int nouvelId = Integer.parseInt(scanner.nextLine());
                    admin.modifierEmploye(employeIdModif, nouveauNom, nouvelId);
                    sauvegarderDonnees(admin);
                    break;
                case 7:
                    System.out.println("Entrez l'ID de l'employe:");
                    int employeIdAssign = Integer.parseInt(scanner.nextLine());
                    Employe employeAssign = admin.getEmployeById(employeIdAssign);
                    if (employeAssign == null) {
                        System.out.println("Employe non trouve.");
                        break;
                    }
                    System.out.println("Entrez l'ID du projet:");
                    int projetIdAssign = Integer.parseInt(scanner.nextLine());
                    Projet projetAssign = admin.getProjetById(projetIdAssign);
                    if (projetAssign == null) {
                        System.out.println("Projet non trouve.");
                        break;
                    }
                    admin.assignerEmploye(employeAssign, projetAssign);
                    employeAssign.ajouterProjet(projetAssign); // Ajout du projet à l'employé
                    sauvegarderDonnees(admin);
                    break;
                case 8:
                    System.out.println("Entrez le nom de l'employe:");
                    String nomEmploye = scanner.nextLine();
                    System.out.println("Entrez la date d'embauche (yyyy-MM-dd):");
                    Date dateEmbauche = DateTypeAdapter.parseDate(scanner.nextLine());
                    System.out.println("Entrez le numero d'assurance:");
                    String numAssurance = scanner.nextLine();
                    System.out.println("Entrez le poste:");
                    String poste = scanner.nextLine();
                    System.out.println("Entrez le taux horaire de base:");
                    float tauxHoraireBase = Float.parseFloat(scanner.nextLine());
                    System.out.println("Entrez le taux d'heure supplementaire:");
                    float tauxHeureSupplementaire = Float.parseFloat(scanner.nextLine());
                    Employe employeAjoute = new Employe(admin.getEmployes().size() + 1, nomEmploye, dateEmbauche,
                            numAssurance, poste, tauxHoraireBase, tauxHeureSupplementaire);
                    admin.ajouterEmploye(employeAjoute);
                    sauvegarderDonnees(admin);
                    break;
                case 9:
                    System.out.println("Liste des employes:");
                    for (Employe emp : admin.getEmployes()) {
                        System.out.println(emp);
                    }
                    break;
                case 10:
                    System.out.println("Liste des projets:");
                    for (Projet proj : admin.getProjets()) {
                        System.out.println(proj.getId() + ": " + proj.getNom());
                    }
                    break;
                case 11:
                    sauvegarderDonnees(admin);
                    System.exit(0);
                    break;
                case 12:
                    return;
                case 13:
                    admin.genererRapportTotauxSalairesDernieres26Periodes();
                    break;
                case 14:
                    System.out.println("Entrez la nouvelle valeur de NPE:");
                    int nouveauNPE = Integer.parseInt(scanner.nextLine());
                    admin.setNPE(nouveauNPE);
                    sauvegarderDonnees(admin);
                    break;
                default:
                    System.out.println("Option invalide, veuillez reessayer.");
            }
        }
    }

    private static void handleEmployeeActions(Scanner scanner, Admin admin) {
        System.out.println("Entrez votre ID:");
        int employeId = Integer.parseInt(scanner.nextLine());
        Employe employe = admin.getEmployeById(employeId);

        if (employe == null) {
            System.out.println("Employe non trouve.");
            return;
        }

        while (true) {
            System.out.println("\nMenu Employe:");
            System.out.println("1. Debuter une activite");
            System.out.println("2. Terminer une activite");
            System.out.println("3. Generer un rapport d'heures travaillees");
            System.out.println("4. Consulter le talon de paie");
            System.out.println("5. Retour au menu principal");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    System.out.println("Entrez l'ID du projet:");
                    int projetId = Integer.parseInt(scanner.nextLine());
                    Projet projet = employe.getProjets().stream()
                            .filter(p -> p.getId() == projetId)
                            .findFirst()
                            .orElse(null);
                    if (projet != null) {
                        employe.debuterActivite(projet, scanner);
                        sauvegarderDonnees(admin);
                    } else {
                        System.out.println("Projet non trouve ou vous n'etes pas assigne a ce projet.");
                    }
                    break;
                case 2:
                    System.out.println("Entrez l'ID du projet:");
                    projetId = Integer.parseInt(scanner.nextLine());
                    projet = employe.getProjets().stream()
                            .filter(p -> p.getId() == projetId)
                            .findFirst()
                            .orElse(null);
                    if (projet != null) {
                        employe.terminerActivite(projet, scanner);
                        sauvegarderDonnees(admin);
                    } else {
                        System.out.println("Projet non trouve ou vous n'etes pas assigne à ce projet.");
                    }
                    break;
                case 3:
                    employe.genererRapportHeures();
                    break;
                case 4:
                    employe.consulterTalonPaiePourPeriode(scanner);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Option invalide, veuillez reessayer.");
            }
        }
    }

    private static void sauvegarderDonnees(Admin admin) {
        try {
            admin.sauvegarderDonnees("time_log_data.json");
        } catch (IOException e) {
            System.out.println("Erreur lors de la sauvegarde des donnees: " + e.getMessage());
        }
    }
}

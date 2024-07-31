package NyamiMakouet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Admin implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nomUtilisateur;
    private String motDePasse;
    private List<Projet> projets;
    private List<Employe> employes;
    private int NPE = 3; // Nombre de projets par employé par défaut

    public Admin(String nomUtilisateur, String motDePasse) {
        this.nomUtilisateur = nomUtilisateur;
        this.motDePasse = motDePasse;
        this.projets = new ArrayList<>();
        this.employes = new ArrayList<>();
    }

    // Assurez-vous que les listes sont initialisées après le chargement des données
    public void initialiserListes() {
        if (projets == null) {
            projets = new ArrayList<>();
        }
        if (employes == null) {
            employes = new ArrayList<>();
        }
    }

    public void assignerEmploye(Employe employe, Projet projet) {
        if (employe != null && projet != null) {
            if (employe.getProjets().size() >= NPE) {
                System.out.println(
                        "Impossible d'assigner l'employe " + employe.getNom() + " à plus de " + NPE + " projets.");
                return;
            }
            employe.ajouterProjet(projet);
            projet.ajouterEmploye(employe);
            System.out.println("Employe " + employe.getNom() + " a ete assigne au projet " + projet.getNom());
        }
    }

    public void changerMotDePasse(String nouveauMotDePasse) {
        this.motDePasse = nouveauMotDePasse;
    }

    public void setNPE(int NPE) {
        this.NPE = NPE;
        System.out.println("NPE mis à jour à " + NPE + ".");
    }

    public void ajouterProjet(Projet projet) {
        projets.add(projet);
    }

    public void supprimerProjet(Projet projet) {
        projets.remove(projet);
    }

    public void ajouterEmploye(Employe employe) {
        employes.add(employe);
        System.out.println("Employe ajouter avec succes");

    }

    public void modifierEmploye(int id, String nouveauNom, int nouvelId) {
        Employe employe = getEmployeById(id);
        if (employe != null) {
            employe.setNom(nouveauNom);
            employe.setId(nouvelId);
        } else {
            System.out.println("Employé non trouvé.");
        }
    }

    public void genererTalonPaieGlobal(Employe employe, Scanner scanner) {
        employe.consulterTalonPaiePourPeriode(scanner);
    }

    public void genererRapportEtatGlobal() {
        System.out.println("Rapport d'état global:");
        for (Projet projet : projets) {
            projet.genererRapportEtat(this);
        }
    }

    public void genererRapportValeurHeures(Employe employe, Date dateDebut) {
        System.out.println(
                "Rapport des valeurs des heures travaillées pour " + employe.getNom() + " depuis " + dateDebut + ":");
        long totalMinutes = 0;

        for (Activite activite : employe.getHistorique()) {
            Date debut = activite.getDateDebut();
            if (debut.after(dateDebut)) {
                Date fin = activite.getDateFin();
                if (fin != null) {
                    long duree = fin.getTime() - debut.getTime();
                    long dureeEnMinutes = duree / (1000 * 60);
                    totalMinutes += dureeEnMinutes;
                    System.out.println("Projet: " + activite.getProjetId() +
                            ", Discipline: " + activite.getDiscipline() +
                            ", Début: " + debut +
                            ", Fin: " + fin +
                            ", Durée: " + dureeEnMinutes + " minutes");
                }
            }
        }

        long heures = totalMinutes / 60;
        long minutes = totalMinutes % 60;
        float valeur = (totalMinutes / 60.0f) * employe.getTauxHoraireBase();
        System.out.println("Temps total travaillé: " + heures + " heures et " + minutes);
        System.out.println("Valeur totale des heures: " + valeur + " USD");
    }

    public void genererRapportTotauxSalairesDernieres26Periodes() {
        System.out.println("Rapport des salaires pour les 26 dernières périodes de paie:");
        for (Employe employe : employes) {
            System.out.println("Employé: " + employe.getNom());
            employe.consulterTalonPaiePourPeriode(new Scanner(System.in));
        }
    }

    public String getNomUtilisateur() {
        return nomUtilisateur;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public List<Projet> getProjets() {
        return projets;
    }

    public List<Employe> getEmployes() {
        return employes;
    }

    public Employe getEmployeById(int id) {
        for (Employe employe : employes) {
            if (employe.getId() == id) {
                return employe;
            }
        }
        return null;
    }

    public Projet getProjetById(int id) {
        for (Projet projet : projets) {
            if (projet.getId() == id) {
                return projet;
            }
        }
        return null;
    }

    // Sauvegarde des données en JSON
    public void sauvegarderDonnees(String fichier) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(Date.class, new DateTypeAdapter())
                .create();
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(fichier), "UTF-8")) {
            gson.toJson(this, writer);
        }
    }

    // Chargement des données depuis un fichier JSON avec validation
    public static Admin chargerDonnees(String fichier) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new DateTypeAdapter()).create();
        try (Reader reader = new InputStreamReader(new FileInputStream(fichier), "UTF-8")) {
            Admin admin = gson.fromJson(reader, Admin.class);
            if (admin == null) {
                throw new IOException("Données JSON nulles");
            }
            admin.initialiserListes(); // Assurez-vous que les listes sont initialisées
            return admin;
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement des données: " + e.getMessage());
            return new Admin("admin", "admin");
        }
    }
}

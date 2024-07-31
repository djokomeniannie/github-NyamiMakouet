package NyamiMakouet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Projet implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String nom;
    private Date dateDebut;
    private Date dateFin;
    private int heuresPrevues;
    private List<Discipline> disciplines;
    private List<Employe> employes;

    public Projet(int id, String nom, Date dateDebut, Date dateFin, int heuresPrevues) {
        this.id = id;
        this.nom = nom;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.heuresPrevues = heuresPrevues;
        this.disciplines = new ArrayList<>();
        this.employes = new ArrayList<>();
    }

    public void ajouterDiscipline(Discipline discipline) {
        disciplines.add(discipline);
    }

    public void ajouterEmploye(Employe employe) {
        employes.add(employe);
    }

    public List<Discipline> getDisciplines() {
        return disciplines;
    }

    public List<Employe> getEmployes() {
        return employes;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void genererRapportEtat(Admin admin) {
        System.out.println("Rapport d'état pour le projet " + nom + ":");
        for (Employe employe : getEmployes()) {
            System.out.println("Employé: " + employe.getNom());
            for (Activite activite : employe.getHistorique()) {
                if (activite.getProjetId() == id) {
                    System.out.println("  - Activité: " + activite.getDiscipline()
                            + " débutée à " + activite.getDateDebut()
                            + " et terminée à " + activite.getDateFin());
                }
            }
        }
    }

    public List<Discipline> obtenirDisciplines() {
        return getDisciplines();
    }

    @Override
    public String toString() {
        return id + ": " + nom;
    }
}

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

    public Projet(int id, String nom, Date dateDebut, Date dateFin, int heuresPrevues, List<Discipline> disciplines) {
        this.id = id;
        this.nom = nom;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.heuresPrevues = heuresPrevues;
        this.disciplines = disciplines;
        this.employes = new ArrayList<>();
    }

    // Getters et setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public int getHeuresPrevues() {
        return heuresPrevues;
    }

    public void setHeuresPrevues(int heuresPrevues) {
        this.heuresPrevues = heuresPrevues;
    }

    public List<Discipline> getDisciplines() {
        return disciplines;
    }

    public void setDisciplines(List<Discipline> disciplines) {
        this.disciplines = disciplines;
    }

    public List<Employe> getEmployes() {
        return employes;
    }

    public void setEmployes(List<Employe> employes) {
        this.employes = employes;
    }

    public void ajouterDiscipline(Discipline discipline) {
        disciplines.add(discipline);
    }

    public void ajouterEmploye(Employe employe) {
        employes.add(employe);
    }

    public List<Discipline> obtenirDisciplines() {
        return disciplines;
    }

    public void genererRapportEtat(Admin admin) {
        System.out.println("Rapport d'etat du projet " + nom + ":");
        for (Employe employe : employes) {
            System.out.println("Employe: " + employe.getNom());
            employe.genererRapportHeures();
        }
    }

    @Override
    public String toString() {
        return "Projet{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", heuresPrevues=" + heuresPrevues +
                ", disciplines=" + disciplines +
                ", employes=" + employes +
                '}';
    }
}

package NyamiMakouet;

import java.io.Serializable;
import java.util.Date;

public class Activite implements Serializable {
    private static final long serialVersionUID = 1L;
    private int employeId;
    private int projetId;
    private String discipline;
    private Date dateDebut;
    private Date dateFin;

    public Activite(int employeId, int projetId, String discipline, Date dateDebut) {
        this.employeId = employeId;
        this.projetId = projetId;
        this.discipline = discipline;
        this.dateDebut = dateDebut;
        this.dateFin = null;
    }

    // Ajouter le constructeur avec la date de fin
    public Activite(int employeId, int projetId, String discipline, Date dateDebut, Date dateFin) {
        this.employeId = employeId;
        this.projetId = projetId;
        this.discipline = discipline;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    // Getters et setters

    public int getEmployeId() {
        return employeId;
    }

    public void setEmployeId(int employeId) {
        this.employeId = employeId;
    }

    public int getProjetId() {
        return projetId;
    }

    public void setProjetId(int projetId) {
        this.projetId = projetId;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
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

    @Override
    public String toString() {
        return "Activite{" +
                "employeId=" + employeId +
                ", projetId=" + projetId +
                ", discipline='" + discipline + '\'' +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                '}';
    }
}

package NyamiMakouet;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Employe implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String nom;
    private Date dateEmbauche;
    private String numAssurance;
    private String poste;
    private float tauxHoraireBase;
    private float tauxHeureSupplementaire;
    private List<Activite> historique;
    private List<Projet> projets;

    public Employe(int id, String nom, Date dateEmbauche, String numAssurance, String poste, float tauxHoraireBase,
            float tauxHeureSupplementaire) {
        this.id = id;
        this.nom = nom;
        this.dateEmbauche = dateEmbauche;
        this.numAssurance = numAssurance;
        this.poste = poste;
        this.tauxHoraireBase = tauxHoraireBase;
        this.tauxHeureSupplementaire = tauxHeureSupplementaire;
        this.historique = new ArrayList<>();
        this.projets = new ArrayList<>();
    }

    public void debuterActivite(Projet projet, Scanner scanner) {
        if (!projets.contains(projet)) {
            System.out.println("Vous n'etes pas affecte a ce projet.");
            return;
        }

        System.out.println("Selectionnez la discipline:");
        List<Discipline> disciplines = projet.obtenirDisciplines();
        for (int i = 0; i < disciplines.size(); i++) {
            System.out.println((i + 1) + ". " + disciplines.get(i).getNom());
        }
        int choixDiscipline = Integer.parseInt(scanner.nextLine()) - 1;
        if (choixDiscipline < 0 || choixDiscipline >= disciplines.size()) {
            System.out.println("Discipline invalide.");
            return;
        }
        Discipline disciplineSelectionnee = disciplines.get(choixDiscipline);

        System.out.println("Confirmez-vous le debut de l'activite? (oui/non)");
        String confirmation = scanner.nextLine();
        if (!confirmation.equalsIgnoreCase("oui")) {
            System.out.println("Debut de l'activite annule.");
            return;
        }

        Date dateDebut = new Date();
        Activite activite = new Activite(this.id, projet.getId(), disciplineSelectionnee.getNom(), dateDebut);
        historique.add(activite);
        System.out.println("Activite debutee pour le projet " + projet.getNom() + " dans la discipline "
                + disciplineSelectionnee.getNom() + " a " + dateDebut);
    }

    public void terminerActivite(Projet projet, Scanner scanner) {
        if (!projets.contains(projet)) {
            System.out.println("Vous n'etes pas affecte a ce projet.");
            return;
        }

        System.out.println("Signalez l'arret de l'activite pour le projet " + projet.getNom());

        System.out.println("Confirmez-vous la fin de l'activite? (oui/non)");
        String confirmation = scanner.nextLine();
        if (!confirmation.equalsIgnoreCase("oui")) {
            System.out.println("Fin de l'activite annulee.");
            return;
        }

        boolean activiteTrouvee = false;
        Date dateFin = new Date();
        for (Activite activite : historique) {
            if (activite.getProjetId() == projet.getId() && activite.getDateFin() == null) {
                activite.setDateFin(dateFin);

                long duree = dateFin.getTime() - activite.getDateDebut().getTime();
                long dureeEnMinutes = duree / (1000 * 60);
                System.out.println("Activite terminee pour le projet " + projet.getNom() + " a " + dateFin);
                System.out.println("Temps total de l'activite: " + dureeEnMinutes + " minutes");
                activiteTrouvee = true;
                break;
            }
        }

        if (!activiteTrouvee) {
            System.out.println("Vous souhaitez terminer une activite qui n'a pas commence.");
        }
    }

    public void genererRapportHeures() {
        System.out.println("Rapport des heures travaillees pour " + nom + ":");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        long totalMinutes = 0;

        for (Activite activite : historique) {
            Date dateDebut = activite.getDateDebut();
            Date dateFin = activite.getDateFin();
            if (dateFin != null) {
                long duree = dateFin.getTime() - dateDebut.getTime();
                long dureeEnMinutes = duree / (1000 * 60);
                totalMinutes += dureeEnMinutes;
                System.out.println("Projet: " + activite.getProjetId() +
                        ", Discipline: " + activite.getDiscipline() +
                        ", Debut: " + sdf.format(dateDebut) +
                        ", Fin: " + sdf.format(dateFin) +
                        ", Duree: " + dureeEnMinutes + " minutes");
            } else {
                System.out.println("Projet: " + activite.getProjetId() +
                        ", Discipline: " + activite.getDiscipline() +
                        ", Debut: " + sdf.format(dateDebut) +
                        ", Fin: En cours");
            }
        }

        long heures = totalMinutes / 60;
        long minutes = totalMinutes % 60;
        System.out.println("Temps total travaille: " + heures + " heures et " + minutes + " minutes");
    }

    public void consulterTalonPaiePourPeriode(Scanner scanner) {
        System.out.println("Choisissez une periode de paie:");
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        List<Date> startDates = new ArrayList<>();
        List<Date> endDates = new ArrayList<>();

        // Générer les 26 dernières périodes de paie sur deux semaines
        for (int i = 0; i < 26; i++) {
            cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY); // Fin de la période (samedi)
            Date endDate = cal.getTime();
            cal.add(Calendar.DAY_OF_YEAR, -13); // Début de la période (dimanche deux semaines avant)
            Date startDate = cal.getTime();
            cal.add(Calendar.DAY_OF_YEAR, -1); // Préparer pour la prochaine période

            startDates.add(startDate);
            endDates.add(endDate);

            System.out.println((i + 1) + ". " + sdf.format(startDate) + " - " + sdf.format(endDate));
        }

        int choix = Integer.parseInt(scanner.nextLine()) - 1;
        if (choix < 0 || choix >= 26) {
            System.out.println("Periode invalide.");
            return;
        }

        Date periodeDebut = startDates.get(choix);
        Date periodeFin = endDates.get(choix);

        long totalMinutes = 0;
        long heuresSupplementairesMinutes = 0;

        for (Activite activite : historique) {
            Date dateDebut = activite.getDateDebut();
            Date dateFin = activite.getDateFin();
            if (dateFin != null && !dateFin.before(periodeDebut) && !dateFin.after(periodeFin)) {
                long duree = dateFin.getTime() - dateDebut.getTime();
                long dureeEnMinutes = duree / (1000 * 60);
                totalMinutes += dureeEnMinutes;
            }
        }

        long heuresNormalesMinutes = Math.min(totalMinutes, 80 * 60); // 80 heures pour deux semaines
        heuresSupplementairesMinutes = Math.max(0, totalMinutes - heuresNormalesMinutes);

        float heuresNormales = heuresNormalesMinutes / 60.0f;
        float heuresSupplementaires = heuresSupplementairesMinutes / 60.0f;

        float paieNormale = heuresNormales * tauxHoraireBase;
        float paieSupplementaire = heuresSupplementaires * tauxHeureSupplementaire;
        float paieTotale = paieNormale + paieSupplementaire;
        float paieNette = paieTotale * 0.6f;

        System.out.println(
                "Talon de paie pour la periode " + sdf.format(periodeDebut) + " - " + sdf.format(periodeFin) + ":");
        System.out.println("Heures regulieres travaillees: " + heuresNormales + " heures");
        System.out.println("Heures supplementaires travaillees: " + heuresSupplementaires + " heures");
        System.out.println("Salaire regulier: " + paieNormale + " dollars");
        System.out.println("Salaire des heures supplementaires: " + paieSupplementaire + " dollars");
        System.out.println("Salaire brut: " + paieTotale + " dollars");
        System.out.println("Salaire net: " + paieNette + " dollars");

    }
    // Getters et setters générés automatiquement

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

    public Date getDateEmbauche() {
        return dateEmbauche;
    }

    public void setDateEmbauche(Date dateEmbauche) {
        this.dateEmbauche = dateEmbauche;
    }

    public String getNumAssurance() {
        return numAssurance;
    }

    public void setNumAssurance(String numAssurance) {
        this.numAssurance = numAssurance;
    }

    public String getPoste() {
        return poste;
    }

    public void setPoste(String poste) {
        this.poste = poste;
    }

    public float getTauxHoraireBase() {
        return tauxHoraireBase;
    }

    public void setTauxHoraireBase(float tauxHoraireBase) {
        this.tauxHoraireBase = tauxHoraireBase;
    }

    public float getTauxHeureSupplementaire() {
        return tauxHeureSupplementaire;
    }

    public void setTauxHeureSupplementaire(float tauxHeureSupplementaire) {
        this.tauxHeureSupplementaire = tauxHeureSupplementaire;
    }

    public List<Activite> getHistorique() {
        return historique;
    }

    public void setHistorique(List<Activite> historique) {
        this.historique = historique;
    }

    public List<Projet> getProjets() {
        return projets;
    }

    public void ajouterProjet(Projet projet) {
        if (projets == null) {
            projets = new ArrayList<>();
        }
        this.projets.add(projet);
    }

    @Override
    public String toString() {
        return id + ": " + nom;
    }
}

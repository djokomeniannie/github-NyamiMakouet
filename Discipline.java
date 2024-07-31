package NyamiMakouet;

import java.io.Serializable;

public class Discipline implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nom;

    public Discipline(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public String toString() {
        return "Discipline{" +
                "nom='" + nom + '\'' +
                '}';
    }
}

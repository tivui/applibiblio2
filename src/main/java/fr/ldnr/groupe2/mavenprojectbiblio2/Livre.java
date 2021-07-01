package fr.ldnr.groupe2.mavenprojectbiblio2;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author stag
 */

@Entity 
@Table
public class Livre implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private int idLivre;
    private String titre;
    private String nomAuteur ;
    private String prenomAuteur;
    private String editeur;
    private String annee;
    private boolean estEmprunte;

    public Livre() {
    }

    public Livre(String titre, String nomAuteur, String prenomAuteur, String annee, String editeur) {
        this.titre = titre;
        this.nomAuteur = nomAuteur;
        this.prenomAuteur = prenomAuteur;
        this.annee = annee;
        this.editeur = editeur;
        this.estEmprunte = false;
    }

    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    public int getIdLivre() {
        return idLivre;
    }

    public void setIdLivre(int idLivre) {
        this.idLivre = idLivre;
    }

    @Column(nullable = false)
    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    @Column(nullable = false)
    public String getNomAuteur() {
        return nomAuteur;
    }

    public void setNomAuteur(String nomAuteur) {
        this.nomAuteur = nomAuteur;
    }

    @Column(nullable = false)
    public String getPrenomAuteur() {
        return prenomAuteur;
    }

    public void setPrenomAuteur(String prenomAuteur) {
        this.prenomAuteur = prenomAuteur;
    }
    
        @Column(nullable = false)
    public String getAnnee() {
        return annee;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    @Column(nullable = false)
    public String getEditeur() {
        return editeur;
    }

    public void setEditeur(String editeur) {
        this.editeur = editeur;
    }

    public boolean isEstEmprunte() {
        return estEmprunte;
    }

    public void setEstEmprunte(boolean estEmprunte) {
        this.estEmprunte = estEmprunte;
    }
    
    
    
    
}

package fr.ldnr.groupe2.mavenprojectbiblio2;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author stag
 */
@Entity
@Table
public class Emprunt implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private int idEmprunt;
    private Date date;
    private String nomEmprunteur;
    private Livre livre ;

    public Emprunt() {
    }

    public Emprunt(Date date, String nomEmprunteur, Livre livre) {
        this.date = date;
        this.nomEmprunteur = nomEmprunteur;
        this.livre = livre;
        livre.setEstEmprunte(true);
    }

    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getIdEmprunt() {
        return idEmprunt;
    }

    public void setIdEmprunt(int idEmprunt) {
        this.idEmprunt = idEmprunt;
    }

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Column(nullable = false)
    public String getNomEmprunteur() {
        return nomEmprunteur;
    }

    public void setNomEmprunteur(String nomEmprunteur) {
        this.nomEmprunteur = nomEmprunteur;
    }

    @ManyToOne (cascade = CascadeType.ALL)
    public Livre getLivre() {
        return livre;
    }

    public void setLivre(Livre livre) {
        this.livre = livre;
    }

    
    
    
    
}


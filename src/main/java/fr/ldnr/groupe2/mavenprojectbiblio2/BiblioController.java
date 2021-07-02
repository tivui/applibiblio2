package fr.ldnr.groupe2.mavenprojectbiblio2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author stag
 */
@RestController
public class BiblioController {

    public static final Logger logger = LoggerFactory.getLogger(BiblioController.class);

//******************Branchement à la sessionFactory pour gérer les sessions Hibernate****************
    //branchement automatique à la session Hibernate
    @Autowired
    public SessionFactory sessionFactory;

//************************Get mapping pour la page emprunt*******************************************
    //Contrôleur qui relie la page html emprunt.html à l'url /emprunt
    @Controller
    public class HtmlPageController {

        @GetMapping("/emprunt")
        public String getTestPage() {
            return "emprunt.html";
        }
    }


//*************************Liste des request mapping, à chaque url, une fonction*******************************
    //Affiche tous les livres de la bdd 
    @RequestMapping("/bdd/livres")
    public List<Livre> livres() {
        List<Livre> list;
        try (Session session = sessionFactory.openSession()) {
            String livresHQL = "FROM Livre";
            list = session.createQuery(livresHQL).list();
        }
        return list;
    }
    //Affiche tous les livres disponibles triés par titre
    @RequestMapping("/bdd/livrespartitre")
    public List<Livre> livresParTitre() {
        List<Livre> list;
        try (Session session = sessionFactory.openSession()) {
            String livresParTitreHQL = "FROM Livre  WHERE estEmprunte=false ORDER BY titre ";
            list = session.createQuery(livresParTitreHQL).list();
        }
        return list;
    }
    //Affiche tous les emprunts
    @RequestMapping("/bdd/emprunts")
    public List<Emprunt> emprunts() {
        List<Emprunt> list;
        try (Session session = sessionFactory.openSession()) {
            String empruntsHQL = "FROM Emprunt";
            list = session.createQuery(empruntsHQL).list();
        }
        return list;
    }
    //Affiche tous les livres empruntés ordonnés par date
    @RequestMapping("/bdd/listEmprunts")
    public List<Emprunt> listEmprunts() {
        List<Emprunt> listEmprunt;
        try (Session session = sessionFactory.openSession()) {
            String ListeDesEmprunts = "From Emprunt WHERE enCours=true ORDER BY date";
            listEmprunt = session.createQuery(ListeDesEmprunts).getResultList();
        }
        return listEmprunt;
    }

    
    //crée un livre à partir des paramètres titre, nomAuteur, prenomAuteur, annee, editeur
    @RequestMapping("/creation/livre/{titre}/{nomAuteur}/{prenomAuteur}/{annee}/{editeur}")
    public String creationLivre(@PathVariable String titre, @PathVariable String nomAuteur,
            @PathVariable String prenomAuteur, @PathVariable String annee, @PathVariable String editeur) throws ParseException {
        //Création d'un nouvel livre
        try (Session session = sessionFactory.openSession()) {
            //Création d'un nouvel livre
            Livre nouveauLivre = new Livre(titre, nomAuteur, prenomAuteur, annee, editeur);
            logger.info("Livre créé: " + ", Titre : " + titre + ", Auteur : " + nomAuteur + " " + prenomAuteur + " date de " + annee + " edité par " + editeur);
            //Ajout du livre dans la base
            session.save(nouveauLivre);
        }
        logger.info("Nouveau livre ajouté à la base");
        return "ok";
    }
    //crée un emprunt à partir de l'id et de la date du jour
    @RequestMapping("/creation/emprunt/{idLivre}/{nomEmprunteur}/{date}")
    public String creationEmprunt(@PathVariable String idLivre, @PathVariable String nomEmprunteur, @PathVariable String date) throws ParseException {
        //Récupération de l'objet livre dans la table Livre grâce à son id (qu'il faut caster en int)
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        int idLivreInt = Integer.parseInt(idLivre);//cast de l'id de String à Int
        Livre livreEmprunt = session.get(Livre.class, idLivreInt);
        //Cast de la date String récupéré en objet date
        Date dateEmprunt = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        //Création d'un nouvel emprunt
        Emprunt nouvelEmprunt = new Emprunt(dateEmprunt, nomEmprunteur, livreEmprunt);
        logger.info("Emprunt créé: id du livre n°" + idLivre + ", Emprunteur : " + nomEmprunteur + " à la date du " + date);
        //Modification du statut du livre : estEmprunte passe de false à true
        nouvelEmprunt.getLivre().setEstEmprunte(true);
        //Ajout de l'emprunt dans la base
        session.save(nouvelEmprunt);
        tx.commit();
        logger.info("Nouvel Emprunt ajouté à la base, statut du livre associé :" + nouvelEmprunt.getLivre().isEstEmprunte());
        return idLivre;
    }
    //met à jour le statut d'un emprunt à partir de son id
    @RequestMapping("/miseajour/emprunt/{idEmprunt}/{today}")
    public String miseAjour(@PathVariable String idEmprunt, @PathVariable String today) throws ParseException {
        Emprunt emprunt;
        try ( //Récupération de l'emprunt
                Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            int idLivreInt = Integer.parseInt(idEmprunt);//cast de l'id de String à Int
            //Cast de la date String récupéré en objet date
            Date dateDuJour = new SimpleDateFormat("yyyy-MM-dd").parse(today);
            emprunt = session.get(Emprunt.class, idLivreInt);
            //Mise à jour de l'emprunt
            emprunt.setEnCours(false);
            emprunt.setDateDeRetour(dateDuJour);
            emprunt.getLivre().setEstEmprunte(false);
            //Ajout de l'emprunt mis à jour dans la base
            session.save(emprunt);
            tx.commit();
            //session.close();
        } //cast de l'id de String à Int
        logger.info("Emprunt n° :" + idEmprunt + " mis à jour avec succès!");
        logger.info("Valeur du booléen livre estEmprunte :" + emprunt.getLivre().isEstEmprunte());
        return "Statut de l'emprunt n° " + idEmprunt + " mis à jour";
    }
    

    //****TESTS****
    //Test pour générer des créations de livres et d'emprunts 
    @RequestMapping("/creation")
    @ResponseBody
    public String creation() {
        try ( //crée des livres et des emprunts de base pour les tests
                Session session = sessionFactory.openSession()) {
            Livre l1 = new Livre("A la lumière des étoiles", "Hardy", "Thomas", "2015", "CraneFord Editions");
            Livre l2 = new Livre("Villette", "Brontë", "Charlotte", "1932", "CraneFord Editions");
            Livre l3 = new Livre("Lady Suzans: les Watson sans dit-on ", "Austen", "Jane", "2000", "CraneFord Editions");
            Livre l4 = new Livre("L'éveil", "Chopin", "Kate", "1993", "CraneFord Editions");
            Emprunt e1 = new Emprunt(new GregorianCalendar(2021, 6, 29).getTime(), "Justin Bodinier", l4);
            Emprunt e2 = new Emprunt(new GregorianCalendar(2021, 6, 28).getTime(), "Chantal Keda", l3);
            Emprunt e3 = new Emprunt(new GregorianCalendar(2021, 6, 27).getTime(), "Thierry Vuillermet", l1);

            session.save(l1);
            session.save(l2);
            session.save(l3);
            session.save(l4);
            session.save(e1);
            session.save(e2);
            session.save(e3);
        }
        return "4 livres et 3 emprunts test créés.";
    }

}

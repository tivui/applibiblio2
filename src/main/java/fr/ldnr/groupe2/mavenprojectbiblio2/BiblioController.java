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

    //branchement automatique à la session Hibernate
    @Autowired
    public SessionFactory sessionFactory;

    @RequestMapping("/creation")
    @ResponseBody
    public void creation() {
        //crée des livres et des emprunts de base pour les tests
        Session session = sessionFactory.openSession();
        Livre l1 = new Livre("2015", "A la lumière des étoiles", "Hardy", "Thomas", "CraneFord Editions");
        Livre l2 = new Livre("1932", "Villette", "Brontë", "Charlotte", "CraneFord Editions");
        Livre l3 = new Livre("2000", "Lady Suzans: les Watson sans dit-on ", "Austen", "Jane", "CraneFord Editions");
        Livre l4 = new Livre("1993", "L'éveil", "Chopin", "Kate", "CraneFord Editions");
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
        session.close();
    }

    //Contrôleur qui affiche la liste de tous les livres de la bdd 
    @RequestMapping("/bdd/livres")
    public List<Livre> livres() {
        Session session = sessionFactory.openSession();
        String livresHQL = "FROM Livre";
        List<Livre> list = session.createQuery(livresHQL).list();
        session.close();
        return list;
    }

    //Contrôleur qui affiche la liste de tous les livres de la bdd triés par titre
    @RequestMapping("/bdd/livrespartitre")
    public List<Livre> livresParTitre() {
        Session session = sessionFactory.openSession();
        String livresParTitreHQL = "FROM Livre ORDER BY titre";
        List<Livre> list = session.createQuery(livresParTitreHQL).list();
        session.close();
        return list;
    }

    //Contrôleur qui affiche la liste de tous les emprunts dans la base
    @RequestMapping("/bdd/emprunts")
    public List<Emprunt> emprunts() {
        Session session = sessionFactory.openSession();
        String empruntsHQL = "FROM Emprunt";
        List<Emprunt> list = session.createQuery(empruntsHQL).list();
        session.close();
        return list;
    }

    //Contrôleur qui relie la page html emprunt.html avec l'url /emprunt
    @Controller
    public class HtmlPageController {

        @GetMapping("/emprunt")
        public String getTestPage() {
            return "emprunt.html";
        }
    }

    //Contrôleur qui crée un emprunt à partir de l'id d'un livre
    @RequestMapping("/creation/emprunt/{idLivre}/{nomEmprunteur}/{date}")
    public String creationEmprunt(@PathVariable String idLivre, @PathVariable String nomEmprunteur, @PathVariable String date) throws ParseException {
        //Récupération de l'objet livre dans la table Livre grâce à son id (qu'il faut caster en int)
        Session session = sessionFactory.openSession();
        int idLivreInt = Integer.parseInt(idLivre);//cast de l'id de String à Int
        Livre livreEmprunt = session.load(Livre.class, idLivreInt);
        //Cast de la date String récupéré en objet date
        Date dateEmprunt = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        //Création d'un nouvel emprunt
        Emprunt nouvelEmprunt = new Emprunt(dateEmprunt, nomEmprunteur, livreEmprunt);
        logger.info("Emprunt créé: id du livre n°" + idLivre + ", Emprunteur : " + nomEmprunteur + " à la date du " + date);
        //Ajout de l'emprunt dans la base
        session.save(nouvelEmprunt);
        session.close();
        logger.info("Nouvel Emprunt ajouté à la base");
        return idLivre;
    }

    //Contrôleur qui met à jour le statut d'un emprunt à partir de son id
    @RequestMapping("/miseajour/emprunt/{idEmprunt}")
    public String miseAjour(@PathVariable String idEmprunt) throws ParseException {
        Emprunt emprunt;
        try ( //Récupération de l'emprunt
                Session session = sessionFactory.openSession()) {
            int idLivreInt = Integer.parseInt(idEmprunt);//cast de l'id de String à Int
            emprunt = session.load(Emprunt.class, idLivreInt);
            //Mise à jour de l'emprunt
            emprunt.setEstEmprunte(false);
            //Ajout de l'emprunt mis à jour dans la base
            session.save(emprunt);
            session.close();
        } //cast de l'id de String à Int
        logger.info("Emprunt n° :" + idEmprunt + " mis à jour avec succès!");
        logger.info("Valeur du booléen estEmprunte :" + emprunt.isEstEmprunte());
        return "Statut de l'emprunt n° " + idEmprunt + " mis à jour";
    }

    //Contrôleur qui affiche la liste de tous les emprunts dans la base
    @RequestMapping("/coucou/{titre}")
    public String coucou(@PathVariable String titre) {
        logger.info("coucou OK :" + titre);
        return "coucou";
    }

    @RequestMapping("/creation/livre/{titre}/{nomAuteur}/{prenomAuteur}/{annee}/{editeur}")
    public String creationLivre(@PathVariable String titre, @PathVariable String nomAuteur,
            @PathVariable String prenomAuteur, @PathVariable String annee, @PathVariable String editeur) throws ParseException {
        Session session = sessionFactory.openSession();
        //Création d'un nouvel livre
        Livre nouveauLivre = new Livre(annee, titre, nomAuteur, prenomAuteur, editeur);
        logger.info("Livre créé: " + ", Titre : " + titre + ", Auteur : " + nomAuteur + " " + prenomAuteur + " date de " + annee + " edité par " + editeur);
        //Ajout du livre dans la base
        session.save(nouveauLivre);
        session.close();
        logger.info("Nouveau livre ajouté à la base");
        return "ok";
    }

    //Contrôleur qui permet d'afficher la liste des emprunts contenus dans la base triés par date 
    //Emprunts en cours
    @RequestMapping("/bdd/listEmprunts")
    public List<Emprunt> listEmprunts() {
        Session session = sessionFactory.openSession();
        String ListeDesEmprunts = "From Emprunt ORDER BY date";
        List<Emprunt> listEmprunt = session.createQuery(ListeDesEmprunts).getResultList();
        session.close();
        return listEmprunt;
    }

}

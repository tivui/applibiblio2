package fr.ldnr.groupe2.mavenprojectbiblio2;

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
        Session session = sessionFactory.openSession();
        Livre l1 = new Livre("2015", "A la lumière des étoiles", "Hardy", "Thomas", "CraneFord Editions");
        Livre l2 = new Livre("1932", "Villette", "Brontë", "Charlotte", "CraneFord Editions");
        Livre l3 = new Livre("2000", "Lady Suzans: les Watson sans dit-on ", "Austen", "Jane", "CraneFord Editions");
        Livre l4 = new Livre("1993", "L'éveil", "Chopin", "Kate", "CraneFord Editions");
        Emprunt e1 = new Emprunt(new GregorianCalendar(2021, 6, 29).getTime(), "Justin Bodinier", l4);
        Emprunt e2 = new Emprunt(new GregorianCalendar(2021, 6, 28).getTime(), "Chantal Keda", l3);
        Emprunt e3 = new Emprunt(new GregorianCalendar(2021, 6, 27).getTime(), "Thierry Vuillermet", l1);
               
        Transaction tx = session.beginTransaction(); //ouvre la transaction
        session.save(l1);
        session.save(l2);
        session.save(l3);
        session.save(l4);
        session.save(e1);
        session.save(e2);
        session.save(e3);
        tx.commit(); //ferme la transaction
    }
    
    @RequestMapping("/bdd/livres")
	public List<Livre> dirigeable() {
		Session session = sessionFactory.openSession();
		String livresHQL = "FROM Livre";
		List<Livre> list = session.createQuery(livresHQL).list();
		session.close();
		return list;
	}
        
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

}
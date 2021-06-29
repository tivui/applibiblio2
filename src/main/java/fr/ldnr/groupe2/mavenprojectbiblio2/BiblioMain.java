package fr.ldnr.groupe2.mavenprojectbiblio2;

import java.util.GregorianCalendar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author stag
 */
@SpringBootApplication
public class BiblioMain {

	public static final Logger logger = LoggerFactory.getLogger(BiblioMain.class);

	public static void main(String[] args) {

		SpringApplication.run(BiblioMain.class, args);
		logger.info("Lancement de l'application Biblio !!!");

	}

}

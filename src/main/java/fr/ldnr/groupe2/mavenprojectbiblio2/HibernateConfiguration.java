package fr.ldnr.groupe2.mavenprojectbiblio2;

import java.util.Properties;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HibernateConfiguration {

    public static final Logger logger = LoggerFactory.getLogger(HibernateConfiguration.class);

    @Bean
    public SessionFactory sessionFactory() {

        Properties options = new Properties();
        options.put("hibernate.dialect", "org.sqlite.hibernate.dialect.SQLiteDialect");
        options.put("hibernate.hbm2ddl.auto", "create");
        options.put("hibernate.show_sql", "true");
        options.put("hibernate.connection.driver_class", "org.sqlite.JDBC");
        options.put("hibernate.connection.url", "jdbc:sqlite:biblio.sqlite");
        SessionFactory factory = new org.hibernate.cfg.Configuration().
                addProperties(options).addAnnotatedClass(Livre.class)
                .addAnnotatedClass(Emprunt.class)
                .buildSessionFactory();

        logger.info("SessionFactory créée");
        return factory;
    }
}

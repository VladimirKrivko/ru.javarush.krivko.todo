package ru.javarush.todolist.config;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Objects;
import java.util.Properties;

@Configuration
@PropertySource("classpath:database.properties")
@EnableTransactionManagement
@ComponentScan("ru.javarush.todolist")
public class DaoConfig {

    @Value("${database.driver}")
    private String dbDriver;
    @Value("${database.url}")
    private String dbUrl;
    @Value("${database.username}")
    private String dbUsername;
    @Value("${database.password}")
    private String dbPassword;
    @Value("${hibernate.dialect}")
    private String hibernateDialect;
    @Value("${hibernate.show_sql}")
    private String hibernateShowSql;
    @Value("${hibernate.hbm2ddl.auto}")
    private String hibernateHbm2ddlAuto;

    @Bean
    public LocalSessionFactoryBean sessionFactoryBean() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSourceBean());
        sessionFactory.setPackagesToScan("ru.javarush.todolist.entity");
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }

    @Bean
    public DataSource dataSourceBean() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(Objects.requireNonNull(dbDriver));
        dataSource.setJdbcUrl(dbUrl);
        dataSource.setUsername(dbUsername);
        dataSource.setPassword(dbPassword);

        int availableCoreProcessors = Runtime.getRuntime().availableProcessors();
        dataSource.setMaximumPoolSize(availableCoreProcessors < 2 ? availableCoreProcessors : availableCoreProcessors - 1);
        return dataSource;
    }

    public Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", hibernateDialect);
        properties.put("hibernate.show_sql", hibernateShowSql);
        properties.put("hibernate.hbm2ddl.auto", hibernateHbm2ddlAuto);
        return properties;
    }

    @Bean
    public PlatformTransactionManager transactionManagerBean(EntityManagerFactory factory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(factory);
        return transactionManager;
    }
}

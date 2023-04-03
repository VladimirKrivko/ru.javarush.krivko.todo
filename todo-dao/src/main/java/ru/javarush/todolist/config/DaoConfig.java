package ru.javarush.todolist.config;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
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
    private final Environment environment;

    public DaoConfig(Environment environment) {
        this.environment = environment;
    }

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
        dataSource.setDriverClassName(Objects.requireNonNull(environment.getProperty("database.driver")));
        dataSource.setJdbcUrl(environment.getProperty("database.url"));
        dataSource.setUsername(environment.getProperty("database.username"));
        dataSource.setPassword(environment.getProperty("database.password"));
        dataSource.setMaximumPoolSize(Integer.parseInt(Objects.requireNonNull(environment.getProperty("database.maximum_pull_size"))));
        return dataSource;
    }

    public Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", environment.getProperty("hibernate.dialect"));
        properties.put("hibernate.show_sql", environment.getProperty("hibernate.show_sql"));
        properties.put("hibernate.hbm2ddl.auto", environment.getProperty("hibernate.hbm2ddl.auto"));
        return properties;
    }

    @Bean
    public PlatformTransactionManager transactionManagerBean(EntityManagerFactory factory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(factory);
        return transactionManager;
    }
}

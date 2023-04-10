package ru.javarush.todolist.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import ru.javarush.todolist.entity.Status;
import ru.javarush.todolist.entity.Task;
import ru.javarush.todolist.query.QuerySql;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//@Disabled
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class QuerySqlTest {
    private SessionFactory sessionFactory;
    private Session session;

    @BeforeAll
    void setUp() {
        try {
            runSqlScriptFile("src/test/resources/schema.sql");
            runSqlScriptFile("src/test/resources/test_data.sql");
            sessionFactory = new Configuration()
                    .addAnnotatedClass(Task.class)
                    .addAnnotatedClass(Status.class)
                    .addAnnotatedClass(TaskHibernateDaoImpl.class)
                    .buildSessionFactory();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

    }

    @BeforeEach
    void setUpThis() {
        session = sessionFactory.openSession();
        session.beginTransaction();
    }

    @AfterEach
    void tearDownThis() {
        session.getTransaction().commit();
        session.close();
    }

    @AfterAll
    void tearDown() {
        sessionFactory.close();
    }

    @Test
    void querySqlReturnAllTasks() {
        Query<Task> actualQuery = session.createQuery(QuerySql.GET_ALL, Task.class);
        List<Task> actualTasks = actualQuery.list();

        Query<Task> expectedQuery = session.createQuery("select t from Task t", Task.class);
        List<Task> expectedTasks = expectedQuery.list();

        assertEquals(expectedTasks, actualTasks);
    }

    @Test
    void querySqlReturnAllCountTasks() {
        Query<Long> actualQuery = session.createQuery(QuerySql.GET_ALL_COUNT, Long.class);
        Integer actualResult = Math.toIntExact(actualQuery.getSingleResult());

        Query<Long> expectedQuery = session.createQuery("select count(t) from Task t", Long.class);
        Integer expectedResult = Math.toIntExact(expectedQuery.getSingleResult());

        assertEquals(expectedResult, actualResult);
    }

    @Test
    void querySqlReturnTaskByID() {
        Query<Task> actualQuery = session.createQuery(QuerySql.GET_BY_ID, Task.class);
        actualQuery.setParameter("ID", 1);
        Task actualTask = actualQuery.uniqueResult();

        Query<Task> expectedQuery = session.createQuery("select t from Task t where id=:ID", Task.class);
        expectedQuery.setParameter("ID", 1);
        Task expectedTask = expectedQuery.uniqueResult();

        assertEquals(expectedTask, actualTask);
    }

    private void runSqlScriptFile(String uri) {
        try {
            String sqlScript = new String(Files.readAllBytes(Paths.get(uri)), StandardCharsets.UTF_8);
            SessionFactory scriptSessionFactory = new Configuration().buildSessionFactory();
            Session scriptSession = scriptSessionFactory.openSession();
            scriptSession.beginTransaction();

            NativeQuery nativeQuery = scriptSession.createNativeQuery(sqlScript);
            nativeQuery.executeUpdate();

            scriptSession.getTransaction().commit();
            scriptSession.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

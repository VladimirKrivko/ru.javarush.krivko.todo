package ru.javarush.todolist.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javarush.todolist.entity.Task;
import ru.javarush.todolist.query.QuerySql;

import java.util.List;
import java.util.Optional;

@Repository
public class TaskHibernateDaoImpl implements TaskDao {
    private final SessionFactory sessionFactory;

    public TaskHibernateDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<Task> getAll(int offset, int limit) {
        Session session = sessionFactory.getCurrentSession();
        Query<Task> query = session.createQuery(QuerySql.GET_ALL, Task.class);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public int getAllCount() {
        Session session = sessionFactory.getCurrentSession();
        Query<Long> query = session.createQuery(QuerySql.GET_ALL_COUNT, Long.class);
        return Math.toIntExact(query.uniqueResult());
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Optional<Task> getById(int id) {
        Session session = sessionFactory.getCurrentSession();
        Query<Task> query = session.createQuery(QuerySql.GET_BY_ID, Task.class);
        query.setParameter("ID", id);
        return Optional.of(query.uniqueResult());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveOrUpdate(Task task) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(task);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(Task task) {
        Session session = sessionFactory.getCurrentSession();
        session.remove(task);
    }
}

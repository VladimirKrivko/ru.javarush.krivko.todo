package ru.javarush.todolist;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.javarush.todolist.dao.TaskHibernateDao;
import ru.javarush.todolist.entity.Task;

public class TestGetBean {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext("ru.javarush.todolist");
        TaskHibernateDao taskHibernateDao = applicationContext.getBean(TaskHibernateDao.class);
        Task task = taskHibernateDao.getById(1);
        System.out.println(task);
    }
}

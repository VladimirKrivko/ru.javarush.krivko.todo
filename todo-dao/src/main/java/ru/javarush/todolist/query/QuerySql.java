package ru.javarush.todolist.query;

public class QuerySql {
    public static final String GET_ALL = "SELECT t FROM Task t";
    public static final String GET_ALL_COUNT = "SELECT COUNT(t) FROM Task t";
    public static final String GET_BY_ID = "SELECT t FROM Task t WHERE id=:ID";
}

package ru.javarush.todolist.exception;

public class TaskIdInvalidException extends RuntimeException {

    public TaskIdInvalidException(String message) {
        super(message);
    }
}

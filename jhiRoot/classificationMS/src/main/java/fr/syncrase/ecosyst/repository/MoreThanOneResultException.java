package fr.syncrase.ecosyst.repository;

public class MoreThanOneResultException extends Throwable {
    public MoreThanOneResultException(String message) {
        super(message);
    }
}

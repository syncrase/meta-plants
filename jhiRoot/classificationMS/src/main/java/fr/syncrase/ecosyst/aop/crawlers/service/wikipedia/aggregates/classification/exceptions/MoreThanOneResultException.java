package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.exceptions;

public class MoreThanOneResultException extends Throwable {
    public MoreThanOneResultException(String message) {
        super(message);
    }
}

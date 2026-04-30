package ru.rogotovsky.deal.exception;

public class KafkaMessageSendException extends RuntimeException {
    public KafkaMessageSendException(String message) {
        super(message);
    }
}

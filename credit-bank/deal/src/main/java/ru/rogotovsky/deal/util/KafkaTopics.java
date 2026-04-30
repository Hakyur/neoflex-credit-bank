package ru.rogotovsky.deal.util;

public class KafkaTopics {

    public static final String FINISH_REGISTRATION = "finish-registration";
    public static final String CREATE_DOCUMENTS = "create-documents";
    public static final String SEND_DOCUMENTS = "send-documents";
    public static final String SEND_SES = "send-ses";
    public static final String CREDIT_ISSUED = "credit-issued";
    public static final String STATEMENT_DENIED = "statement-denied";

    public static final int COUNT_PARTITIONS = 1;
    public static final int COUNT_REPLICAS = 1;
}

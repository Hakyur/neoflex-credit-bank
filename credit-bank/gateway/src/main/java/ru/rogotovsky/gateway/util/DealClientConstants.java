package ru.rogotovsky.gateway.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class DealClientConstants {

    public static final String CALCULATE_CREDIT = "/calculate/%s";
    public static final String SEND_DOCUMENTS = "/document/%s/send";
    public static final String SIGN_DOCUMENTS = "/document/%s/sign";
    public static final String VERIFY_CODE = "/document/%s/code";
    public static final String GET_STATEMENT = "/admin/statement/%s";
    public static final String GET_STATEMENTS = "/admin/statement";
}

package com.assignment;

import com.assignment.utils.PropertyReader;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.UUID;

public class UserInsertConstant {

    private static PropertyReader propertyReader = PropertyReader.getInstance();

    public final String ID = UUID.randomUUID().toString();

    public final String NAME = RandomStringUtils.randomAlphabetic(10);

    public final String PHONE = RandomStringUtils.randomNumeric(10);

    public static final String INSTANCE = propertyReader.getProperties().get("INSERT_USER_URI");

    public static final String DB = propertyReader.getProperties().get("DB");

    public static final String SCHEMA = propertyReader.getProperties().get("INSERT_USER_RESPONSE_SCHEMA");




}

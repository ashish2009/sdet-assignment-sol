package com.assignment.utils;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

public class PropertyReader {

    private static final String defaultEnvFile = "envConfig/env.properties";
    private static Map<String, String> propertyMap;
    private static PropertyReader instance;

    private PropertyReader() {
        propertyMap = getPropValues(defaultEnvFile);
    }

    public static synchronized PropertyReader getInstance() {
        if (instance == null) {
            instance = new PropertyReader();
        }
        return instance;
    }

    public Map<String, String> getProperties() {
        return propertyMap;
    }


    /**
     * get all the properties value present in config.properties
     *
     * @return hash map consisting all properties in key.value pair
     */
    public Map<String, String> getPropValues(String fileName) {
        Map<String, String> map = new HashMap<>();

        try {
            Properties prop = new Properties();

            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("Property file '" + fileName + "' not found in the classpath");
            }

            // get the property values
            Set propNames = prop.stringPropertyNames();
            Iterator<String> iterator = propNames.iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                map.put(key, prop.getProperty(key));
            }

            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }
}

package com.assignment.utils;

import com.assignment.pojo.Request;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.restassured.path.json.JsonPath;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class SpecReaderUtil {
    public static Request resolveSpec(Class className) {
        Field field = null;
        try {
            field = className.getDeclaredField("apiSpecs");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        ApiSpec args = field.getAnnotation(ApiSpec.class);
        Map<String, LinkedHashMap<String, Object>> allApiSpecs = new HashMap<>();
        Request.RequestBuilder apiSpecBuilder = Request.builder();
        Request apiSpec = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            if (args != null) {
                if (args.spec() != null && args.file() != null) {
                    File file = new File(SpecReaderUtil.class.getClassLoader().getResource(args.file()).getFile());
                    JSONParser parser = new JSONParser();
                    JSONArray jsonArray = (JSONArray) parser.parse(new FileReader(file));
                    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    mapper.configure(Feature.AUTO_CLOSE_SOURCE, true);
                    mapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
                    for (Object o : jsonArray) {
                        JsonNode jsonNode = mapper.readTree(o.toString());
                        JsonNode apiNode = jsonNode.at("/" + args.spec());
                        String string = apiNode.toString();
                        JsonPath jsonPath = new JsonPath(string);
                        JsonNode bodyNode = mapper.readTree(string);
                        ObjectNode mandatoryBody = mapper.readValue(bodyNode.at("/mandatoryBody").toPrettyString(), ObjectNode.class);
                        ObjectNode optionalBody = mapper.readValue(bodyNode.at("/optionalBody").toPrettyString(), ObjectNode.class);
                        apiSpec = apiSpecBuilder.path(jsonPath.getString("path"))
                                .mandatoryBody(mapper.writeValueAsString(mandatoryBody))
                                .optionalBody(mapper.writeValueAsString(optionalBody))
                                .method(jsonPath.getString("method"))
                                .headers(mapper.convertValue(jsonPath.getJsonObject("headers"), new TypeReference<Map<String, Object>>() {
                                }))
                                .dbQuery(jsonPath.getString("dbQuery")).build();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiSpec;
    }
}

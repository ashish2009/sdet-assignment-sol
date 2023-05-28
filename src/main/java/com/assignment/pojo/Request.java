package com.assignment.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.simple.JSONObject;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Request {
    private String path;
    private String method;
    private Map<String,Object> headers;
    private String mandatoryBody;
    private String optionalBody;
    private String dbQuery;
}

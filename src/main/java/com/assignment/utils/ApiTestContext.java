package com.assignment.utils;

import com.assignment.UserInsertConstant;
import com.assignment.pojo.Request;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ApiTestContext {
    public Response response;
    public RequestSpecification specification;
    public Request apiSpecs;

    public UserInsertConstant constant;
}

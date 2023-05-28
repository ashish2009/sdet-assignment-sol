package com.assignment.steps;

import com.assignment.UserInsertConstant;
import com.assignment.helper.CustomerDbHelper;
import com.assignment.pojo.Request;
import com.assignment.utils.ApiSpec;
import com.assignment.utils.ApiTestContext;
import com.assignment.utils.SpecReaderUtil;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;

import java.util.Collections;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class UserInsertStepDefination {

    @ApiSpec(file = "UserInsertSpec.json", spec = "user-insert")
    private Request apiSpecs = SpecReaderUtil.resolveSpec(UserInsertStepDefination.class);

    private UserInsertConstant constant;

    private ApiTestContext apiTestContext;

    public UserInsertStepDefination(ApiTestContext apiTestContext) {
        this.apiTestContext = apiTestContext;
    }

    @Given("Post user insert api")
    public void post_user_insert_api() {
        constant = new UserInsertConstant();
        apiTestContext.constant = constant;
        apiTestContext.apiSpecs = apiSpecs;
        apiTestContext.specification = given()
                .filters(Collections.singletonList(new RequestLoggingFilter()))
                .baseUri(UserInsertConstant.INSTANCE)
                .basePath(apiSpecs.getPath());
    }

    @When("provided valid header value")
    public void provided_valid_header_value() {
        apiTestContext.specification.headers(apiSpecs.getHeaders());
    }

    @When("provided valid data in mandatory field \\(id & phone_number)")
    public void provided_valid_data_in_mandatory_field_id_phone_number() {
        String bodyTemplate = apiSpecs.getMandatoryBody();
        String body = bodyTemplate.replace("${id}", constant.ID)
                .replace("${phone_number}", constant.PHONE);
        System.out.println(body);
        apiTestContext.specification.body(body);
    }


    @Given("user id is already inserted")
    public void user_id_is_already_inserted() {
        provided_valid_data_in_mandatory_field_id_phone_number();
        apiTestContext.specification.post().then().statusCode(200);
        provided_valid_data_in_mandatory_field_id_phone_number();
    }

    @When("{string} is missing")
    public void is_missing(String string) {
        if (string.equalsIgnoreCase("x-session-token")) {
            Map<String, Object> headers = apiSpecs.getHeaders();
            headers.remove("x-session-token");
            apiTestContext.specification.headers(headers);
        } else if (string.equalsIgnoreCase("id")) {
            String bodyTemplate = apiSpecs.getMandatoryBody();
            String body = bodyTemplate.replace("${id}", "")
                    .replace("${phone_number}", constant.PHONE);
            System.out.println(body);
            apiTestContext.specification.body(body);
        }

    }

    @When("^x-session-token is (.+) and user-agent is (.+) in header$")
    public void xsessiontoken_is_and_useragent_is_in_header(String xsessiontoken, String useragent) {
        Map<String, Object> headers = apiSpecs.getHeaders();
        headers.put("x-session-token", xsessiontoken);
        headers.put("user-agent", useragent);
        apiTestContext.specification.headers(headers);
    }

    @When("^name is (.+) and phone_number is (.+) in payload$")
    public void name_is_and_phonenumber_is_in_payload(String name, String phonenumber) {
        String bodyTemplate = apiSpecs.getOptionalBody();
        String body = bodyTemplate.replace("${id}", constant.ID)
                .replace("${phone_number}", phonenumber)
                .replace("${name}", name);
        apiTestContext.specification.body(body);
    }

    @When("provided valid data in optional field \\(name)")
    public void providedValidDataInOptionalFieldIdPhone_number() {
        String bodyTemplate = apiSpecs.getOptionalBody();
        String body = bodyTemplate.replace("${id}", constant.ID)
                .replace("${phone_number}", constant.PHONE)
                .replace("${name}", constant.NAME);
        apiTestContext.specification.body(body);
    }

}

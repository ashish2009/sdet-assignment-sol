package com.assignment.steps;

import com.assignment.UserInsertConstant;
import com.assignment.helper.CustomerDbHelper;
import com.assignment.pojo.Request;
import com.assignment.utils.ApiTestContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.junit.Assert;

import java.io.File;

import static com.assignment.UserInsertConstant.SCHEMA;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;

public class UserInsertValidationStep {


    private ApiTestContext apiTestContext;
    private Request apiSpecs;
    private UserInsertConstant constant;
    private CustomerDbHelper helper;
    public UserInsertValidationStep(ApiTestContext apiTestContext) {
        this.apiTestContext = apiTestContext;
        this.apiSpecs = apiTestContext.apiSpecs;
        this.constant = apiTestContext.constant;
        helper = CustomerDbHelper.getInstance();
    }

    @Then("Status_code equals {int}")
    public void status_code_equals(Integer statusCode) {
        if (apiSpecs.getMethod().equalsIgnoreCase("post")) {
            apiTestContext.response = apiTestContext.specification.post();
        }
        Assert.assertTrue("Api call failed with status code " + apiTestContext.response.statusCode(), apiTestContext.response.statusCode() == statusCode);
    }

    @Then("validate data in db")
    public void validate_data_in_db() {
        helper = CustomerDbHelper.getInstance();
        System.out.println("ID to query: " + constant.ID);
        String query = apiSpecs.getDbQuery().replace("${id}", "'" + constant.ID + "'");
        helper.execute(query);
        Assert.assertTrue("Id not found in DB", helper.getId().equals(constant.ID));
        Assert.assertTrue("phone_number not found in DB", helper.getPhoneNumber().equals(constant.PHONE));
    }

    @Then("validate sms_sent flag becomes {int} within {int} sec")
    public void validate_sms_sent_flag_becomes_within_sec(Integer status, Integer duration) {
        helper = CustomerDbHelper.getInstance();
        System.out.println("sms_sent: " + helper.getSMSSent());
        Assert.assertTrue("sms_sent not not updated in DB", helper.getSMSSent().equals(String.valueOf(status)));
    }

    @And("{string} message is {string}")
    public void messageIs(String type, String message) {
        if (type.equalsIgnoreCase("message")) {
            Assert.assertTrue("message in response not matched", apiTestContext.response.jsonPath().getString("message").equals(message));
        } else {
            Assert.assertTrue("message in response not matched, error text is :" + apiTestContext.response.jsonPath().getString("error"), apiTestContext.response.jsonPath().getString("error").contains(message));
        }
    }

    @Then("Status_code equals <errorCode>")
    public void status_codeEqualsErrorCode(String error) {
        status_code_equals(Integer.valueOf(error));
    }

    @And("^error message is (.+)$")
    public void error_message_is(String message) {
        messageIs("error", message);
    }

    @And("validate response schema")
    public void validateResponseSchema() {
        String path = System.getProperty("user.dir")+ "/src/test/resources/schema/"+SCHEMA;
        apiTestContext.response.then().assertThat().body(matchesJsonSchema(new File(path)));
    }
}

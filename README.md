# SDET-Assignment-Solution

## Test Plan ID
    tp_assignment_01
## Instructions (About the project)
    This document outlines a test plan & automation strategy for a post api call which saves data in db. 
## Feature to be tested (Name of module/functionality/services)
- User Authorization
- Access Control
- Data insertion using post api call
- SMS sent feature
## Feature not to be tested(The name of remaining tested modules)
- Integration of api with other services/module.
- Backward compatibility(multiple versions) or impact on other system.
```agsl
Not in scope
- Testing of reduction in response time of GET request if data is written to cache after update in db
- Race condition, if GET, PUT & DELETE requests are available.
- Tests for consistency, availability & reliability.
```
## Approach (Selected list of techniques with respect to test strategy)
### Functionality Testing: Our test cases fall into the following general test scenario groups
#### Basic positive tests (happy paths)
    Execute API call with valid required parameters.
#### Extended positive testing with optional parameters
    Execute API call with valid required parameters AND valid optional parameters
#### Negative testing with valid input
    Execute API calls with valid input that attempts illegal operations. I.e.: Attempting to create a resource with a id that already exists (e.g., user configuration with the same id)
#### Negative testing with invalid input
- Missing or invalid authorization token
- Missing required parameters 
- Payload with invalid model (violates schema)
- Payload with incomplete model (missing fields)
- Invalid values in HTTP headers
- Unsupported methods for endpoints 
#### Destructive testing: Intentionally attempt to fail the API to check its robustness
- Boundary value testing e.g, for String : Lengths: Max+1, Max, ""/Empty, null, "null", Min, Min-1
- Illegal characters in payload or fields
```
name contains special character. e.g, ash@ish
phone number contains non numeric value or special character. e.g "jhdhkfhdkj" , 99-23@0000
id contains spaces. eg "     "
```
- Empty payloads
- Overflow payload – huge JSON in request body
- Overflow parameter values ( invalid ID
  which is 1000 characters long
  )
- Using incorrect HTTP headers (e.g. Content-Type)
- Malformed content in request
- Wrong content-type in payload
#### Validation Of Functionality Tests
##### status code
- Verify requests should return 2XX HTTP status code (201 for POST  requests creating a new resource) in case of positive tests.
- Verify that an erroneous HTTP status code is sent (NOT 2XX) in case of negative tests.
- Verify that the HTTP status code is in accordance with error case as defined in spec.
##### payload
- Verify response is a well-formed JSON object.
- Response structure is according to data model (schema validation: field names and field types are as expected, including nested objects; field values are as expected; non-nullable fields are not null, etc.)
- Verify that error response is received in case of negative tests.
- Verify that error format is according to spec. e.g., error is a valid JSON object or a plain string (as defined in spec).
- Verify that there is a clear, descriptive error message/description field.
- Verify error description is correct for this error case and in accordance with spec.
##### state: Ensure action has been performed correctly in the system by validating data from DB.
- Given values in id, name & phone number are inserted in db properly.
- sms_sent flag becomes 1 within 10-20 second.
#### Headers
- Verify that HTTP headers are as expected, including content-type, connection, cache-control, expires,
  access-control-allow-origin, keep-alive, HSTS, and other standard header fields – according to spec.
- Verify that information is NOT leaked via headers (e.g. X-Powered-By header is not sent to the user). 
### Performance Sanity
- Verify basic level check like response time is less then specified time.
- Based on requirement load testing strategy can be applied & performance matrix(latency,cpu utilisation etc) can be monitored.
### Security Sanity
- If request are coming too quickly api should return 429 - Too many requests.
- Input validation failures should be logged.Assume that someone who is performing hundreds of failed input validations per second is up to no good.
- Reject the request (ideally with a 406 Not Acceptable response) if the Accept header does not specifically contain one of the allowable types.
- Respond with generic error messages - avoid revealing details of the failure unnecessarily.
- Do not pass technical details (e.g. call stacks or other internal hints) to the client.
- etc
## Test Environment
- Test server/pipeline details
## Automation Test Environment
- Language: Java (version: 8)
- Build Tool: Maven (version: 3)
- Test Framework: Junit & Cucumber
## Automation Test Execution step
```agsl
mvn clean install -DdbPath={db_path_to_be_passed}
```
## Entry Criteria
- Recieve stable build
- All P0 cases should be passing
## Suspension Criteria
- All P0 cases are failing
## Exit Criteria
- All test cases are executed
- All major bugs are resolved
- Stakeholders approve build with known issue
## Test Deliverables
- Test Scenarios
- Test Case Documents
- Test Logs
- Defect Reports
- Automation Execution Report: Report will be generated in test-output folder
  <a href="test-output/HtmlReport/ExtentHtml.html" target="_blank">Report</a>
## Schedule
- Testing schedule
## Responsibilities: Task breakdown when multiple people are involved
## Risk & Assumption: Analysed risk & assumption in project

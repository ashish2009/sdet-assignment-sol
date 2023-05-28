Feature: Validate user insertion flow

  @ValidInsertion
  Scenario: Verify user insertion with valid data in mandatory field
    Given Post user insert api
    When provided valid header value
    And provided valid data in mandatory field (id & phone_number)
    Then Status_code equals 200
    And validate response schema
    And "message" message is "customer created"
    And validate data in db
    And validate sms_sent flag becomes 1 within 20 sec

  @ValidInsertion
  Scenario: Verify user insertion with valid data in mandatory & optional field
    Given Post user insert api
    When provided valid header value
    And provided valid data in optional field (name)
    Then Status_code equals 200
    And validate response schema
    And "message" message is "customer created"
    And validate data in db
    And validate sms_sent flag becomes 1 within 20 sec

  @InvalidInsertion
  Scenario: Verify user insertion fails for duplicate user id
    Given Post user insert api
    When provided valid header value
    And user id is already inserted
    Then Status_code equals 500
    And "error" message is "UNIQUE constraint failed: customers.id"

  @InvalidInsertion
  Scenario: Verify user insertion fails if x-session-token is missing
    Given Post user insert api
    When "x-session-token" is missing
    And provided valid data in mandatory field (id & phone_number)
    Then Status_code equals 403
    And "error" message is "request cannot be authenticated!"

  @InvalidInsertion
  Scenario: Verify user insertion fails if mandatory field is missing
    Given Post user insert api
    When provided valid header value
    And "id" is missing
    Then Status_code equals 400
    And "error" message is "'Customer.ID' Error:Field validation for 'ID'"

  @InvalidInsertion
  Scenario Outline: Verify user insertion fails with invalid header value
    Given Post user insert api
    When x-session-token is <x-session-token> and user-agent is <user-agent> in header
    And provided valid data in mandatory field (id & phone_number)
    Then Status_code equals <errorCode>
    And error message is <message>

    Examples:
      |x-session-token|user-agent|errorCode|message|
      |null           |testing   |403      |request cannot be authenticated!|
      |authorized-user|testing-bot|400     |bad bot, go away!               |

  @InvalidInsertion
  Scenario Outline: Verify user insertion fails with invalid value in payload
    Given Post user insert api
    When provided valid header value
    And name is <name> and phone_number is <phone_number> in payload
    Then Status_code equals <errorCode>
    And error message is <message>

    Examples:
      |name|phone_number|errorCode|message|
      | ashis@h | 9977880022  |400    |name has special characters|
      | ashish  | 997788002211|500   |CHECK constraint failed: length(phone_number) = 10|
      | ashish  | 997788002   |500   |CHECK constraint failed: length(phone_number) = 10   |


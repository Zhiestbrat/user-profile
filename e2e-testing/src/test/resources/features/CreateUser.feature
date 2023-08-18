Feature: Create User

  Background:
    Given UP Service is up and running
    And User Endpoint is available:
      | method   | POST   |
      | endpoint | /users |

  Scenario: create User successfully
    When  a client want create user with mandatory parameters:
      | firstName | Steven             |
      | lastName  | Smith              |
      | email     | steven.j@apple.com |
    Then response is status CREATED
    When the client updates user details with mandatory info:
      | telegramId  | @smith     |
      | mobilePhone | 2355678901 |
    Then response is status CREATED
    And response body contains:
      | firstName   | Steven             |
      | lastName    | Smith              |
      | email       | steven.j@apple.com |
      | telegramId  | @smith             |
      | mobilePhone | 2355678901         |

  Scenario: client error while creating a user with invalid parameters
    When a client want create user with mandatory parameters:
      | firstName   | Steven         |
      | lastName    | Smith          |
      | email       | steve.jXXX.com |
      | telegramId  | @smith         |
      | mobilePhone | 2355678901     |

    Then response is status BAD_REQUEST
    And response body contains error:
      | status  | 400              |
      | message | Validation Error |

  Scenario: Internal error while updating a user if email already exists
    When a client wants to update a user
      | firstName   | Steven             |
      | lastName    | Smith              |
      | email       | steven.j@apple.com |
      | telegramId  | @smith             |
      | mobilePhone | 2355678901         |
    Then response is status BAD_REQUEST
    And response body contains:
      | status  | 400                   |
      | message | Internal Server Error |

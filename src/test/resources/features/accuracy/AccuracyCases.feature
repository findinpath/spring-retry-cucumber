Feature: General accuracy tests for the Github API client

  The purpose of these test scenarios is to show how the retrieval
  of the user details from Github API should work in a happy flow,
  when no unexpected errors occur.

  Scenario: Happy flow for retrieving user details
    Given I have configured the responses for the Github API
      | uri               | httpStatus | payloadFile                      |
      | /users/findinpath | 200        | api/github/users/findinpath.json |
    When I retrieve the details for the user findinpath
    Then I receive the user details
      | login      | blog                        |
      | findinpath | https://www.findinpath.com/ |
    And I have made 1 GET calls made towards Github API "/users/findinpath" resource
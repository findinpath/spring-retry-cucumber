Feature: General failure tests for the Github API client

  The purpose of these test scenarios is to show how the retrieval
  of the user details from Github API should work when failures
  occur on the Github API side. In case of sporadic exceptions
  occur on the Github API, due to the retry mechanism built-in
  the Github client, this should go unnoticed in the client program
  flow.


  Scenario: Single failure when fetching user details with retryable service
  still results in a successful Github API call
    Given I have configured the responses for the Github API
      | uri               | httpStatus | payloadFile                      |
      | /users/findinpath | 500        |                                  |
      | /users/findinpath | 200        | api/github/users/findinpath.json |
    When I retrieve the details for the user findinpath
    Then I receive the user details
      | login      | blog                        |
      | findinpath | https://www.findinpath.com/ |
    And I have made 2 GET calls made towards Github API "/users/findinpath" resource
    But I have a backoff delay between GET requests 1 and 2 made towards Github API "/users/findinpath" resource

  Scenario: Multiple failures when fetching user details with retryable service
  will eventually results in a erroneous Github API call
    Given I have configured the responses for the Github API
      | uri               | httpStatus | payloadFile                      |
      | /users/findinpath | 500        |                                  |
      | /users/findinpath | 500        |                                  |
      | /users/findinpath | 500        |                                  |
      | /users/findinpath | 500        |                                  |
      | /users/findinpath | 200        | api/github/users/findinpath.json |
    When I retrieve the details for the user findinpath
    Then I will receive an INTERNAL_SERVER_ERROR response status instead of the user details
    And I have made 3 GET calls made towards Github API "/users/findinpath" resource
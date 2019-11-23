package com.findinpath.cucumber.stepdefinitions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import com.findinpath.api.github.UsersEndpoint;
import io.cucumber.java.After;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientResponseException;

public class UserSteps {

  private UsersEndpoint usersEndpoint;
  private UserSharedScenarioData userSharedScenarioData;

  /**
   * Constructor for the Cucumber step related to Github user API.
   * <p>
   * NOTE that the constructor is annotated with spring's `@Autowired` annotation for instructing
   * the dependency container which other spring beans need to be injected in this class.
   *
   * @param usersEndpoint          the endpoint for getting user details from the Github API
   * @param userSharedScenarioData the data holder shared between the scenario steps
   */
  @Autowired
  public UserSteps(UsersEndpoint usersEndpoint,
      UserSharedScenarioData userSharedScenarioData) {
    this.usersEndpoint = usersEndpoint;
    this.userSharedScenarioData = userSharedScenarioData;
  }

  @After
  public void resetUserSharedScenarioData() {
    userSharedScenarioData.setUser(null);
    userSharedScenarioData.setWebClientResponseException(null);
  }

  @When("I retrieve the details for the user {word}")
  public void retrieveUserDetails(String username) {
    try {
      var user = usersEndpoint.getUser(username);
      userSharedScenarioData.setUser(user);
    } catch (WebClientResponseException e) {
      userSharedScenarioData.setWebClientResponseException(e);
    }
  }


  @Then("I receive the user details")
  public void checkUserDetails(ExpectedUserDetails expectedUser) {
    assertThat(userSharedScenarioData.getUser(), is(notNullValue()));

    var user = userSharedScenarioData.getUser();
    assertThat(user.getLogin(), equalTo(expectedUser.getLogin()));
    assertThat(user.getBlog(), equalTo(expectedUser.getBlog()));
  }

  @Then("I will receive an {httpStatus} response status instead of the user details")
  public void checkErroneousCall(HttpStatus httpStatus) {
    assertThat(userSharedScenarioData.getWebClientResponseException(), is(notNullValue()));
    assertThat(userSharedScenarioData.getWebClientResponseException().getStatusCode(),
        equalTo(httpStatus));
  }


  public static class ExpectedUserDetails {

    private String login;
    private String blog;

    public ExpectedUserDetails() {
    }

    public ExpectedUserDetails(String login, String blog) {
      this.login = login;
      this.blog = blog;
    }

    public String getLogin() {
      return login;
    }

    public void setLogin(String login) {
      this.login = login;
    }

    public String getBlog() {
      return blog;
    }

    public void setBlog(String blog) {
      this.blog = blog;
    }
  }

}

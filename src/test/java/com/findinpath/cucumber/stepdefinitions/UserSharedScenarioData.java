package com.findinpath.cucumber.stepdefinitions;

import com.findinpath.api.github.model.User;
import org.springframework.web.reactive.function.client.WebClientResponseException;

public class UserSharedScenarioData {

  private User user;

  private WebClientResponseException webClientResponseException;

  public UserSharedScenarioData() {
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public WebClientResponseException getWebClientResponseException() {
    return webClientResponseException;
  }

  public void setWebClientResponseException(
      WebClientResponseException webClientResponseException) {
    this.webClientResponseException = webClientResponseException;
  }
}

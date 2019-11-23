package com.findinpath.api.github;


import com.findinpath.api.github.model.User;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class UsersEndpoint {

  private WebClient webClient;

  public UsersEndpoint(WebClient webClient) {
    this.webClient = webClient;
  }

  public User getUser(String username) {
    try {
      return webClient
          .get()
          .uri("/users/{username}", username).accept(MediaType.APPLICATION_JSON)
          .retrieve()
          .onStatus(httpStatus -> HttpStatus.NOT_FOUND == httpStatus,
              clientResponse -> Mono.error(new UnknownUsernameException()))
          .bodyToMono(User.class)
          .block();
    } catch (UnknownUsernameException e) {
      return null;
    }
  }

  public List<User> getUsers(int sinceId) {
    var spec = webClient
        .get();

    WebClient.RequestHeadersSpec uriSpec;
    if (sinceId > 0) {
      uriSpec = spec.uri("users?since={since}", sinceId);
    } else {
      uriSpec = spec.uri("users");
    }

    return uriSpec
        .retrieve()
        .bodyToFlux(User.class)
        .collectList()
        .block();

  }

  private class UnknownUsernameException extends RuntimeException {

  }
}

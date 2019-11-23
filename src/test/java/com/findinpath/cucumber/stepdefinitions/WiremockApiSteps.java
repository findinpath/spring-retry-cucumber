package com.findinpath.cucumber.stepdefinitions;

import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;

import com.findinpath.wiremock.WireMockGithubApi;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.http.RequestMethod;
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder;
import com.github.tomakehurst.wiremock.stubbing.Scenario;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

public class WiremockApiSteps {


  private final WireMockGithubApi wireMockGithubApi;
  private final int retryInitialBackoffTimeMs;

  @Autowired
  public WiremockApiSteps(WireMockGithubApi wireMockGithubApi,
      @Value("${github.api.retry.initialBackoffTime}") int retryInitialBackoffTimeMs) {
    this.wireMockGithubApi = wireMockGithubApi;
    this.retryInitialBackoffTimeMs = retryInitialBackoffTimeMs;
  }


  @After
  public void resetWiremockServer() {
    wireMockGithubApi.getWireMockServer().resetAll();
  }

  @Given("I have configured the responses for the Github API")
  public void configureApiResponses(List<GithubApiResponse> responseList) {
    var server = wireMockGithubApi.getWireMockServer();

    var responsesByUriMap = responseList
        .stream()
        .collect(Collectors.groupingBy(githubApiResponse -> githubApiResponse.getUri().trim()));

    responsesByUriMap.forEach((uri, uriResponseList) -> {
      for (int index = 0; index < uriResponseList.size(); index++) {
        var scenarioState =
            (index == 0) ? Scenario.STARTED : "Attempt " + (index + 1) + " for " + uri;

        var scenarioName = uri;
        var githubApiResponse = uriResponseList.get(index);
        var scenarioMappingBuilder = WireMock
            .get(WireMock.urlEqualTo(uri))
            .inScenario(scenarioName)
            .whenScenarioStateIs(scenarioState);
        if (index != uriResponseList.size() - 1) {
          scenarioMappingBuilder = scenarioMappingBuilder
              .willSetStateTo("Attempt " + (index + 2) + " for " + uri);
        }
        if (githubApiResponse.getHttpStatus() == HttpStatus.OK.value()) {
          var response = WireMock.aResponse()
              .withHeader("Content-Type", "application/json")
              .withStatus(githubApiResponse.getHttpStatus());
          if (githubApiResponse.getPayloadFile() != null) {
            response.withBodyFile(githubApiResponse.getPayloadFile());
          }
          server.stubFor(
              scenarioMappingBuilder
                  .willReturn(response)
          );
        } else {
          server.stubFor(
              scenarioMappingBuilder
                  .willReturn(
                      WireMock.aResponse()
                          .withHeader("Content-Type", "application/json")
                          .withStatus(githubApiResponse.getHttpStatus())
                  )
          );
        }
      }
    });
  }

  @Then("I have made {int} {requestMethod} calls made towards Github API {string} resource")
  public void checkNumberOfApiCalls(int count, RequestMethod requestMethod, String uri) {
    var requests = wireMockGithubApi.getWireMockServer()
        .findAll(new RequestPatternBuilder(requestMethod, urlMatching(uri)));
    assertThat(requests, hasSize(count));
  }

  @Then("I have a backoff delay between {requestMethod} requests {int} and {int} made towards Github API {string} resource")
  public void checkBackoffDelayForRetriableApiCalls(RequestMethod requestMethod, int requestIndex1,
      int requestIndex2, String uri) {
    var requests = wireMockGithubApi.getWireMockServer()
        .findAll(new RequestPatternBuilder(requestMethod, urlMatching(uri)));

    var request1 = requests.get(requestIndex1 - 1);
    var request2 = requests.get(requestIndex2 - 1);
    assertThat(request2.getLoggedDate().getTime(),
        greaterThan(request1.getLoggedDate().getTime() + retryInitialBackoffTimeMs));
  }

  public static class GithubApiResponse {

    private String uri;
    private int httpStatus;
    private String payloadFile;

    public GithubApiResponse() {
    }

    public GithubApiResponse(String uri, int httpStatus, String payloadFile) {
      this.uri = uri;
      this.httpStatus = httpStatus;
      this.payloadFile = payloadFile;
    }

    public String getUri() {
      return uri;
    }

    public void setUri(String uri) {
      this.uri = uri;
    }

    public int getHttpStatus() {
      return httpStatus;
    }

    public void setHttpStatus(int httpStatus) {
      this.httpStatus = httpStatus;
    }

    public String getPayloadFile() {
      return payloadFile;
    }

    public void setPayloadFile(String payloadFile) {
      this.payloadFile = payloadFile;
    }

    @Override
    public String toString() {
      return "GithubApiResponse{" +
          "uri='" + uri + '\'' +
          ", httpStatus=" + httpStatus +
          ", payloadFile='" + payloadFile + '\'' +
          '}';
    }
  }

}

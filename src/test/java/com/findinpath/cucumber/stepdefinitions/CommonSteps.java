package com.findinpath.cucumber.stepdefinitions;


import com.findinpath.SpringDemoTestApplication;
import io.cucumber.java.en.When;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Common steps for the Cucumber tests.
 * <p>
 * NOTE that only one `Cucumber` step class has the `@SpringBootTest` annotation.
 * <p>
 * The `Cucumber` step classes are using the `test` spring profile in order to be able to interract
 * with mocked Github API powered by WireMock.
 */
@SpringBootTest(classes = SpringDemoTestApplication.class)
@ActiveProfiles("test")
public class CommonSteps {

  @When("I wait {long} milliseconds")
  public void sleep(Long milliseconds) throws InterruptedException {
    Thread.sleep(milliseconds);
  }
}
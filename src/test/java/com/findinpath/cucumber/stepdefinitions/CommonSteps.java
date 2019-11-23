package com.findinpath.cucumber.stepdefinitions;


import com.findinpath.SpringDemoTestApplication;
import io.cucumber.java.en.When;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = SpringDemoTestApplication.class)
@ActiveProfiles("test")
public class CommonSteps {

  @When("I wait {long} milliseconds")
  public void sleep(Long milliseconds) throws InterruptedException {
    Thread.sleep(milliseconds);
  }
}
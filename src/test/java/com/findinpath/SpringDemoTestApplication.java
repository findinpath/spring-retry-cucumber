package com.findinpath;

import com.findinpath.cucumber.stepdefinitions.UserSharedScenarioData;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringDemoTestApplication {

  @Bean
  public UserSharedScenarioData userSharedScenarioData() {
    return new UserSharedScenarioData();
  }
}
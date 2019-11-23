package com.findinpath.config;

import com.findinpath.api.github.GithubProperties;
import com.findinpath.wiremock.WireMockGithubApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;

@Configuration
@ActiveProfiles("test")
public class WireMockGithubPropertiesConfiguration {

  @Bean
  public WireMockGithubApi wireMockGithubApi() {
    return new WireMockGithubApi();
  }

  @Bean
  public GithubProperties githubProperties(WireMockGithubApi wireMockGithubApi,
      @Value("${github.api.connectTimeout}") int connectTimeout,
      @Value("${github.api.readTimeout}") int readTimeout
  ) {
    return new GithubProperties(wireMockGithubApi.getServerAddress(), connectTimeout, readTimeout);
  }
}

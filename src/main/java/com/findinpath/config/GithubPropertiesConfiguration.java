package com.findinpath.config;

import com.findinpath.api.github.GithubProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!test")
public class GithubPropertiesConfiguration {

  @Bean
  public GithubProperties githubProperties(
      @Value("${github.api.url}") String url,
      @Value("${github.api.connectTimeout}") int connectTimeout,
      @Value("${github.api.readTimeout}") int readTimeout
  ) {
    return new GithubProperties(url, connectTimeout, readTimeout);
  }
}

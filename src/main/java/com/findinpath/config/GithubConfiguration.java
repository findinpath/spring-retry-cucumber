package com.findinpath.config;

import com.findinpath.api.github.Github;
import com.findinpath.api.github.GithubProperties;
import com.findinpath.api.github.UsersEndpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class GithubConfiguration {

  @Bean
  public Github github(GithubProperties githubProperties) {
    return new Github(githubProperties);
  }

  @Bean
  public UsersEndpoint usersEndpoint(Github github) {
    return github.users();
  }
}

package com.findinpath.config;


import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.retry.backoff.ExponentialRandomBackOffPolicy;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

/**
 * Configuration class used with Spring AOP for wrapping the basic Github API client with
 * `spring-retry` functionality for being able to avoid interrupting the program flow in case that
 * sporadic exceptions occur on the Github API.
 * <p>
 * The program flow will retrieve successfully the user details even though sometimes one sporadic
 * API call will fail, because the API call will be retried and therefor in the client context, the
 * API call will appear as successful (even though it was actually performed two times).
 */
@Configuration
@ImportResource("classpath:/github-api-aop-config.xml")
public class GithubRetryConfiguration {

  private SimpleRetryPolicy createSimpleRetryPolicy(int maxAttempts) {
    var retryPolicy = new SimpleRetryPolicy();
    retryPolicy.setMaxAttempts(maxAttempts);
    return retryPolicy;
  }

  @Bean(name = "githubApiRetryTemplate")
  public RetryTemplate retryTemplate(
      @Value("${github.api.retry.maxAttempts}") int maxAttempts,
      @Value("${github.api.retry.initialBackoffTime}") int initialBackoffTime
  ) {
    var retryTemplate = new RetryTemplate();
    // random jitter is important for ensuring that not all clients back off the same way.
    var backOffPolicy = new ExponentialRandomBackOffPolicy();
    backOffPolicy.setInitialInterval(initialBackoffTime);
    retryTemplate.setBackOffPolicy(backOffPolicy);
    retryTemplate.setRetryPolicy(createSimpleRetryPolicy(maxAttempts));
    return retryTemplate;
  }


  @Bean(name = "githubApiRetryAdvice")
  public MethodInterceptor retryOperationsInterceptor(RetryTemplate retryTemplate) {
    var interceptor = new RetryOperationsInterceptor();
    interceptor.setRetryOperations(retryTemplate);
    return interceptor;
  }
}

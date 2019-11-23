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

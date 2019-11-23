package com.findinpath;


import com.findinpath.api.github.UsersEndpoint;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.stereotype.Component;

@SpringBootApplication
@EnableRetry
public class SpringDemoApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringDemoApplication.class);
  }
}


@Component
@Profile("!test")
class SpringDemo implements CommandLineRunner {

  private ConfigurableApplicationContext applicationContext;
  private UsersEndpoint usersEndpoint;


  public SpringDemo(ConfigurableApplicationContext applicationContext,
      UsersEndpoint usersEndpoint) {
    this.applicationContext = applicationContext;
    this.usersEndpoint = usersEndpoint;
  }

  @Override
  public void run(String... args) throws Exception {
    try {
      System.out.println(usersEndpoint.getUser("findinpath"));
      System.out.println(usersEndpoint.getUsers(0));
    } finally {
      // make sure to close the Spring application context after the execution of the
      // business logic of the application is done.
      applicationContext.close();
    }
  }
}
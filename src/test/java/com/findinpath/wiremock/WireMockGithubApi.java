package com.findinpath.wiremock;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import java.util.Optional;
import javax.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WireMockGithubApi {

  private static final Logger LOGGER = LoggerFactory.getLogger(WireMockGithubApi.class);

  private WireMockServer wireMockServer;


  public WireMockGithubApi() {
    wireMockServer = new WireMockServer(createWireMockConfiguration(Optional.empty()));
    startServer();
  }

  public void stopServer() {
    LOGGER.info("Stopping server.");
    if (wireMockServer.isRunning()) {
      LOGGER.info("Server was running on {}", getServerAddress());
      wireMockServer.stop();
    }
  }


  public void startServer() {
    LOGGER.info("Starting server");
    wireMockServer.start();
    LOGGER.info("Server is now running on {}", getServerAddress());
  }

  @PreDestroy
  public void preDestroy() {
    wireMockServer.stop();
  }


  public WireMockServer getWireMockServer() {
    return wireMockServer;
  }

  public String getServerAddress() {
    return "http://localhost:" + wireMockServer.port() + "/";
  }

  private WireMockConfiguration createWireMockConfiguration(Optional<Integer> port) {
    var configuration = new WireMockConfiguration();
    port.ifPresentOrElse(configuration::port, configuration::dynamicPort);
    return configuration;
  }

}

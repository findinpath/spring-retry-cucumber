package com.findinpath.api.github;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import java.util.concurrent.TimeUnit;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

public class Github {

  private WebClient webClient;


  public Github(GithubProperties githubProperties) {
    this.webClient = createWebClient(githubProperties);
  }

  public UsersEndpoint users() {
    return new UsersEndpoint(webClient);
  }

  private WebClient createWebClient(GithubProperties githubProperties) {

    var tcpClient = TcpClient.create()
        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, githubProperties.getConnectTimeout())
        .doOnConnected(
            connection ->
                connection.addHandler(
                    new ReadTimeoutHandler(
                        Integer.toUnsignedLong(githubProperties.getReadTimeout()),
                        TimeUnit.MILLISECONDS
                    )
                )
        );
    return WebClient.builder()
        .baseUrl(githubProperties.getUrl())
        .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/vnd.github.v3+json")
        .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient)))
        .build();
  }
}

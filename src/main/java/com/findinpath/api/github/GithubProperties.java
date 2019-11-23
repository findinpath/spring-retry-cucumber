package com.findinpath.api.github;

/**
 * Wrapper for the properties used to configure the Github API client.
 */
public class GithubProperties {

  private String url;
  private int connectTimeout;
  private int readTimeout;


  public GithubProperties(String url) {
    this(url, 10000, 10000);
  }

  public GithubProperties(String url, int connectTimeout, int readTimeout) {
    this.url = url;
    this.connectTimeout = connectTimeout;
    this.readTimeout = readTimeout;
  }

  public String getUrl() {
    return url;
  }

  public int getConnectTimeout() {
    return connectTimeout;
  }

  public int getReadTimeout() {
    return readTimeout;
  }
}

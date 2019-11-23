package com.findinpath.api.github.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.URL;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

  private String login;
  private long id;
  private URL url;
  @JsonProperty("repos_url")
  private URL reposUrl;
  private String name;
  private String blog;
  private String email;

  public User() {
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public URL getUrl() {
    return url;
  }

  public void setUrl(URL url) {
    this.url = url;
  }

  public URL getReposUrl() {
    return reposUrl;
  }

  public void setReposUrl(URL reposUrl) {
    this.reposUrl = reposUrl;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getBlog() {
    return blog;
  }

  public void setBlog(String blog) {
    this.blog = blog;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public String toString() {
    return "User{" +
        "login='" + login + '\'' +
        ", id=" + id +
        ", url=" + url +
        ", reposUrl=" + reposUrl +
        ", name='" + name + '\'' +
        ", blog='" + blog + '\'' +
        ", email='" + email + '\'' +
        '}';
  }
}

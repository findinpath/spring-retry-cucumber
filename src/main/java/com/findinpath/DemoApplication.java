package com.findinpath;

import com.findinpath.api.github.Github;
import com.findinpath.api.github.GithubProperties;

/**
 * Simple demo of the Github API client library without spring-retry functionality.
 */
public class DemoApplication {
    public static void main(String[] args) {
        var config = new GithubProperties("https://api.github.com/");
        var github = new Github(config);
        var usersEndpoint = github.users();

        System.out.println(usersEndpoint.getUser("findinpath"));
        System.out.println(usersEndpoint.getUsers(0));
    }
}
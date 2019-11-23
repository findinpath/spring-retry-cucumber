package com.findinpath;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    glue = {"cucumber.api.spring",
        "com.findinpath.cucumber.stepdefinitions"},
    features = "src/test/resources/features/",
    tags = "not @ignored",
    strict = false,
    plugin = {"html:target/cucumber-html-report",
        "json:target/cucumber-json-report.json",
        "pretty",
        "junit:cucumber-junit-report.log"}
)
public class CucumberTest {

}
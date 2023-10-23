package org.penekhun.wanted2023;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class TestWantedPreOnboarding2023Application {

  public static void main(String[] args) {
    SpringApplication.from(WantedPreOnboarding2023Application::main)
        .with(TestWantedPreOnboarding2023Application.class).run(args);
  }

}

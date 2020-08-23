package com.example.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "bootiful")
class BootifulProperties {

  private final String message;

  public BootifulProperties(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}
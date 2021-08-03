package com.github.gibmir.ion.api.configuration.exception;

public class ConfigurationProvidingException extends RuntimeException {
  public ConfigurationProvidingException() {
  }

  public ConfigurationProvidingException(final String message) {
    super(message);
  }

  public ConfigurationProvidingException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public ConfigurationProvidingException(final Throwable cause) {
    super(cause);
  }
}

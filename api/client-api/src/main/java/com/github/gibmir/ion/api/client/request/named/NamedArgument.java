package com.github.gibmir.ion.api.client.request.named;

public class NamedArgument<P> {
  private String name;
  private P namedArgument;

  public NamedArgument() {
  }

  private NamedArgument(String name, P namedArgument) {
    this.name = name;
    this.namedArgument = namedArgument;
  }

  public static <P> NamedArgument<P> nameAs(String argumentName, P argument) {
    return new NamedArgument<>(argumentName, argument);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public P getNamedArgument() {
    return namedArgument;
  }

  public void setNamedArgument(P namedArgument) {
    this.namedArgument = namedArgument;
  }
}

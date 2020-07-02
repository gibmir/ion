package com.github.gibmir.ion.api.client.request.factory.properties.environment;

public class Environment {
  public static class TestDto {
    private String someString;
    private Integer someInteger;

    public TestDto() {
    }

    public TestDto(String someString, Integer someInteger) {
      this.someString = someString;
      this.someInteger = someInteger;
    }

    public String getSomeString() {
      return someString;
    }

    public void setSomeString(String someString) {
      this.someString = someString;
    }

    public Integer getSomeInteger() {
      return someInteger;
    }

    public void setSomeInteger(Integer someInteger) {
      this.someInteger = someInteger;
    }
  }
}

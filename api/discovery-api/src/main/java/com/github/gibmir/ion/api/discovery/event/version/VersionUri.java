package com.github.gibmir.ion.api.discovery.event.version;

import java.util.List;

public class VersionUri {
  private String version;
  private List<String> uri;

  public VersionUri() {
  }

  public VersionUri(String version, List<String> uri) {
    this.version = version;
    this.uri = uri;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public List<String> getUri() {
    return uri;
  }

  public void setUri(List<String> uri) {
    this.uri = uri;
  }
}

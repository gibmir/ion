package com.github.gibmir.ion.api.discovery.event.data;

import com.github.gibmir.ion.api.discovery.event.version.VersionUri;

import java.util.List;

public class EventData {
  private List<VersionUri> versionUris;

  public EventData() {
  }

  public EventData(List<VersionUri> versionUris) {
    this.versionUris = versionUris;
  }

  public List<VersionUri> getVersionUris() {
    return versionUris;
  }

  public void setVersionUris(List<VersionUri> versionUris) {
    this.versionUris = versionUris;
  }
}

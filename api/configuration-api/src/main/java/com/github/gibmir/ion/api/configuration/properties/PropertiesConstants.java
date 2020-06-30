package com.github.gibmir.ion.api.configuration.properties;

public class PropertiesConstants {
  public static final String ROOT_PREFIX = "ion";
  public static final char DOT_SEPARATOR = '.';
  public static final String JSON_ENCODING = PropertiesConstants.ROOT_PREFIX
    + ".json.encoding";
  public static final String CONFIG_FILE_PATH_JAVA_PROPERTY = PropertiesConstants.ROOT_PREFIX
    + ".configuration.file.path";

  private PropertiesConstants() {
  }
}

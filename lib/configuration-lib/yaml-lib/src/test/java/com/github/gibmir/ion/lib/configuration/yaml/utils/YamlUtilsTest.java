package com.github.gibmir.ion.lib.configuration.yaml.utils;

import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.ByteArrayInputStream;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

public class YamlUtilsTest {
  public static final String CORRECT_YAML = "firstName: \"John\"\n" +
    "lastName: \"Doe\"\n" +
    "age: 31\n" +
    "ion: \n" +
    " json: \n" +
    "   encoding: \n" +
    "     UTF-8 \n" +
    " discovery: \n" +
    "     etcd: \n" +
    "       endpoints: \n" +
    "         - 10.11.22.33:62333 \n" +
    "         - 10.22.33.44:51234 \n";
  public static final Yaml YAML_READER = new Yaml();

  @Test
  void smoke() {
    Map<String, Object> propertyMap = YamlUtils.loadAsJavaProperty(YAML_READER,
      new ByteArrayInputStream(CORRECT_YAML.getBytes()));
    //{ion.discovery.etcd.endpoints=[10.11.22.33:62333, 10.22.33.44:51234], ion.json.encoding=UTF-8}
    assertThat(propertyMap.keySet(), hasSize(2));
    assertThat(propertyMap.values(), hasSize(2));
  }
}

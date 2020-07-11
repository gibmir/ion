package com.github.gibmir.ion.lib.configuration.yaml.utils;

import com.github.gibmir.ion.api.configuration.properties.ConfigurationUtils;
import com.github.gibmir.ion.lib.configuration.yaml.YamlConfiguration;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;

import javax.json.bind.Jsonb;
import java.io.ByteArrayInputStream;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

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
  public static final String INPUT_STREAM = "ion:\n" +
    "  jsonb:\n" +
    "    encoding:\n" +
    "      'UTF-8'\n" +
    "    null-values:\n" +
    "      TRUE\n" +
    "  client:\n" +
    "    request:\n" +
    "      charset:\n" +
    "        'UTF-8'\n" +
    "  server:\n" +
    "    request:\n" +
    "      charset:\n" +
    "        'UTF-8'\n" +
    "  netty:\n" +
    "    server:\n" +
    "      port:\n" +
    "        52222\n" +
    "      group:\n" +
    "        threads:\n" +
    "          count:\n" +
    "            2\n" +
    "        type:\n" +
    "          'NIO'\n" +
    "      channel:\n" +
    "        type:\n" +
    "          'NIO'\n" +
    "      log:\n" +
    "        level:\n" +
    "          'INFO'\n" +
    "    client:\n" +
    "      log:\n" +
    "        level:\n" +
    "          'INFO'\n" +
    "      channel:\n" +
    "        type:\n" +
    "          'NIO'\n" +
    "      group:\n" +
    "        type:\n" +
    "          'NIO'\n" +
    "        threads:\n" +
    "          count:\n" +
    "            2\n" +
    "      socket:\n" +
    "        address:\n" +
    "          host:\n" +
    "            'localhost'\n" +
    "          port:\n" +
    "            52222";

  @Test
  void smoke() {
    Map<String, Object> propertyMap = YamlUtils.loadAsJavaProperty(YAML_READER,
      new ByteArrayInputStream(CORRECT_YAML.getBytes()));
    //{ion.discovery.etcd.endpoints=[10.11.22.33:62333, 10.22.33.44:51234], ion.json.encoding=UTF-8}
    assertThat(propertyMap.keySet(), hasSize(2));
    assertThat(propertyMap.values(), hasSize(2));
  }


  @Test
  void hardSmoke() {
    Yaml yaml = new Yaml();

    Map<String, Object> stringObjectMap = assertDoesNotThrow(() -> YamlUtils.loadAsJavaProperty(yaml, INPUT_STREAM));
    YamlConfiguration yamlConfiguration = new YamlConfiguration(stringObjectMap);
    Jsonb jsonb = ConfigurationUtils.createJsonbWith(yamlConfiguration);

    System.out.println(stringObjectMap);
  }
}

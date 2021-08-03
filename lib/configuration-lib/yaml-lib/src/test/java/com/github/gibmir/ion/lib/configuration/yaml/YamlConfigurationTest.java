package com.github.gibmir.ion.lib.configuration.yaml;

import com.github.gibmir.ion.api.configuration.Configuration;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.isA;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.typeCompatibleWith;

class YamlConfigurationTest {

  public static final String ION_DISCOVERY_ETCD_ENDPOINTS_KEY = "ion.discovery.etcd.endpoints";
  public static final List<String> ION_DISCOVERY_ETCD_ENDPOINTS_VALUE = List.of("10.11.22.33:62333", " 10.22.33.44:51234");
  public static final String ION_JSON_ENCODING_KEY = "ion.json.encoding";
  public static final String ION_JSON_ENCODING_VALUE = "UTF-8";

  @Test
  void smoke() {
    //{ion.discovery.etcd.endpoints=[10.11.22.33:62333, 10.22.33.44:51234], ion.json.encoding=UTF-8}
    Map<String, Object> configMap = Map.of(ION_DISCOVERY_ETCD_ENDPOINTS_KEY, ION_DISCOVERY_ETCD_ENDPOINTS_VALUE,
      ION_JSON_ENCODING_KEY, ION_JSON_ENCODING_VALUE);
    Configuration configuration = new YamlConfiguration(configMap);
    assertThat(configuration.getValue(ION_DISCOVERY_ETCD_ENDPOINTS_KEY, List.class), equalTo(ION_DISCOVERY_ETCD_ENDPOINTS_VALUE));
    assertThat(configuration.getValue(ION_JSON_ENCODING_KEY, String.class), equalTo(ION_JSON_ENCODING_VALUE));
  }

  @Test
  void testGetValues() {
    Map<String, Object> configMap = Map.of(ION_DISCOVERY_ETCD_ENDPOINTS_KEY, ION_DISCOVERY_ETCD_ENDPOINTS_VALUE);
    Configuration configuration = new YamlConfiguration(configMap);
    List<String> actual = configuration.getValues(ION_DISCOVERY_ETCD_ENDPOINTS_KEY, String.class);
    assertThat(actual, isA(List.class));
    assertThat(actual, everyItem(isA(String.class)));
    assertThat(actual, equalTo(ION_DISCOVERY_ETCD_ENDPOINTS_VALUE));
  }

  @Test
  void testGetValuesWithUnexpectedKey() {
    Map<String, Object> configMap = Collections.emptyMap();
    Configuration configuration = new YamlConfiguration(configMap);
    List<String> actual = configuration.getValues(ION_DISCOVERY_ETCD_ENDPOINTS_KEY, String.class);
    assertThat(actual, notNullValue());
    assertThat(actual, empty());
  }

  @Test
  void testGetValuesWithSingleValue() {
    Map<String, Object> configMap = Map.of(ION_DISCOVERY_ETCD_ENDPOINTS_KEY, "test-string");
    Configuration configuration = new YamlConfiguration(configMap);
    List<String> actual = configuration.getValues(ION_DISCOVERY_ETCD_ENDPOINTS_KEY, String.class);
    assertThat(actual, isA(List.class));
    assertThat(actual, everyItem(isA(String.class)));
    assertThat(actual, hasSize(1));
  }
}

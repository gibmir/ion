package com.github.gibmir.ion.lib.configuration.yaml;

import com.github.gibmir.ion.api.configuration.Configuration;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

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
}

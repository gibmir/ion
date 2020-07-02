package com.github.gibmir.ion.lib.discovery.etcd.configuration;

import com.github.gibmir.ion.api.configuration.properties.ConfigurationUtils;

public class EtcdProperties {
  public static final String ETCD_PREFIX = "etcd";
  public static final String ENDPOINTS = ConfigurationUtils.ROOT_PREFIX +
    ConfigurationUtils.DOT_SEPARATOR +
    EtcdProperties.ETCD_PREFIX +
    ConfigurationUtils.DOT_SEPARATOR +
    "endpoints";

}

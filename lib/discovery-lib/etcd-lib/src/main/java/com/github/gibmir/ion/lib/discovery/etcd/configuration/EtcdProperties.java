package com.github.gibmir.ion.lib.discovery.etcd.configuration;

import com.github.gibmir.ion.api.configuration.properties.PropertiesConstants;

public class EtcdProperties {
  public static final String ETCD_PREFIX = "etcd";
  public static final String ENDPOINTS = PropertiesConstants.ROOT_PREFIX +
    PropertiesConstants.DOT_SEPARATOR +
    EtcdProperties.ETCD_PREFIX +
    PropertiesConstants.DOT_SEPARATOR +
    "endpoints";

}

package com.github.gibmir.ion.api.configuration.properties;

import com.github.gibmir.ion.api.configuration.Configuration;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Optional;

public final class ConfigurationUtils {
  public static final String ROOT_PREFIX = "ion";
  public static final char DOT_SEPARATOR = '.';
  //jsonb string properties
  public static final String JSONB_ENCODING = ConfigurationUtils.ROOT_PREFIX
    + DOT_SEPARATOR + JsonbConfig.ENCODING;
  public static final String JSONB_BINARY_DATA_STRATEGY = ConfigurationUtils.ROOT_PREFIX
    + DOT_SEPARATOR + JsonbConfig.BINARY_DATA_STRATEGY;
  public static final String JSONB_PROPERTY_NAMING_STRATEGY = ConfigurationUtils.ROOT_PREFIX
    + DOT_SEPARATOR + JsonbConfig.PROPERTY_NAMING_STRATEGY;
  public static final String JSONB_PROPERTY_ORDER_STRATEGY = ConfigurationUtils.ROOT_PREFIX
    + DOT_SEPARATOR + JsonbConfig.PROPERTY_ORDER_STRATEGY;
  public static final String JSONB_LOCALE = ConfigurationUtils.ROOT_PREFIX
    + DOT_SEPARATOR + JsonbConfig.LOCALE;
  public static final String JSONB_DATE_FORMAT = ConfigurationUtils.ROOT_PREFIX
    + DOT_SEPARATOR + JsonbConfig.DATE_FORMAT;
  //jsonb bool properties
  public static final String JSONB_FORMATTING = ConfigurationUtils.ROOT_PREFIX
    + DOT_SEPARATOR + JsonbConfig.FORMATTING;
  public static final String JSONB_NULL_VALUES = ConfigurationUtils.ROOT_PREFIX
    + DOT_SEPARATOR + JsonbConfig.NULL_VALUES;
  //java properties
  public static final String CONFIG_FILE_PATH_JAVA_PROPERTY = ConfigurationUtils.ROOT_PREFIX
    + ".configuration.file.path";
  public static final String CONFIG_FILE_PATH_ENV = "ION_CONFIGURATION_FILE_PATH";
  public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

  //charset
  public static final String REQUEST_CHARSET_PROPERTY = ROOT_PREFIX + ".client.request.charset";

  private ConfigurationUtils() {
  }

  /**
   * Creates {@link Jsonb} with provided config.
   *
   * @param configuration application config
   * @return {@link Jsonb}
   */
  public static Jsonb createJsonbWith(final Configuration configuration) {
    return JsonbBuilder.newBuilder().withConfig(readJsonbConfigFrom(configuration)).build();
  }

  /**
   * Reads jsonb config from application config.
   *
   * @param configuration application config
   * @return {@link JsonbConfig}
   */
  public static JsonbConfig readJsonbConfigFrom(final Configuration configuration) {
    JsonbConfig jsonbConfig = new JsonbConfig();
    configuration.getOptionalValue(JSONB_ENCODING, String.class).ifPresent(jsonbConfig::withEncoding);
    configuration.getOptionalValue(JSONB_BINARY_DATA_STRATEGY, String.class).ifPresent(jsonbConfig::withBinaryDataStrategy);
    configuration.getOptionalValue(JSONB_PROPERTY_NAMING_STRATEGY, String.class)
      .ifPresent(jsonbConfig::withPropertyNamingStrategy);
    configuration.getOptionalValue(JSONB_PROPERTY_ORDER_STRATEGY, String.class).ifPresent(jsonbConfig::withPropertyOrderStrategy);
    setDateFormat(configuration, jsonbConfig);
    configuration.getOptionalValue(JSONB_FORMATTING, Boolean.class).ifPresent(jsonbConfig::withFormatting);
    configuration.getOptionalValue(JSONB_NULL_VALUES, Boolean.class).ifPresent(jsonbConfig::withNullValues);
    return jsonbConfig;
  }

  private static void setDateFormat(final Configuration configuration, final JsonbConfig jsonbConfig) {
    Optional<Locale> optionalLocale = configuration.getOptionalValue(JSONB_LOCALE, String.class).map(Locale::new);
    optionalLocale.ifPresent(jsonbConfig::withLocale);
    configuration.getOptionalValue(JSONB_DATE_FORMAT, String.class)
      .ifPresent(dateFormat -> optionalLocale.ifPresent(locale -> jsonbConfig.withDateFormat(dateFormat, locale)));
  }

  /**
   * Reads charset from config.
   *
   * @param configuration application config
   * @return charset
   */
  public static Charset readCharsetFrom(final Configuration configuration) {
    return configuration.getOptionalValue(REQUEST_CHARSET_PROPERTY, String.class)
      .map(Charset::forName).orElse(ConfigurationUtils.DEFAULT_CHARSET);
  }
}

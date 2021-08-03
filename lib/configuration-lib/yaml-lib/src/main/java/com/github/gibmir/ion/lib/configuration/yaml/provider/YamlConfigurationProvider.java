package com.github.gibmir.ion.lib.configuration.yaml.provider;

import com.github.gibmir.ion.api.configuration.exception.ConfigurationProvidingException;
import com.github.gibmir.ion.api.configuration.properties.ConfigurationUtils;
import com.github.gibmir.ion.api.configuration.provider.ConfigurationProvider;
import com.github.gibmir.ion.lib.configuration.yaml.YamlConfiguration;
import com.github.gibmir.ion.lib.configuration.yaml.utils.YamlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

public class YamlConfigurationProvider implements ConfigurationProvider {
  private static final Logger LOGGER = LoggerFactory.getLogger(YamlConfigurationProvider.class);

  public static final String DEFAULT_RESOURCE = "application.yml";
  private static volatile YamlConfiguration yamlConfigurationInstance;

  @Override
  public final YamlConfiguration provide() {
    YamlConfiguration localInstance = yamlConfigurationInstance;
    //double-check singleton
    if (localInstance == null) {
      synchronized (YamlConfiguration.class) {
        localInstance = yamlConfigurationInstance;
        if (localInstance == null) {
          LOGGER.info("Starts to read yaml configuration");
          yamlConfigurationInstance = localInstance = readYamlConfiguration();
        }
      }
    }
    return localInstance;
  }

  private YamlConfiguration readYamlConfiguration() {
    Yaml yaml = new Yaml();
    InputStream configuration = resolveConfigurationFilePath()
      .map(YamlConfigurationProvider::getConfigurationFileInputStream)
      .orElseGet(YamlConfigurationProvider::getDefaultConfig);
    Map<String, Object> configMap = YamlUtils.loadAsJavaProperty(yaml, configuration);
    return new YamlConfiguration(configMap);
  }

  private static InputStream getDefaultConfig() {
    ClassLoader classLoader = YamlConfigurationProvider.class.getClassLoader();
    LOGGER.info("Configuration file path wasn't specified. Trying to find default resource [{}] in classloader [{}]",
      DEFAULT_RESOURCE, classLoader);
    try {
      return classLoader.getResourceAsStream(DEFAULT_RESOURCE);
    } catch (Throwable throwable) {
      String message = String.format("Can't find default configuration [%s] in ClassLoader [%s]",
        DEFAULT_RESOURCE, classLoader);
      throw new ConfigurationProvidingException(message, throwable);
    }
  }

  private static Optional<String> resolveConfigurationFilePath() {
    String configurationFilePath = System.getProperty(ConfigurationUtils.CONFIG_FILE_PATH_JAVA_PROPERTY);
    if (/*java variable wasn't specified*/configurationFilePath == null) {
      configurationFilePath = System.getenv(ConfigurationUtils.CONFIG_FILE_PATH_ENV);
      if (/*environment variable wasn't specified*/configurationFilePath == null) {
        return Optional.empty();
      } else {
        //java variable wasn't specified. env variable was specified.
        return Optional.of(configurationFilePath);
      }
    } else {
      //java variable was specified
      return Optional.of(configurationFilePath);
    }
  }

  private static InputStream getConfigurationFileInputStream(final String configurationFilePath) {
    LOGGER.info("Configuration file path was specified. Trying to find configuration in [{}]", configurationFilePath);
    Path path = Path.of(configurationFilePath);
    File file = path.toFile();
    try {
      return new FileInputStream(file);
    } catch (FileNotFoundException e) {
      String message = String.format("Can't find configuration in path [%s]. %s", configurationFilePath, e);
      throw new ConfigurationProvidingException(message, e);
    }
  }
}

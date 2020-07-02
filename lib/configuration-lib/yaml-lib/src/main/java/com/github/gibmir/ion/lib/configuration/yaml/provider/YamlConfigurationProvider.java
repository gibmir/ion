package com.github.gibmir.ion.lib.configuration.yaml.provider;

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

public class YamlConfigurationProvider implements ConfigurationProvider {
  private static final Logger LOGGER = LoggerFactory.getLogger(YamlConfigurationProvider.class);

  public static final String DEFAULT_RESOURCE = "application.yml";

  @Override
  public YamlConfiguration provide() {
    Yaml yaml = new Yaml();
    ClassLoader classLoader = YamlConfigurationProvider.class.getClassLoader();
    String configurationFilePath = System.getProperty(ConfigurationUtils.CONFIG_FILE_PATH_JAVA_PROPERTY);
    InputStream configurationFileInputStream = getConfigurationFileInputStream(classLoader, configurationFilePath);
    Map<String, Object> configMap = YamlUtils.loadAsJavaProperty(yaml, configurationFileInputStream);
    return new YamlConfiguration(configMap);
  }

  private static InputStream getConfigurationFileInputStream(ClassLoader classLoader, String configurationFilePath) {
    if (configurationFilePath != null) {
      Path path = Path.of(configurationFilePath);
      File file = path.toFile();
      try {
        return new FileInputStream(file);
      } catch (FileNotFoundException e) {
        LOGGER.error("Can't find configuration in path [{}].", configurationFilePath, e);
      }
    }
    return classLoader.getResourceAsStream(DEFAULT_RESOURCE);
  }
}

import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;

import java.util.Map;

public class YamlTest {

  public static final String TEST_FILE_PATH = "yaml/application-test.yml";

  @Test
  void smoke() {
    Yaml yaml = new Yaml();

    Map<String, Object> load = yaml.load(this.getClass()
      .getClassLoader()
      .getResourceAsStream(TEST_FILE_PATH));
    System.out.println(load);
  }
}

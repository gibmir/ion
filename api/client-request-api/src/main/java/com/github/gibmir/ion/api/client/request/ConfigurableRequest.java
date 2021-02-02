package com.github.gibmir.ion.api.client.request;

import javax.json.bind.Jsonb;
import java.nio.charset.Charset;

public interface ConfigurableRequest<R extends ConfigurableRequest<R>> {

  /**
   * @param jsonb json binding framework
   * @return configurable request
   * @implSpec You should return new request to separate configuration
   */
  R jsonb(Jsonb jsonb);

  /**
   * @param charset json encoding charset
   * @return configurable request
   * @implSpec You should return new request to separate configuration
   */
  R charset(Charset charset);

  /**
   * @return current request json binding framework
   * @implSpec default value should be consumed from configuration
   */
  Jsonb jsonb();

  /**
   * @return current request charset
   * @implSpec default value should be consumed from configuration
   */
  Charset charset();
}

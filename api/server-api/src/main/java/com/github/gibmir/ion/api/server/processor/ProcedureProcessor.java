package com.github.gibmir.ion.api.server.processor;

import javax.json.bind.Jsonb;
import java.nio.charset.Charset;

public interface ProcedureProcessor<T> {
  /**
   * Sets serializer.
   *
   * @param jsonb serializer
   * @return this
   */
  ProcedureProcessor<T> jsonb(Jsonb jsonb);

  /**
   * Gets current serializer.
   *
   * @return serializer
   */
  Jsonb jsonb();

  /**
   * Sets charset.
   *
   * @param charset charset
   * @return this
   */
  ProcedureProcessor<T> charset(Charset charset);

  /**
   * Gets charset.
   *
   * @return current charset
   */
  Charset charset();

  /**
   * Gets procedure API class.
   *
   * @return API class
   */
  Class<T> getProcedure();

  /**
   * Gets processor.
   *
   * @return processor
   */
  T getProcessor();
}

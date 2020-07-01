package com.github.gibmir.ion.api.client.request;

import javax.json.bind.Jsonb;
import java.nio.charset.Charset;
import java.time.Duration;

public interface ConfigurableRequest<R extends ConfigurableRequest<R>> {
  R jsonb(Jsonb jsonb);

  R timeout(Duration timeout);

  R charset(Charset charset);

  Jsonb jsonb();

  Duration timeout();

  Charset charset();
}

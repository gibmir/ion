package com.github.gibmir.ion.api.client.request;

import javax.json.bind.Jsonb;
import java.nio.charset.Charset;

public interface ConfigurableRequest<R extends ConfigurableRequest<R>> {
  R jsonb(Jsonb jsonb);

  R charset(Charset charset);

  Jsonb jsonb();

  Charset charset();
}

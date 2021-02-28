package com.github.gibmir.ion.api.server.processor;

import javax.json.bind.Jsonb;
import java.nio.charset.Charset;

public interface ProcedureProcessor<T> {

  ProcedureProcessor<T> jsonb(Jsonb jsonb);

  Jsonb jsonb();

  ProcedureProcessor<T> charset(Charset charset);

  Charset charset();

  Class<T> getProcedure();

  T getProcessor();
}

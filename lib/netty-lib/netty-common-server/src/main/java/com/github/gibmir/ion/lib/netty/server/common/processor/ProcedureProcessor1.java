package com.github.gibmir.ion.lib.netty.server.common.processor;

import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure1;
import com.github.gibmir.ion.api.server.processor.ProcedureProcessor;

import javax.json.bind.Jsonb;
import java.nio.charset.Charset;

public class ProcedureProcessor1<T, R, P extends JsonRemoteProcedure1<T, R>>
  extends AbstractProcedureProcessor<P> implements ProcedureProcessor<P> {

  public ProcedureProcessor1(final Class<P> procedure, final P processor, final Jsonb defaultJsonb,
                             final Charset defaultCharset) {
    super(procedure, processor, defaultJsonb, defaultCharset);
  }
}

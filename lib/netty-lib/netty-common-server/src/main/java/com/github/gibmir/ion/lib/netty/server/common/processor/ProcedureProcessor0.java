package com.github.gibmir.ion.lib.netty.server.common.processor;

import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure0;
import com.github.gibmir.ion.api.server.processor.ProcedureProcessor;

import javax.json.bind.Jsonb;
import java.nio.charset.Charset;

public class ProcedureProcessor0<R, P extends JsonRemoteProcedure0<R>>
  extends AbstractProcedureProcessor<P> implements ProcedureProcessor<P> {

  public ProcedureProcessor0(Class<P> procedure, P processor, Jsonb defaultJsonb, Charset defaultCharset) {
    super(procedure, processor, defaultJsonb, defaultCharset);
  }
}

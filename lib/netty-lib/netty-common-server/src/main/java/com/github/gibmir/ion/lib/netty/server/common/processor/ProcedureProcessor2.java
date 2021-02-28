package com.github.gibmir.ion.lib.netty.server.common.processor;

import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure2;
import com.github.gibmir.ion.api.server.processor.ProcedureProcessor;

import javax.json.bind.Jsonb;
import java.nio.charset.Charset;

public class ProcedureProcessor2<T1, T2, R, P extends JsonRemoteProcedure2<T1, T2, R>>
  extends AbstractProcedureProcessor<P> implements ProcedureProcessor<P> {

  public ProcedureProcessor2(Class<P> procedure, P processor, Jsonb defaultJsonb, Charset defaultCharset) {
    super(procedure, processor, defaultJsonb, defaultCharset);
  }
}

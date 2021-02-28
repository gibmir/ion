package com.github.gibmir.ion.lib.netty.server.common.processor;

import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure3;
import com.github.gibmir.ion.api.server.processor.ProcedureProcessor;

import javax.json.bind.Jsonb;
import java.nio.charset.Charset;

public class ProcedureProcessor3<T1, T2, T3, R, P extends JsonRemoteProcedure3<T1, T2, T3, R>>
  extends AbstractProcedureProcessor<P> implements ProcedureProcessor<P> {

  public ProcedureProcessor3(Class<P> procedure, P processor, Jsonb defaultJsonb, Charset defaultCharset) {
    super(procedure, processor, defaultJsonb, defaultCharset);
  }
}

package com.github.gibmir.ion.lib.netty.server.common.processor;

import com.github.gibmir.ion.api.server.processor.ProcedureProcessor;

import javax.json.bind.Jsonb;
import java.nio.charset.Charset;

public abstract class AbstractProcedureProcessor<P> implements ProcedureProcessor<P> {
  private final Class<P> procedure;
  private final P processor;
  private Jsonb jsonb;
  private Charset charset;

  protected AbstractProcedureProcessor(final Class<P> procedure, final P processor, final Jsonb jsonb,
                                       final Charset charset) {
    this.procedure = procedure;
    this.processor = processor;
    this.jsonb = jsonb;
    this.charset = charset;
  }

  @Override
  public final ProcedureProcessor<P> jsonb(final Jsonb jsonb) {
    this.jsonb = jsonb;
    return this;
  }

  @Override
  public final Jsonb jsonb() {
    return jsonb;
  }

  @Override
  public final ProcedureProcessor<P> charset(final Charset charset) {
    this.charset = charset;
    return this;
  }

  @Override
  public final Charset charset() {
    return charset;
  }

  @Override
  public final Class<P> getProcedure() {
    return procedure;
  }

  @Override
  public final P getProcessor() {
    return processor;
  }
}

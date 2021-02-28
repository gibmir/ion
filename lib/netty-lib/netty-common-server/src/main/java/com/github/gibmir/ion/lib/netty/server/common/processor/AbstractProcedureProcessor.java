package com.github.gibmir.ion.lib.netty.server.common.processor;

import com.github.gibmir.ion.api.server.processor.ProcedureProcessor;

import javax.json.bind.Jsonb;
import java.nio.charset.Charset;

public abstract class AbstractProcedureProcessor<P> implements ProcedureProcessor<P> {
  private final Class<P> procedure;
  private final P processor;
  private Jsonb jsonb;
  private Charset charset;

  protected AbstractProcedureProcessor(Class<P> procedure, P processor, Jsonb jsonb, Charset charset) {
    this.procedure = procedure;
    this.processor = processor;
    this.jsonb = jsonb;
    this.charset = charset;
  }

  @Override
  public ProcedureProcessor<P> jsonb(Jsonb jsonb) {
    this.jsonb = jsonb;
    return this;
  }

  @Override
  public Jsonb jsonb() {
    return jsonb;
  }

  @Override
  public ProcedureProcessor<P> charset(Charset charset) {
    this.charset = charset;
    return this;
  }

  @Override
  public Charset charset() {
    return charset;
  }

  @Override
  public Class<P> getProcedure() {
    return procedure;
  }

  @Override
  public P getProcessor() {
    return processor;
  }
}

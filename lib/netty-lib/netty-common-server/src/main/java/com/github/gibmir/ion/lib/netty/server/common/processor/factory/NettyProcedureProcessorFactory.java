package com.github.gibmir.ion.lib.netty.server.common.processor.factory;

import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure0;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure1;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure2;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure3;
import com.github.gibmir.ion.api.server.processor.ProcedureProcessor;
import com.github.gibmir.ion.api.server.processor.ProcedureProcessorFactory;
import com.github.gibmir.ion.lib.netty.server.common.processor.ProcedureProcessor0;
import com.github.gibmir.ion.lib.netty.server.common.processor.ProcedureProcessor1;
import com.github.gibmir.ion.lib.netty.server.common.processor.ProcedureProcessor2;
import com.github.gibmir.ion.lib.netty.server.common.processor.ProcedureProcessor3;

import javax.json.bind.Jsonb;
import java.nio.charset.Charset;

public final class NettyProcedureProcessorFactory implements ProcedureProcessorFactory {

  private final Jsonb defaultJsonb;
  private final Charset defaultCharset;

  public NettyProcedureProcessorFactory(final Jsonb defaultJsonb, final Charset defaultCharset) {
    this.defaultJsonb = defaultJsonb;
    this.defaultCharset = defaultCharset;
  }

  @Override
  public <R, P extends JsonRemoteProcedure0<R>> ProcedureProcessor<P> create(final Class<P> procedureClass,
                                                                             final P procedureImpl) {
    return new ProcedureProcessor0<>(procedureClass, procedureImpl, defaultJsonb, defaultCharset);
  }

  @Override
  public <T, R, P extends JsonRemoteProcedure1<T, R>> ProcedureProcessor<P> create(final Class<P> procedureClass,
                                                                                   final P procedureImpl) {
    return new ProcedureProcessor1<>(procedureClass, procedureImpl, defaultJsonb, defaultCharset);
  }

  @Override
  public <T1, T2, R, P extends JsonRemoteProcedure2<T1, T2, R>> ProcedureProcessor<P> create(final Class<P> procedureClass,
                                                                                             final P procedureImpl) {
    return new ProcedureProcessor2<>(procedureClass, procedureImpl, defaultJsonb, defaultCharset);
  }

  @Override
  public <T1, T2, T3, R, P extends JsonRemoteProcedure3<T1, T2, T3, R>> ProcedureProcessor<P> create(
    final Class<P> procedureClass, final P procedureImpl) {
    return new ProcedureProcessor3<>(procedureClass, procedureImpl, defaultJsonb, defaultCharset);
  }
}

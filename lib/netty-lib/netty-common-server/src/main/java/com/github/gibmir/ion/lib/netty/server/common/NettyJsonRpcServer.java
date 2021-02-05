package com.github.gibmir.ion.lib.netty.server.common;

import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure0;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure1;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure2;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure3;
import com.github.gibmir.ion.api.core.procedure.scan.ProcedureScanner;
import com.github.gibmir.ion.api.server.JsonRpcServer;
import com.github.gibmir.ion.api.server.cache.processor.ProcedureProcessorRegistry;
import com.github.gibmir.ion.api.server.cache.processor.factory.JsonRpcRequestProcessorFactory;
import com.github.gibmir.ion.api.server.manager.ComposedProcedureManager;
import com.github.gibmir.ion.api.server.manager.ProcedureManager;
import com.github.gibmir.ion.api.server.processor.ProcedureProcessor;
import com.github.gibmir.ion.lib.netty.server.common.manager.NettyProcedureManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NettyJsonRpcServer implements JsonRpcServer {
  private static final Logger LOGGER = LoggerFactory.getLogger(NettyJsonRpcServer.class);
  private final ProcedureProcessorRegistry procedureProcessorRegistry;

  public NettyJsonRpcServer(ProcedureProcessorRegistry procedureProcessorRegistry) {
    this.procedureProcessorRegistry = procedureProcessorRegistry;
  }

  @Override
  public <R, P extends JsonRemoteProcedure0<R>> ProcedureManager registerProcedureProcessor(
    Class<P> procedureClass, P procedureImpl) {
    String procedureName = ProcedureScanner.getProcedureName(procedureClass);
    LOGGER.debug("Procedure [{}] registration was started. Procedure impl {}", procedureName,
      procedureImpl);
    procedureProcessorRegistry.register(procedureName,
      JsonRpcRequestProcessorFactory.createProcessor0(procedureClass, procedureImpl));
    return new NettyProcedureManager(procedureProcessorRegistry, procedureName);
  }

  @Override
  public <T, R, P extends JsonRemoteProcedure1<T, R>> ProcedureManager registerProcedureProcessor(
    Class<P> procedureClass, P procedureImpl) {
    String procedureName = ProcedureScanner.getProcedureName(procedureClass);
    LOGGER.debug("Procedure [{}] registration was started. Procedure impl {}", procedureName,
      procedureImpl);
    procedureProcessorRegistry.register(procedureName,
      JsonRpcRequestProcessorFactory.createProcessor1(procedureClass, procedureImpl));
    return new NettyProcedureManager(procedureProcessorRegistry, procedureName);
  }

  @Override
  public <T1, T2, R, P extends JsonRemoteProcedure2<T1, T2, R>> ProcedureManager registerProcedureProcessor(
    Class<P> procedureClass, P procedureImpl) {
    String procedureName = ProcedureScanner.getProcedureName(procedureClass);
    LOGGER.debug("Procedure [{}] registration was started. Procedure impl {}", procedureName,
      procedureImpl);
    procedureProcessorRegistry.register(procedureName,
      JsonRpcRequestProcessorFactory.createProcessor2(procedureClass, procedureImpl));
    return new NettyProcedureManager(procedureProcessorRegistry, procedureName);
  }

  @Override
  public <T1, T2, T3, R, P extends JsonRemoteProcedure3<T1, T2, T3, R>> ProcedureManager registerProcedureProcessor(
    Class<P> procedureClass, P procedureImpl) {
    String procedureName = ProcedureScanner.getProcedureName(procedureClass);
    LOGGER.debug("Procedure [{}] registration was started. Procedure impl {}", procedureName,
      procedureImpl);
    procedureProcessorRegistry.register(procedureName, JsonRpcRequestProcessorFactory.createProcessor3(procedureClass,
      procedureImpl));
    return new NettyProcedureManager(procedureProcessorRegistry, procedureName);
  }

  @Override
  public ProcedureManager register(ProcedureProcessor... procedureProcessors) {
    LOGGER.debug("Procedure processors registration was started. Processors count [{}]", procedureProcessors.length);
    List<ProcedureManager> managers = new ArrayList<>(procedureProcessors.length);
    for (ProcedureProcessor procedureManager : procedureProcessors) {
      managers.add(procedureManager.register(this));
    }
    return new ComposedProcedureManager(managers);
  }

  @Override
  public ProcedureManager register(Collection<ProcedureProcessor> procedureProcessors) {
    int processorsCount = procedureProcessors.size();
    LOGGER.debug("Procedure processors registration was started. Processors count [{}]", processorsCount);
    List<ProcedureManager> managers = new ArrayList<>(processorsCount);
    for (ProcedureProcessor procedureManager : procedureProcessors) {
      managers.add(procedureManager.register(this));
    }
    return new ComposedProcedureManager(managers);
  }
}

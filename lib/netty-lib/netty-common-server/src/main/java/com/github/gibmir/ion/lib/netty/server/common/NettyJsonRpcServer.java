package com.github.gibmir.ion.lib.netty.server.common;

import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure0;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure1;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure2;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure3;
import com.github.gibmir.ion.scanner.ProcedureScanner;
import com.github.gibmir.ion.api.server.JsonRpcServer;
import com.github.gibmir.ion.api.server.cache.processor.ProcedureProcessorRegistry;
import com.github.gibmir.ion.lib.netty.server.common.processor.factory.JsonRpcRequestProcessorFactory;
import com.github.gibmir.ion.lib.netty.server.common.manager.ComposedProcedureManager;
import com.github.gibmir.ion.api.server.manager.ProcedureManager;
import com.github.gibmir.ion.api.server.processor.ProcedureProcessor;
import com.github.gibmir.ion.api.server.processor.ProcedureProcessorFactory;
import com.github.gibmir.ion.lib.netty.server.common.manager.NettyProcedureManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.bind.Jsonb;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NettyJsonRpcServer implements JsonRpcServer {
  private static final Logger LOGGER = LoggerFactory.getLogger(NettyJsonRpcServer.class);
  private final ProcedureProcessorRegistry procedureProcessorRegistry;
  private final ProcedureProcessorFactory procedureProcessorFactory;

  public NettyJsonRpcServer(ProcedureProcessorRegistry procedureProcessorRegistry,
                            ProcedureProcessorFactory procedureProcessorFactory) {
    this.procedureProcessorRegistry = procedureProcessorRegistry;
    this.procedureProcessorFactory = procedureProcessorFactory;
  }

  private ProcedureManager registerProcedureProcessor0(Class<?> procedureClass, Object procedureImpl, Jsonb jsonb) {
    String procedureName = ProcedureScanner.getProcedureName(procedureClass);
    LOGGER.debug("Procedure [{}] registration was started. Procedure impl {}", procedureName,
      procedureImpl);
    procedureProcessorRegistry.register(procedureName,
      JsonRpcRequestProcessorFactory.createProcessor0(procedureClass, procedureImpl, jsonb));
    return new NettyProcedureManager(procedureProcessorRegistry, procedureName);
  }

  private ProcedureManager registerProcedureProcessor1(Class<?> procedureClass, Object procedureImpl, Jsonb jsonb) {
    String procedureName = ProcedureScanner.getProcedureName(procedureClass);
    LOGGER.debug("Procedure [{}] registration was started. Procedure impl {}", procedureName,
      procedureImpl);
    procedureProcessorRegistry.register(procedureName,
      JsonRpcRequestProcessorFactory.createProcessor1(procedureClass, procedureImpl, jsonb));
    return new NettyProcedureManager(procedureProcessorRegistry, procedureName);
  }

  private ProcedureManager registerProcedureProcessor2(Class<?> procedureClass, Object procedureImpl, Jsonb jsonb) {
    String procedureName = ProcedureScanner.getProcedureName(procedureClass);
    LOGGER.debug("Procedure [{}] registration was started. Procedure impl {}", procedureName,
      procedureImpl);
    procedureProcessorRegistry.register(procedureName,
      JsonRpcRequestProcessorFactory.createProcessor2(procedureClass, procedureImpl, jsonb));
    return new NettyProcedureManager(procedureProcessorRegistry, procedureName);
  }

  private ProcedureManager registerProcedureProcessor3(Class<?> procedureClass, Object procedureImpl, Jsonb jsonb) {
    String procedureName = ProcedureScanner.getProcedureName(procedureClass);
    LOGGER.debug("Procedure [{}] registration was started. Procedure impl {}", procedureName,
      procedureImpl);
    procedureProcessorRegistry.register(procedureName, JsonRpcRequestProcessorFactory.createProcessor3(procedureClass,
      procedureImpl, jsonb));
    return new NettyProcedureManager(procedureProcessorRegistry, procedureName);
  }

  @Override
  public ProcedureProcessorFactory getProcedureProcessorFactory() {
    return procedureProcessorFactory;
  }

  @Override
  public ProcedureManager register(ProcedureProcessor<?>... procedureProcessors) {
    LOGGER.debug("Procedure processors registration was started. Processors count [{}]", procedureProcessors.length);
    List<ProcedureManager> managers = new ArrayList<>(procedureProcessors.length);
    for (ProcedureProcessor<?> procedureProcessor : procedureProcessors) {
      managers.add(register(procedureProcessor));
    }
    return new ComposedProcedureManager(managers);
  }

  @Override
  public ProcedureManager register(Collection<ProcedureProcessor<?>> procedureProcessors) {
    int processorsCount = procedureProcessors.size();
    LOGGER.debug("Procedure processors registration was started. Processors count [{}]", processorsCount);
    List<ProcedureManager> managers = new ArrayList<>(processorsCount);
    for (ProcedureProcessor<?> procedureManager : procedureProcessors) {
      managers.add(register(procedureManager));
    }
    return new ComposedProcedureManager(managers);
  }

  private ProcedureManager register(ProcedureProcessor<?> procedureProcessor) {
    Class<?> procedure = procedureProcessor.getProcedure();
    Object processor = procedureProcessor.getProcessor();
    Jsonb jsonb = procedureProcessor.jsonb();
    if (JsonRemoteProcedure0.class.isAssignableFrom(procedure)) {
      return registerProcedureProcessor0(procedure, processor, jsonb);
    } else if (JsonRemoteProcedure1.class.isAssignableFrom(procedure)) {
      return registerProcedureProcessor1(procedure, processor, jsonb);
    } else if (JsonRemoteProcedure2.class.isAssignableFrom(procedure)) {
      return registerProcedureProcessor2(procedure, processor, jsonb);
    } else if (JsonRemoteProcedure3.class.isAssignableFrom(procedure)) {
      return registerProcedureProcessor3(procedure, processor, jsonb);
    } else {
      throw new IllegalArgumentException();
    }
  }
}

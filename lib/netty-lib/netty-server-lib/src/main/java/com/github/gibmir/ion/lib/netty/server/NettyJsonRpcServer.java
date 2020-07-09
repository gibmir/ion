package com.github.gibmir.ion.lib.netty.server;

import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure0;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure1;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure2;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure3;
import com.github.gibmir.ion.api.server.JsonRpcServer;
import com.github.gibmir.ion.api.server.cache.processor.factory.JsonRpcRequestProcessorFactory;
import com.github.gibmir.ion.api.server.cache.processor.ProcedureProcessorRegistry;
import com.github.gibmir.ion.api.server.cache.signature.SignatureRegistry;
import com.github.gibmir.ion.api.server.manager.ProcedureManager;
import com.github.gibmir.ion.api.server.scan.ProcedureScanner;
import com.github.gibmir.ion.lib.netty.server.manager.NettyProcedureManager;

public class NettyJsonRpcServer implements JsonRpcServer {
  private final SignatureRegistry signatureRegistry;
  private final ProcedureProcessorRegistry procedureProcessorRegistry;

  public NettyJsonRpcServer(SignatureRegistry signatureRegistry, ProcedureProcessorRegistry procedureProcessorRegistry) {
    this.signatureRegistry = signatureRegistry;
    this.procedureProcessorRegistry = procedureProcessorRegistry;
  }

  @Override
  public <R, P extends JsonRemoteProcedure0<R>> ProcedureManager registerProcedureProcessor(
    Class<? extends JsonRemoteProcedure0<R>> procedureClass, P procedureImpl) {
    String procedureName = procedureClass.getName();
    procedureProcessorRegistry.putProcedureProcessorFor(procedureName,
      JsonRpcRequestProcessorFactory.createProcessor(procedureClass, procedureImpl));
    signatureRegistry.putProcedureSignature(procedureName, ProcedureScanner.resolveSignature0(procedureClass));
    return new NettyProcedureManager(signatureRegistry, procedureProcessorRegistry, procedureName);
  }

  @Override
  public <T, R, P extends JsonRemoteProcedure1<T, R>> ProcedureManager registerProcedureProcessor(
    Class<? extends JsonRemoteProcedure1<T, R>> procedureClass,P procedureImpl) {
    String procedureName = procedureClass.getName();
    procedureProcessorRegistry.putProcedureProcessorFor(procedureName,
      JsonRpcRequestProcessorFactory.createProcessor(procedureClass, procedureImpl));
    signatureRegistry.putProcedureSignature(procedureName, ProcedureScanner.resolveSignature1(procedureClass));
    return new NettyProcedureManager(signatureRegistry, procedureProcessorRegistry, procedureClass.getName());
  }

  @Override
  public <T1, T2, R, P extends JsonRemoteProcedure2<T1, T2, R>> ProcedureManager registerProcedureProcessor(
    Class<? extends JsonRemoteProcedure2<T1, T2, R>> procedureClass, P procedureImpl) {
    String procedureName = procedureClass.getName();
    procedureProcessorRegistry.putProcedureProcessorFor(procedureName,
      JsonRpcRequestProcessorFactory.createProcessor(procedureClass, procedureImpl));
    signatureRegistry.putProcedureSignature(procedureName, ProcedureScanner.resolveSignature2(procedureClass));
    return new NettyProcedureManager(signatureRegistry, procedureProcessorRegistry, procedureClass.getName());
  }

  @Override
  public <T1, T2, T3, R, P extends JsonRemoteProcedure3<T1, T2, T3, R>> ProcedureManager registerProcedureProcessor(
    Class<? extends JsonRemoteProcedure3<T1, T2, T3, R>> procedureClass, P procedureImpl) {
    String procedureName = procedureClass.getName();
    procedureProcessorRegistry.putProcedureProcessorFor(procedureName,
      JsonRpcRequestProcessorFactory.createProcessor(procedureClass, procedureImpl));
    signatureRegistry.putProcedureSignature(procedureName, ProcedureScanner.resolveSignature3(procedureClass));
    return new NettyProcedureManager(signatureRegistry, procedureProcessorRegistry, procedureClass.getName());
  }
}

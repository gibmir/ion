package com.github.gibmir.ion.lib.netty.server.manager;

import com.github.gibmir.ion.api.server.cache.processor.ProcedureProcessorRegistry;
import com.github.gibmir.ion.api.server.cache.signature.SignatureRegistry;
import com.github.gibmir.ion.api.server.manager.ProcedureManager;

public class NettyProcedureManager implements ProcedureManager {
  private final SignatureRegistry signatureRegistry;
  private final ProcedureProcessorRegistry procedureProcessorRegistry;
  private final String methodName;


  public NettyProcedureManager(SignatureRegistry signatureRegistry, ProcedureProcessorRegistry procedureProcessorRegistry, String methodName) {
    this.signatureRegistry = signatureRegistry;
    this.procedureProcessorRegistry = procedureProcessorRegistry;
    this.methodName = methodName;
  }

  @Override
  public void close() {
    signatureRegistry.unregister(methodName);
    procedureProcessorRegistry.unregister(methodName);
  }

  @Override
  public String getProcedureName() {
    return methodName;
  }
}

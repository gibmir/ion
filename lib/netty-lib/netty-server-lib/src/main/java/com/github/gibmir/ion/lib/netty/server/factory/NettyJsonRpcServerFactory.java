package com.github.gibmir.ion.lib.netty.server.factory;

import com.github.gibmir.ion.api.server.cache.processor.ProcedureProcessorRegistry;
import com.github.gibmir.ion.api.server.cache.signature.SignatureRegistry;
import com.github.gibmir.ion.api.server.factory.JsonRpcServerFactory;
import com.github.gibmir.ion.lib.netty.server.NettyJsonRpcServer;

public class NettyJsonRpcServerFactory implements JsonRpcServerFactory {
  private final SignatureRegistry signatureRegistry;
  private final ProcedureProcessorRegistry procedureProcessorRegistry;

  public NettyJsonRpcServerFactory(SignatureRegistry signatureRegistry, ProcedureProcessorRegistry procedureProcessorRegistry) {
    this.signatureRegistry = signatureRegistry;
    this.procedureProcessorRegistry = procedureProcessorRegistry;
  }

  @Override
  public NettyJsonRpcServer create() {
    return new NettyJsonRpcServer(signatureRegistry, procedureProcessorRegistry);
  }
}

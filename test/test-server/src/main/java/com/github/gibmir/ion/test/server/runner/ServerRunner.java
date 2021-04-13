package com.github.gibmir.ion.test.server.runner;

import com.github.gibmir.ion.api.server.JsonRpcServer;
import com.github.gibmir.ion.api.server.factory.JsonRpcServerFactory;
import com.github.gibmir.ion.api.server.factory.provider.JsonRpcServerFactoryProvider;
import com.github.gibmir.ion.api.server.processor.ProcedureProcessorFactory;
import com.github.gibmir.ion.test.common.procedure.TestStringProcedure;

public class ServerRunner {
  public static void main(String[] args) {
    JsonRpcServerFactory jsonRpcServerFactory = JsonRpcServerFactoryProvider.load().provide();
    JsonRpcServer jsonRpcServer = jsonRpcServerFactory.create();
    ProcedureProcessorFactory procedureProcessorFactory = jsonRpcServer.getProcedureProcessorFactory();
    jsonRpcServer.register(procedureProcessorFactory.create(TestStringProcedure.class, String::toUpperCase));
  }
}

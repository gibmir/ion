package com.github.gibmir.ion.test.server.runner;

import com.github.gibmir.ion.api.server.JsonRpcServer;
import com.github.gibmir.ion.api.server.factory.JsonRpcServerFactory;
import com.github.gibmir.ion.api.server.factory.provider.JsonRpcServerFactoryProvider;
import com.github.gimbir.ion.test.common.procedure.TestStringProcedure;

public class ServerRunner {
  public static void main(String[] args) {
    JsonRpcServerFactory jsonRpcServerFactory = JsonRpcServerFactoryProvider.load().provide();
    JsonRpcServer jsonRpcServer = jsonRpcServerFactory.create();
    jsonRpcServer.registerProcedureProcessor(TestStringProcedure.class, String::toUpperCase);
  }
}

package com.github.gibmir.ion.test.server.runner;

import com.github.gibmir.ion.api.server.JsonRpcServer;
import com.github.gibmir.ion.api.server.processor.ProcedureProcessor;
import com.github.gibmir.ion.api.server.factory.JsonRpcServerFactory;
import com.github.gibmir.ion.api.server.factory.provider.JsonRpcServerFactoryProvider;
import com.github.gimbir.ion.test.common.procedure.TestStringProcedure;

public class ServerRunner {
  public static void main(String[] args) {
    //todo Unexpected char -1 at (line no=1, column no=243, offset=242)
    JsonRpcServerFactory jsonRpcServerFactory = JsonRpcServerFactoryProvider.load().provide();
    JsonRpcServer jsonRpcServer = jsonRpcServerFactory.create();
    jsonRpcServer.register(ProcedureProcessor.from(TestStringProcedure.class, String::toUpperCase));
    jsonRpcServer.registerProcedureProcessor(TestStringProcedure.class, String::toUpperCase);
  }
}

package com.github.gibmir.ion.api.server.factory;

import com.github.gibmir.ion.api.server.JsonRpcServer;

public interface JsonRpcServerFactory {
  JsonRpcServer create();
}

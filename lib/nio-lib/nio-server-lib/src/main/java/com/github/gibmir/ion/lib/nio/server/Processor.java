package com.github.gibmir.ion.lib.nio.server;

import com.github.gibmir.ion.lib.nio.server.callback.PayloadCallback;

public class Processor {

  public void process(byte[] request, PayloadCallback callback) {
    callback.onResponse(request);
  }
}

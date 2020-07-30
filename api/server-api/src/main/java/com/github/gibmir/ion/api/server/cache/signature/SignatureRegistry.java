package com.github.gibmir.ion.api.server.cache.signature;

import com.github.gibmir.ion.api.core.procedure.signature.JsonRemoteProcedureSignature;

public interface SignatureRegistry {
  JsonRemoteProcedureSignature getProcedureSignatureFor(String procedureName);

  void register(String procedureName, JsonRemoteProcedureSignature procedureJsonRemoteProcedureSignature);

  void unregister(String procedureName);
}

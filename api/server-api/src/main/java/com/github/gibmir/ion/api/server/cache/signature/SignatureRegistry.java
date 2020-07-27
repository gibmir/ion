package com.github.gibmir.ion.api.server.cache.signature;

import com.github.gibmir.ion.api.core.procedure.signature.Signature;

public interface SignatureRegistry {
  Signature getProcedureSignatureFor(String procedureName);

  void register(String procedureName, Signature procedureSignature);

  void unregister(String procedureName);
}

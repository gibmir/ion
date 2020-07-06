package com.github.gibmir.ion.api.server.cache.signature;

import com.github.gibmir.ion.api.dto.method.signature.Signature;

public interface SignatureRegistry {
  Signature getProcedureSignatureFor(String methodName);

  void putProcedureSignature(String methodName, Signature procedureSignature);

  void clean(String methodName);
}

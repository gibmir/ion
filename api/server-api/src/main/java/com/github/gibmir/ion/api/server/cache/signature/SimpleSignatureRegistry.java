package com.github.gibmir.ion.api.server.cache.signature;

import com.github.gibmir.ion.api.core.procedure.signature.JsonRemoteProcedureSignature;

import java.util.Map;

public class SimpleSignatureRegistry implements SignatureRegistry {
  private final Map<String, JsonRemoteProcedureSignature> signatureMap;

  public SimpleSignatureRegistry(Map<String, JsonRemoteProcedureSignature> signatureMap) {
    this.signatureMap = signatureMap;
  }

  @Override
  public JsonRemoteProcedureSignature getProcedureSignatureFor(String procedureName) {
    return signatureMap.get(procedureName);
  }

  @Override
  public void register(String procedureName, JsonRemoteProcedureSignature procedureJsonRemoteProcedureSignature) {
    signatureMap.put(procedureName, procedureJsonRemoteProcedureSignature);
  }

  @Override
  public void unregister(String procedureName) {
    signatureMap.computeIfPresent(procedureName, (k, w) -> null);
  }
}

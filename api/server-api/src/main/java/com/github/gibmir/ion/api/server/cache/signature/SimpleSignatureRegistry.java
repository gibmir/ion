package com.github.gibmir.ion.api.server.cache.signature;

import com.github.gibmir.ion.api.core.procedure.signature.Signature;

import java.util.Map;

public class SimpleSignatureRegistry implements SignatureRegistry {
  private final Map<String, Signature> signatureMap;

  public SimpleSignatureRegistry(Map<String, Signature> signatureMap) {
    this.signatureMap = signatureMap;
  }

  @Override
  public Signature getProcedureSignatureFor(String procedureName) {
    return signatureMap.get(procedureName);
  }

  @Override
  public void register(String procedureName, Signature procedureSignature) {
    signatureMap.put(procedureName, procedureSignature);
  }

  @Override
  public void unregister(String procedureName) {
    signatureMap.computeIfPresent(procedureName, (k, w) -> null);
  }
}

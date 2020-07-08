package com.github.gibmir.ion.api.server.cache.signature;

import com.github.gibmir.ion.api.dto.method.signature.Signature;

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
  public void putProcedureSignature(String procedureName, Signature procedureSignature) {
    signatureMap.put(procedureName, procedureSignature);
  }

  @Override
  public void clean(String procedureName) {
    signatureMap.computeIfPresent(procedureName, (k, w) -> null);
  }
}

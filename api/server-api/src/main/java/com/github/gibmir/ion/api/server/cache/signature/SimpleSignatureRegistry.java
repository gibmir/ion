package com.github.gibmir.ion.api.server.cache.signature;

import com.github.gibmir.ion.api.dto.method.signature.Signature;

import java.util.Map;

public class SimpleSignatureRegistry implements SignatureRegistry {
  private final Map<String, Signature> signatureMap;

  public SimpleSignatureRegistry(Map<String, Signature> signatureMap) {
    this.signatureMap = signatureMap;
  }

  @Override
  public Signature getProcedureSignatureFor(String methodName) {
    return signatureMap.get(methodName);
  }

  @Override
  public void putProcedureSignature(String methodName, Signature procedureSignature) {
    signatureMap.put(methodName, procedureSignature);
  }

  @Override
  public void clean(String methodName) {
    signatureMap.computeIfPresent(methodName, (k, w) -> null);
  }
}

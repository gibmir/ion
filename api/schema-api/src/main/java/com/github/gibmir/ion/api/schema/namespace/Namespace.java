package com.github.gibmir.ion.api.schema.namespace;

import com.github.gibmir.ion.api.schema.procedure.Procedure;
import com.github.gibmir.ion.api.schema.type.TypeDeclaration;

import java.util.List;
import java.util.Map;

public interface Namespace {
  /**
   * @return user defined types
   */
  Map<String, TypeDeclaration> getTypes();

  /**
   * @return user defined procedures
   */
  List<Procedure> getProcedures();
}

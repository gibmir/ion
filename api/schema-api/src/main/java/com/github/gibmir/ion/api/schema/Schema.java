package com.github.gibmir.ion.api.schema;

import com.github.gibmir.ion.api.schema.procedure.Procedure;
import com.github.gibmir.ion.api.schema.type.TypeDeclaration;

import java.util.List;
import java.util.Map;

public interface Schema {
  /**
   * @return user defined types
   */
  Map<String, TypeDeclaration> getTypes();

  /**
   * @return user defined procedures
   */
  List<Procedure> getProcedures();
}

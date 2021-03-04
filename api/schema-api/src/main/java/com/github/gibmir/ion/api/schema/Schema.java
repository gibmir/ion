package com.github.gibmir.ion.api.schema;

import com.github.gibmir.ion.api.schema.procedure.Procedure;
import com.github.gibmir.ion.api.schema.type.TypeDeclaration;

import java.util.List;
import java.util.Map;

public interface Schema {
  Map<String, TypeDeclaration> getTypes();

  List<Procedure> getProcedures();
}

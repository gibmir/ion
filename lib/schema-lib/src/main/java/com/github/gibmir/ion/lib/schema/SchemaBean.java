package com.github.gibmir.ion.lib.schema;

import com.github.gibmir.ion.api.schema.Schema;
import com.github.gibmir.ion.api.schema.procedure.Procedure;
import com.github.gibmir.ion.api.schema.type.TypeDeclaration;

import java.util.List;
import java.util.Map;

public final class SchemaBean implements Schema {
  private List<Procedure> procedures;
  private Map<String, TypeDeclaration> types;

  public SchemaBean() {
    super();
  }

  public SchemaBean(final Map<String, TypeDeclaration> types,
                    final List<Procedure> procedures) {
    this.types = types;
    this.procedures = procedures;
  }

  @Override
  public Map<String, TypeDeclaration> getTypes() {
    return types;
  }

  @Override
  public List<Procedure> getProcedures() {
    return procedures;
  }

  @Override
  public String toString() {
    return "SchemaBean{"
      + "procedures=" + procedures
      + ", types=" + types + '}';
  }
}

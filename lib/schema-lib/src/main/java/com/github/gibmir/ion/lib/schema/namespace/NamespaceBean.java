package com.github.gibmir.ion.lib.schema.namespace;

import com.github.gibmir.ion.api.schema.namespace.Namespace;
import com.github.gibmir.ion.api.schema.procedure.Procedure;
import com.github.gibmir.ion.api.schema.type.TypeDeclaration;
import com.github.gibmir.ion.lib.schema.SchemaElementBean;

import java.util.List;
import java.util.Map;

public class NamespaceBean extends SchemaElementBean implements Namespace {
  private final List<Procedure> procedures;
  private final Map<String, TypeDeclaration> types;

  public NamespaceBean(final String id, final String name, final String description, final List<Procedure> procedures,
                       final Map<String, TypeDeclaration> types) {
    super(id, name, description);
    this.procedures = procedures;
    this.types = types;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<String, TypeDeclaration> getTypes() {
    return types;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Procedure> getProcedures() {
    return procedures;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return "NamespaceBean{"
      + "id='"
      + id
      + '\''
      + ", name='"
      + name
      + '\''
      + ", description='"
      + description
      + '\''
      + ", procedures="
      + procedures
      + ", types="
      + types
      + '}';
  }
}

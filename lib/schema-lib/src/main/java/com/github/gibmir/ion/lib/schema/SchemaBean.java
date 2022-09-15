package com.github.gibmir.ion.lib.schema;

import com.github.gibmir.ion.api.schema.Schema;
import com.github.gibmir.ion.api.schema.namespace.Namespace;

import java.util.List;

public final class SchemaBean implements Schema {
  private final List<Namespace> namespaces;

  public SchemaBean(final List<Namespace> namespaces) {
    this.namespaces = namespaces;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Namespace> getNamespaces() {
    return namespaces;
  }

  @Override
  public String toString() {
    return "SchemaBean{"
      + "namespaces="
      + namespaces
      + '}';
  }
}

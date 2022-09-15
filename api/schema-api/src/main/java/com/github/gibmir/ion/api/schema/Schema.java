package com.github.gibmir.ion.api.schema;

import com.github.gibmir.ion.api.schema.namespace.Namespace;

import java.util.List;

public interface Schema {
  /**
   * @return schema namespaces
   */
  List<Namespace> getNamespaces();
}

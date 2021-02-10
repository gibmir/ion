package com.github.gibmir.ion.api.schema.type;

import com.github.gibmir.ion.api.schema.SchemaElement;

/**
 * @implSpec must implements {@link Object#equals(Object)} and {@link Object#hashCode()}
 */
public interface TypeDeclaration extends SchemaElement {
  PropertyType[] getPropertyTypes();

  TypeParameter[] getParameters();
}

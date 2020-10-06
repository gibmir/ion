package com.github.gibmir.ion.api.schema.service.procedure;

import com.github.gibmir.ion.api.schema.SchemaElement;
import com.github.gibmir.ion.api.schema.type.PropertyType;

public interface Procedure extends SchemaElement {
  PropertyType[] getArgumentTypes();

  PropertyType getReturnArgumentType();
}

package com.github.gibmir.ion.api.schema;

import com.github.gibmir.ion.api.schema.service.Service;
import com.github.gibmir.ion.api.schema.type.TypeDeclaration;

import java.util.Map;

public interface Schema {
  Map<String, TypeDeclaration> getTypes();

  Service[] getServices();
}

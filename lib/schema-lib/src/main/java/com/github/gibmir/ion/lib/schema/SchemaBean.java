package com.github.gibmir.ion.lib.schema;

import com.github.gibmir.ion.api.schema.Schema;
import com.github.gibmir.ion.api.schema.service.Service;
import com.github.gibmir.ion.api.schema.type.TypeDeclaration;

import java.util.Arrays;
import java.util.Map;

public class SchemaBean implements Schema {
  private Service[] services;
  private Map<String, TypeDeclaration> types;

  public SchemaBean() {
    super();
  }

  public SchemaBean(Map<String, TypeDeclaration> types,
                    Service... services) {
    this.services = services;
    this.types = types;
  }

  @Override
  public Map<String, TypeDeclaration> getTypes() {
    return types;
  }

  @Override
  public Service[] getServices() {
    return services;
  }

  @Override
  public String toString() {
    return "SchemaBean{" +
      "services=" + Arrays.toString(services) +
      ", types=" + types +
      '}';
  }
}

package com.github.gibmir.ion.lib.schema;

import com.github.gibmir.ion.api.schema.Schema;
import com.github.gibmir.ion.api.schema.service.Service;
import com.github.gibmir.ion.api.schema.type.TypeDeclaration;

import java.util.Map;

public class SchemaBean extends SchemaElementBean implements Schema {
  private Service[] services;
  private Map<String, TypeDeclaration> types;

  public SchemaBean() {
    super();
  }

  public SchemaBean(String id, String name, String description, Map<String, TypeDeclaration> types,
                    Service... services) {
    super(id, name, description);
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
}

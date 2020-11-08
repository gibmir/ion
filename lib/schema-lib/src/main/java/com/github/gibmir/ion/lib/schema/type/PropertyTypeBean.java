package com.github.gibmir.ion.lib.schema.type;

import com.github.gibmir.ion.api.schema.type.PropertyType;
import com.github.gibmir.ion.lib.schema.SchemaElementBean;

public class PropertyTypeBean extends SchemaElementBean implements PropertyType {
  private String typeName;

  public PropertyTypeBean() {
    super();
  }

  public PropertyTypeBean(String id, String name, String description, String typeName) {
    super(id, name, description);
    this.typeName = typeName;
  }

  @Override
  public boolean equals(Object o) {
    return super.equals(o);
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  @Override
  public String getTypeName() {
    return typeName;
  }

  @Override
  public String toString() {
    return "PropertyTypeBean{" +
      "typeName='" + typeName + '\'' +
      ", id='" + id + '\'' +
      ", name='" + name + '\'' +
      ", description='" + description + '\'' +
      '}';
  }
}

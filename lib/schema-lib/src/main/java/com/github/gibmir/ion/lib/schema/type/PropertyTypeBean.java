package com.github.gibmir.ion.lib.schema.type;

import com.github.gibmir.ion.api.schema.type.PropertyType;
import com.github.gibmir.ion.lib.schema.SchemaElementBean;

public final class PropertyTypeBean extends SchemaElementBean implements PropertyType {
  private String typeName;

  public PropertyTypeBean() {
    super();
  }

  public PropertyTypeBean(final String id, final String name, final String description, final String typeName) {
    super(id, name, description);
    this.typeName = typeName;
  }

  @Override
  public boolean equals(final Object o) {
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
    return "PropertyTypeBean{"
      + "typeName='" + typeName + '\''
      + ", id='" + id + '\''
      + ", name='" + name + '\''
      + ", description='" + description + '\'' + '}';
  }
}

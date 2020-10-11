package com.github.gibmir.ion.lib.schema.type;

import com.github.gibmir.ion.api.schema.type.PropertyType;
import com.github.gibmir.ion.lib.schema.SchemaElementBean;

public class PropertyTypeBean extends SchemaElementBean implements PropertyType {
  public PropertyTypeBean() {
    super();
  }

  public PropertyTypeBean(String id, String name, String description) {
    super(id, name, description);
  }

  @Override
  public boolean equals(Object o) {
    return super.equals(o);
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }
}

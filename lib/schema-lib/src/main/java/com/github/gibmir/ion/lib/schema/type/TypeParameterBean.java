package com.github.gibmir.ion.lib.schema.type;

import com.github.gibmir.ion.api.schema.type.TypeParameter;
import com.github.gibmir.ion.lib.schema.SchemaElementBean;

public class TypeParameterBean extends SchemaElementBean implements TypeParameter {
  public TypeParameterBean() {
  }

  public TypeParameterBean(final String id, final String name, final String description) {
    super(id, name, description);
  }
}

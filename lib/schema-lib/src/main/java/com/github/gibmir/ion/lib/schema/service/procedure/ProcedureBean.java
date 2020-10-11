package com.github.gibmir.ion.lib.schema.service.procedure;

import com.github.gibmir.ion.api.schema.service.procedure.Procedure;
import com.github.gibmir.ion.api.schema.type.PropertyType;
import com.github.gibmir.ion.lib.schema.SchemaElementBean;

public class ProcedureBean extends SchemaElementBean implements Procedure {
  private PropertyType[] argumentTypes;
  private PropertyType returnArgumentType;

  public ProcedureBean() {
  }

  public ProcedureBean(String id, String name, String description, PropertyType[] argumentTypes,
                       PropertyType returnArgumentType) {
    super(id, name, description);
    this.argumentTypes = argumentTypes;
    this.returnArgumentType = returnArgumentType;
  }

  @Override
  public PropertyType[] getArgumentTypes() {
    return argumentTypes;
  }

  @Override
  public PropertyType getReturnArgumentType() {
    return returnArgumentType;
  }

  public void setArgumentTypes(PropertyType... argumentTypes) {
    this.argumentTypes = argumentTypes;
  }

  public void setReturnArgumentType(PropertyType returnArgumentType) {
    this.returnArgumentType = returnArgumentType;
  }
}

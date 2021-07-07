package com.github.gibmir.ion.lib.schema.procedure;

import com.github.gibmir.ion.api.schema.procedure.Procedure;
import com.github.gibmir.ion.api.schema.type.PropertyType;
import com.github.gibmir.ion.lib.schema.SchemaElementBean;

public final class ProcedureBean extends SchemaElementBean implements Procedure {
  private PropertyType[] argumentTypes;
  private PropertyType returnArgumentType;

  public ProcedureBean() {
  }

  public ProcedureBean(final String id, final String name, final String description, final PropertyType[] argumentTypes,
                       final PropertyType returnArgumentType) {
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

  /**
   * Sets argument types.
   *
   * @param argumentTypes argument types
   */
  public void setArgumentTypes(final PropertyType... argumentTypes) {
    this.argumentTypes = argumentTypes;
  }

  /**
   * Sets return argument type.
   *
   * @param returnArgumentType return argument type
   */
  public void setReturnArgumentType(final PropertyType returnArgumentType) {
    this.returnArgumentType = returnArgumentType;
  }
}

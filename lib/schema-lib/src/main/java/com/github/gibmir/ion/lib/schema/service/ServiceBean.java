package com.github.gibmir.ion.lib.schema.service;

import com.github.gibmir.ion.api.schema.service.Service;
import com.github.gibmir.ion.api.schema.service.procedure.Procedure;
import com.github.gibmir.ion.lib.schema.SchemaElementBean;

public class ServiceBean extends SchemaElementBean implements Service {
  private Procedure[] procedures;

  public ServiceBean() {
    super();
  }

  public ServiceBean(String id, String name, String description, Procedure... procedures) {
    super(id, name, description);
    this.procedures = procedures;
  }

  @Override
  public Procedure[] getServiceProcedures() {
    return procedures;
  }

  public void setProcedures(Procedure... procedures) {
    this.procedures = procedures;
  }
}

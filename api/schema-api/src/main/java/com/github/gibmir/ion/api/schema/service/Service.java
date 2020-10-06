package com.github.gibmir.ion.api.schema.service;

import com.github.gibmir.ion.api.schema.SchemaElement;
import com.github.gibmir.ion.api.schema.service.procedure.Procedure;

public interface Service extends SchemaElement {
  Procedure[] getServiceProcedures();
}

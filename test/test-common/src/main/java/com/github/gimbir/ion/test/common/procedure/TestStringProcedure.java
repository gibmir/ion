package com.github.gimbir.ion.test.common.procedure;

import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure1;
import com.github.gibmir.ion.api.core.procedure.named.Named;

@Named(name = "namedProcedure")
public interface TestStringProcedure extends JsonRemoteProcedure1<String, String> {
  @Override
  String call(@Named(name = "namedArgument") String arg);
}

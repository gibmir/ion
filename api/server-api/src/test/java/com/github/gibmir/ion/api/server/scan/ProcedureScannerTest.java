package com.github.gibmir.ion.api.server.scan;

import com.github.gibmir.ion.api.core.procedure.scan.ProcedureScanner;
import com.github.gibmir.ion.api.core.procedure.signature.JsonRemoteProcedureSignature;
import com.github.gibmir.ion.api.server.environment.ServerTestEnvironment;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProcedureScannerTest {

  @Test
  void testResolveCorrectSignature0() {
    JsonRemoteProcedureSignature jsonRemoteProcedureSignature =
      assertDoesNotThrow(() -> ProcedureScanner.resolveSignature0(ServerTestEnvironment.TestProcedure0.class));
    assertThat(jsonRemoteProcedureSignature.getGenericTypes().length, is(0));
    assertThat(jsonRemoteProcedureSignature.getReturnType().getTypeName(),
      is(ServerTestEnvironment.ReturnTestType.class.getName()));
  }

  @Test
  void testResolveCorrectSignature1() {
    JsonRemoteProcedureSignature jsonRemoteProcedureSignature =
      assertDoesNotThrow(() -> ProcedureScanner.resolveSignature1(ServerTestEnvironment.TestProcedure1.class));
    Type[] genericTypes = jsonRemoteProcedureSignature.getGenericTypes();

    assertThat(genericTypes, arrayWithSize(1));
    assertThat(genericTypes[0].getTypeName(), equalTo(ServerTestEnvironment.FirstTestType.class.getName()));
    assertThat(jsonRemoteProcedureSignature.getReturnType().getTypeName(),
      equalTo(ServerTestEnvironment.ReturnTestType.class.getName()));
  }

  @Test
  void testResolveCorrectSignature2() {
    JsonRemoteProcedureSignature jsonRemoteProcedureSignature =
      assertDoesNotThrow(() -> ProcedureScanner.resolveSignature2(ServerTestEnvironment.TestProcedure2.class));
    Type[] genericTypes = jsonRemoteProcedureSignature.getGenericTypes();

    assertThat(genericTypes, arrayWithSize(2));
    assertThat(genericTypes[0].getTypeName(), equalTo(ServerTestEnvironment.FirstTestType.class.getName()));
    assertThat(genericTypes[1].getTypeName(), equalTo(ServerTestEnvironment.SecondTestType.class.getName()));
    assertThat(jsonRemoteProcedureSignature.getReturnType().getTypeName(),
      equalTo(ServerTestEnvironment.ReturnTestType.class.getName()));
  }

  @Test
  void testResolveCorrectSignature3() {
    JsonRemoteProcedureSignature jsonRemoteProcedureSignature =
      assertDoesNotThrow(() -> ProcedureScanner.resolveSignature3(ServerTestEnvironment.TestProcedure3.class));
    Type[] genericTypes = jsonRemoteProcedureSignature.getGenericTypes();

    assertThat(genericTypes, arrayWithSize(3));
    assertThat(genericTypes[0].getTypeName(), equalTo(ServerTestEnvironment.FirstTestType.class.getName()));
    assertThat(genericTypes[1].getTypeName(), equalTo(ServerTestEnvironment.SecondTestType.class.getName()));
    assertThat(genericTypes[2].getTypeName(), equalTo(ServerTestEnvironment.ThirdTestType.class.getName()));
    assertThat(jsonRemoteProcedureSignature.getReturnType().getTypeName(),
      equalTo(ServerTestEnvironment.ReturnTestType.class.getName()));
  }

  @Test
  //raw type exception testing
  @SuppressWarnings({"unchecked", "rawtypes"})
  void testResolveRawSignature1() {
    assertThrows(IllegalArgumentException.class,
      () -> {
        Class rawTestProcedure1Class = ServerTestEnvironment.RawTestProcedure1.class;
        ProcedureScanner.resolveSignature1(rawTestProcedure1Class)
          .getGenericTypes();
      });
  }

  @Test
  //raw type exception testing
  @SuppressWarnings({"unchecked", "rawtypes"})
  void testResolveRawSignature2() {
    assertThrows(IllegalArgumentException.class, () -> {
      Class rawTestProcedure2Class = ServerTestEnvironment.RawTestProcedure2.class;
      ProcedureScanner.resolveSignature2(rawTestProcedure2Class)
        .getGenericTypes();
    });
  }

  @Test
  //raw type exception testing
  @SuppressWarnings({"unchecked", "rawtypes"})
  void testResolveRawSignature3() {
    assertThrows(IllegalArgumentException.class, () -> {
      Class rawTestProcedure3Class = ServerTestEnvironment.RawTestProcedure3.class;
      ProcedureScanner.resolveSignature3(rawTestProcedure3Class)
        .getGenericTypes();
    });
  }

  @Test
  void testResolveMultipleSignature1() {
    assertDoesNotThrow(
      () -> ProcedureScanner.resolveSignature1(ServerTestEnvironment.MultipleTestProcedure1.class)
        .getGenericTypes());
  }

  @Test
  void testResolveMultipleSignature2() {
    assertDoesNotThrow(
      () -> ProcedureScanner.resolveSignature2(ServerTestEnvironment.MultipleTestProcedure2.class)
        .getGenericTypes());
  }

  @Test
  void testResolveMultipleSignature3() {
    assertDoesNotThrow(
      () -> ProcedureScanner.resolveSignature3(ServerTestEnvironment.MultipleTestProcedure3.class)
        .getGenericTypes());
  }

  @Test
  void testResolveHierarchySignature0() {
    JsonRemoteProcedureSignature jsonRemoteProcedureSignature =
      assertDoesNotThrow(() -> ProcedureScanner.resolveSignature0(ServerTestEnvironment.HierarchyTestProcedure0.class));
    assertThat(jsonRemoteProcedureSignature.getGenericTypes().length, is(0));
    assertThat(jsonRemoteProcedureSignature.getReturnType().getTypeName(),
      is(ServerTestEnvironment.ReturnTestType.class.getName()));
  }

  @Test
  void testResolveHierarchySignature1() {
    JsonRemoteProcedureSignature jsonRemoteProcedureSignature =
      assertDoesNotThrow(() -> ProcedureScanner.resolveSignature1(ServerTestEnvironment.HierarchyTestProcedure1.class));
    Type[] genericTypes = jsonRemoteProcedureSignature.getGenericTypes();
    assertThat(genericTypes, arrayWithSize(1));
    assertThat(genericTypes[0].getTypeName(), equalTo(ServerTestEnvironment.FirstTestType.class.getName()));
    assertThat(jsonRemoteProcedureSignature.getReturnType().getTypeName(),
      equalTo(ServerTestEnvironment.ReturnTestType.class.getName()));
  }

  @Test
  void testResolveHierarchySignature2() {
    JsonRemoteProcedureSignature jsonRemoteProcedureSignature =
      assertDoesNotThrow(() -> ProcedureScanner.resolveSignature2(ServerTestEnvironment.HierarchyTestProcedure2.class));
    Type[] genericTypes = jsonRemoteProcedureSignature.getGenericTypes();
    assertThat(genericTypes, arrayWithSize(2));
    assertThat(genericTypes[0].getTypeName(), equalTo(ServerTestEnvironment.FirstTestType.class.getName()));
    assertThat(genericTypes[1].getTypeName(), equalTo(ServerTestEnvironment.SecondTestType.class.getName()));
    assertThat(jsonRemoteProcedureSignature.getReturnType().getTypeName(),
      equalTo(ServerTestEnvironment.ReturnTestType.class.getName()));
  }

  @Test
  void testResolveHierarchySignature3() {
    JsonRemoteProcedureSignature jsonRemoteProcedureSignature =
      assertDoesNotThrow(() -> ProcedureScanner.resolveSignature3(ServerTestEnvironment.HierarchyTestProcedure3.class));
    Type[] genericTypes = jsonRemoteProcedureSignature.getGenericTypes();
    assertThat(genericTypes, arrayWithSize(3));
    assertThat(genericTypes[0].getTypeName(), equalTo(ServerTestEnvironment.FirstTestType.class.getName()));
    assertThat(genericTypes[1].getTypeName(), equalTo(ServerTestEnvironment.SecondTestType.class.getName()));
    assertThat(genericTypes[2].getTypeName(), equalTo(ServerTestEnvironment.ThirdTestType.class.getName()));
    assertThat(jsonRemoteProcedureSignature.getReturnType().getTypeName(),
      equalTo(ServerTestEnvironment.ReturnTestType.class.getName()));
  }
}

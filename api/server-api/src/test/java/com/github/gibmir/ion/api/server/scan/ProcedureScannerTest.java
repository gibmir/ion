package com.github.gibmir.ion.api.server.scan;

import com.github.gibmir.ion.api.dto.method.signature.Signature;
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
    Signature signature = assertDoesNotThrow(ProcedureScanner::resolveSignature0);
    assertThat(signature.getGenericTypes().length, is(0));
  }

  @Test
  void testResolveCorrectSignature1() {
    Type[] genericTypes = assertDoesNotThrow(() -> ProcedureScanner.resolveSignature1(ServerTestEnvironment.TestProcedure1.class)
      .getGenericTypes());

    assertThat(genericTypes, arrayWithSize(1));
    assertThat(genericTypes[0].getTypeName(), equalTo(String.class.getName()));

  }

  @Test
  void testResolveCorrectSignature2() {
    Type[] genericTypes = assertDoesNotThrow(() -> ProcedureScanner.resolveSignature2(ServerTestEnvironment.TestProcedure2.class)
      .getGenericTypes());

    assertThat(genericTypes, arrayWithSize(2));
    assertThat(genericTypes[0].getTypeName(), equalTo(String.class.getName()));
    assertThat(genericTypes[1].getTypeName(), equalTo(String.class.getName()));
  }

  @Test
  void testResolveCorrectSignature3() {
    Type[] genericTypes = assertDoesNotThrow(() -> ProcedureScanner.resolveSignature3(ServerTestEnvironment.TestProcedure3.class)
      .getGenericTypes());

    assertThat(genericTypes, arrayWithSize(3));
    assertThat(genericTypes[0].getTypeName(), equalTo(String.class.getName()));
    assertThat(genericTypes[1].getTypeName(), equalTo(String.class.getName()));
    assertThat(genericTypes[2].getTypeName(), equalTo(String.class.getName()));
  }

  //todo incorrect until deep scan feature
  @Test
  void testResolveIncorrectSignature1() {
    assertThrows(IllegalArgumentException.class,
      () -> ProcedureScanner.resolveSignature1(ServerTestEnvironment.IncorrectTestProcedure1.class)
        .getGenericTypes());
  }

  @Test
  void testResolveIncorrectSignature2() {
    assertThrows(IllegalArgumentException.class, () -> ProcedureScanner.resolveSignature2(ServerTestEnvironment.IncorrectTestProcedure2.class)
      .getGenericTypes());
  }

  @Test
  void testResolveIncorrectSignature3() {
    assertThrows(IllegalArgumentException.class, () -> ProcedureScanner.resolveSignature3(ServerTestEnvironment.IncorrectTestProcedure3.class)
      .getGenericTypes());
  }
}

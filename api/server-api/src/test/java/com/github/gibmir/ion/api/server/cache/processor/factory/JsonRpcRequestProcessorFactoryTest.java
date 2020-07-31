//package com.github.gibmir.ion.api.server.cache.processor.factory;
//
//import com.github.gibmir.ion.api.core.procedure.scan.ProcedureScanner;
//import com.github.gibmir.ion.api.core.procedure.signature.JsonRemoteProcedureSignature;
//import com.github.gibmir.ion.api.server.cache.processor.JsonRpcRequestProcessor;
//import com.github.gibmir.ion.api.dto.request.transfer.notification.NotificationDto;
//import com.github.gibmir.ion.api.dto.request.transfer.RequestDto;
//import com.github.gibmir.ion.api.server.environment.ServerTestEnvironment;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.doThrow;
//import static org.mockito.Mockito.mock;
//
//class JsonRpcRequestProcessorFactoryTest {
//
//  public static final String ID = "id";
//  public static final String NO_ARG_RESULT = "no-arg";
//  public static final ServerTestEnvironment.TestProcedure0 TEST_PROCEDURE_0 = () -> NO_ARG_RESULT;
//  public static final RequestDto NO_ARG_REQUEST = RequestDto.positional(ID,
//    ServerTestEnvironment.TestProcedure0.class.getName(), new Object[0]);
//  public static final ServerTestEnvironment.ServerTestException SERVER_TEST_EXCEPTION = new ServerTestEnvironment.ServerTestException();
//
//  @Test
//  void testCorrectRequestProcessor0() {
//    JsonRpcRequestProcessor processor = assertDoesNotThrow(() -> JsonRpcRequestProcessorFactory
//      .createProcessor(ServerTestEnvironment.TestProcedure0.class, TEST_PROCEDURE_0));
////    JsonRpcResponse response = processor.process(NO_ARG_REQUEST);
////    assertTrue(response instanceof SuccessResponse);
////    assertThat(((SuccessResponse) response).getResult(), equalTo(NO_ARG_RESULT));
////    assertThat(((SuccessResponse) response).getId(), equalTo(ID));
//  }
//
//  public static final NotificationDto NO_ARG_NOTIFICATION = new NotificationDto(ServerTestEnvironment.TestProcedure0.class.getName(),
//    new Object[0]);
//
//  @Test
//  void testCorrectNotificationProcessor0() {
//    JsonRpcRequestProcessor processor = assertDoesNotThrow(() -> JsonRpcRequestProcessorFactory
//      .createProcessor(ServerTestEnvironment.TestProcedure0.class, TEST_PROCEDURE_0));
////    JsonRpcResponse response = processor.process(NO_ARG_NOTIFICATION);
////    assertTrue(response instanceof NotificationResponse);
//  }
//
//  public static final ServerTestEnvironment.TestProcedure0 TEST_INCORRECT_PROCEDURE_0 = () -> {
//    throw SERVER_TEST_EXCEPTION;
//  };
//
//  @Test
//  void testIncorrectRequestProcessor0() {
//    JsonRpcRequestProcessor processor = assertDoesNotThrow(() -> JsonRpcRequestProcessorFactory
//      .createProcessor(ServerTestEnvironment.TestProcedure0.class, TEST_INCORRECT_PROCEDURE_0));
////    JsonRpcResponse response = processor.process(NO_ARG_REQUEST);
////    assertTrue(response instanceof ErrorResponse);
////    assertThat(((ErrorResponse) response).getJsonRpcError().getMessage(),
////      containsString(ServerTestEnvironment.ServerTestException.class.getName()));
//  }
//
//  @Test
//  void testIncorrectNotificationProcessor0() {
//    JsonRpcRequestProcessor processor = assertDoesNotThrow(() -> JsonRpcRequestProcessorFactory
//      .createProcessor(ServerTestEnvironment.TestProcedure0.class, TEST_INCORRECT_PROCEDURE_0));
////    JsonRpcResponse response = processor.process(NO_ARG_NOTIFICATION);
////    assertTrue(response instanceof NotificationResponse);
//  }
//
//  public static final String ONE_ARG_RESULT = "one-arg";
//
//  public static final ServerTestEnvironment.TestProcedure1 TEST_PROCEDURE_1 = arg -> ONE_ARG_RESULT;
//
//  public static final RequestDto ONE_ARG_REQUEST = RequestDto.positional(ID,
//    ServerTestEnvironment.TestProcedure0.class.getName(), new Object[]{"arg"});
//
//  @Test
//  void testCorrectRequestProcessor1() {
//    JsonRemoteProcedureSignature jsonRemoteProcedureSignature = ProcedureScanner.resolveSignature1(ServerTestEnvironment.TestProcedure1.class);
//    JsonRpcRequestProcessor processor = assertDoesNotThrow(() -> JsonRpcRequestProcessorFactory
//      .createProcessor(ServerTestEnvironment.TestProcedure1.class, TEST_PROCEDURE_1));
////    JsonRpcResponse response = processor.process(ONE_ARG_REQUEST);
////    assertTrue(response instanceof SuccessResponse);
////    assertThat(((SuccessResponse) response).getResult(), equalTo(ONE_ARG_RESULT));
////    assertThat(((SuccessResponse) response).getId(), equalTo(ID));
//  }
//
//  public static final NotificationDto ONE_ARG_NOTIFICATION = new NotificationDto(
//    ServerTestEnvironment.TestProcedure0.class.getName(), new Object[]{"arg"});
//
//  @Test
//  void testCorrectNotificationProcessor1() {
//    JsonRpcRequestProcessor processor = assertDoesNotThrow(() -> JsonRpcRequestProcessorFactory
//      .createProcessor(ServerTestEnvironment.TestProcedure1.class, TEST_PROCEDURE_1));
////    JsonRpcResponse response = processor.process(ONE_ARG_NOTIFICATION);
////    assertTrue(response instanceof NotificationResponse);
//  }
//
//  public static final ServerTestEnvironment.TestProcedure1 TEST_INCORRECT_PROCEDURE_1 = arg -> {
//    throw SERVER_TEST_EXCEPTION;
//  };
//
//  @Test
//  void testIncorrectRequestProcessor1() {
//    JsonRpcRequestProcessor processor = assertDoesNotThrow(() -> JsonRpcRequestProcessorFactory
//      .createProcessor(ServerTestEnvironment.TestProcedure1.class, TEST_INCORRECT_PROCEDURE_1));
////    JsonRpcResponse response = processor.process(ONE_ARG_REQUEST);
////    assertTrue(response instanceof ErrorResponse);
////    assertThat(((ErrorResponse) response).getJsonRpcError().getMessage(),
////      containsString(ServerTestEnvironment.ServerTestException.class.getName()));
//  }
//
//  @Test
//  void testIncorrectNotificationProcessor1() {
//    JsonRpcRequestProcessor processor = assertDoesNotThrow(() -> JsonRpcRequestProcessorFactory
//      .createProcessor(ServerTestEnvironment.TestProcedure1.class, TEST_INCORRECT_PROCEDURE_1));
////    JsonRpcResponse response = processor.process(ONE_ARG_NOTIFICATION);
////    assertTrue(response instanceof NotificationResponse);
//  }
//
//  public static final String TWO_ARG_RESULT = "two-arg";
//
//  public static final ServerTestEnvironment.TestProcedure2 TEST_PROCEDURE_2 = (arg1, arg2) -> TWO_ARG_RESULT;
//
//  public static final RequestDto TWO_ARG_REQUEST = RequestDto.positional(ID,
//    ServerTestEnvironment.TestProcedure0.class.getName(), new Object[]{"1", "2"});
//
//  @Test
//  void testCorrectRequestProcessor2() {
//    JsonRpcRequestProcessor processor = assertDoesNotThrow(() -> JsonRpcRequestProcessorFactory
//      .createProcessor(ServerTestEnvironment.TestProcedure2.class, TEST_PROCEDURE_2));
////    JsonRpcResponse response = processor.process(TWO_ARG_REQUEST);
////    assertTrue(response instanceof SuccessResponse);
////    assertThat(((SuccessResponse) response).getResult(), equalTo(TWO_ARG_RESULT));
////    assertThat(((SuccessResponse) response).getId(), equalTo(ID));
//  }
//
//  public static final NotificationDto TWO_ARG_NOTIFICATION = new NotificationDto(
//    ServerTestEnvironment.TestProcedure0.class.getName(), new Object[]{"1", "2"});
//
//  @Test
//  void testCorrectNotificationProcessor2() {
//    JsonRpcRequestProcessor processor = assertDoesNotThrow(() -> JsonRpcRequestProcessorFactory
//      .createProcessor(ServerTestEnvironment.TestProcedure2.class, TEST_PROCEDURE_2));
////    JsonRpcResponse response = processor.process(TWO_ARG_NOTIFICATION);
////    assertTrue(response instanceof NotificationResponse);
//  }
//
//  public static final ServerTestEnvironment.TestProcedure2 TEST_INCORRECT_PROCEDURE_2 = (arg1, arg2) -> {
//    throw SERVER_TEST_EXCEPTION;
//  };
//
//  @Test
//  void testIncorrectRequestProcessor2() {
//    JsonRpcRequestProcessor processor = assertDoesNotThrow(() -> JsonRpcRequestProcessorFactory
//      .createProcessor(ServerTestEnvironment.TestProcedure2.class, TEST_INCORRECT_PROCEDURE_2));
////    JsonRpcResponse response = processor.process(TWO_ARG_REQUEST);
////    assertTrue(response instanceof ErrorResponse);
////    assertThat(((ErrorResponse) response).getJsonRpcError().getMessage(),
////      containsString(ServerTestEnvironment.ServerTestException.class.getName()));
//  }
//
//  @Test
//  void testIncorrectNotificationProcessor2() {
//    JsonRpcRequestProcessor processor = assertDoesNotThrow(() -> JsonRpcRequestProcessorFactory
//      .createProcessor(ServerTestEnvironment.TestProcedure2.class, TEST_INCORRECT_PROCEDURE_2));
////    JsonRpcResponse response = processor.process(TWO_ARG_NOTIFICATION);
////    assertTrue(response instanceof NotificationResponse);
//  }
//
//  public static final String THREE_ARG_RESULT = "three-arg-result";
//
//  public static final ServerTestEnvironment.TestProcedure3 TEST_PROCEDURE_3 = (arg1, arg2, arg3) -> THREE_ARG_RESULT;
//
//  public static final RequestDto THREE_ARG_REQUEST = RequestDto.positional(ID,
//    ServerTestEnvironment.TestProcedure0.class.getName(), new Object[]{"1", "2", "3"});
//
//  @Test
//  void testCorrectRequestProcessor3() {
//    JsonRpcRequestProcessor processor = assertDoesNotThrow(() -> JsonRpcRequestProcessorFactory
//      .createProcessor(ServerTestEnvironment.TestProcedure3.class, TEST_PROCEDURE_3));
////    JsonRpcResponse response = processor.process(THREE_ARG_REQUEST);
////    assertTrue(response instanceof SuccessResponse);
////    assertThat(((SuccessResponse) response).getResult(), equalTo(THREE_ARG_RESULT));
////    assertThat(((SuccessResponse) response).getId(), equalTo(ID));
//  }
//
//  public static final NotificationDto THREE_ARG_NOTIFICATION = new NotificationDto(
//    ServerTestEnvironment.TestProcedure0.class.getName(), new Object[]{"1", "2", "3"});
//
//  @Test
//  void testCorrectNotificationProcessor3() {
//    JsonRpcRequestProcessor processor = assertDoesNotThrow(() -> JsonRpcRequestProcessorFactory
//      .createProcessor(ServerTestEnvironment.TestProcedure3.class, TEST_PROCEDURE_3));
////    JsonRpcResponse response = processor.process(THREE_ARG_NOTIFICATION);
////    assertTrue(response instanceof NotificationResponse);
//  }
//
//  public static final ServerTestEnvironment.TestProcedure3 TEST_INCORRECT_PROCEDURE_3 = (arg1, arg2, arg3) -> {
//    throw SERVER_TEST_EXCEPTION;
//  };
//
//  @Test
//  void testIncorrectRequestProcessor3() {
//    JsonRpcRequestProcessor processor = assertDoesNotThrow(() -> JsonRpcRequestProcessorFactory
//      .createProcessor(ServerTestEnvironment.TestProcedure3.class, TEST_INCORRECT_PROCEDURE_3));
////    JsonRpcResponse response = processor.process(THREE_ARG_REQUEST);
////    assertTrue(response instanceof ErrorResponse);
////    assertThat(((ErrorResponse) response).getJsonRpcError().getMessage(),
////      containsString(ServerTestEnvironment.ServerTestException.class.getName()));
//  }
//
//  @Test
//  void testIncorrectNotificationProcessor3() {
//    JsonRpcRequestProcessor processor = assertDoesNotThrow(() -> JsonRpcRequestProcessorFactory
//      .createProcessor(ServerTestEnvironment.TestProcedure3.class, TEST_INCORRECT_PROCEDURE_3));
////    JsonRpcResponse response = processor.process(THREE_ARG_NOTIFICATION);
////    assertTrue(response instanceof NotificationResponse);
//  }
//
//  public interface IncorrectService /*does not implements procedure*/ {
//
//  }
//
//  @Test
//  void testIncorrectServiceForProcessorCreation() {
//    assertThrows(IllegalArgumentException.class,
//      () -> JsonRpcRequestProcessorFactory.createProcessor(IncorrectService.class, new IncorrectService() {
//      }));
//  }
//
//  @Test
//  void testIncorrectFindCaller() {
//    assertThrows(IllegalArgumentException.class,
//      () -> JsonRpcRequestProcessorFactory.findCaller(new JsonRpcRequestProcessorFactory.NamedMethodHandle[0]));
//  }
//
//  @Test
//  void testCorrectFindCaller() {
//    assertDoesNotThrow(
//      () -> {
//        JsonRpcRequestProcessorFactory.NamedMethodHandle namedMethodHandle =
//          new JsonRpcRequestProcessorFactory.NamedMethodHandle(null, "call", parameterNames, int.class);
//        JsonRpcRequestProcessorFactory.findCaller(new JsonRpcRequestProcessorFactory.NamedMethodHandle[]{namedMethodHandle});
//      });
//  }
//
//  @Test
//  void testIncorrectServiceImplementation() {
//    IncorrectService incorrectService = mock(IncorrectService.class);
//    doThrow(SERVER_TEST_EXCEPTION).when(incorrectService).getClass();
//    assertThrows(IllegalArgumentException.class, () -> JsonRpcRequestProcessorFactory
//      .createProcessor(IncorrectService.class, incorrectService));
//  }
//}

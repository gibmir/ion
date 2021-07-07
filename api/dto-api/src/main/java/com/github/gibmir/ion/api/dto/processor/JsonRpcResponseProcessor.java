package com.github.gibmir.ion.api.dto.processor;

import com.github.gibmir.ion.api.dto.response.transfer.batch.BatchResponseDto;
import com.github.gibmir.ion.api.dto.response.transfer.error.ErrorResponse;
import com.github.gibmir.ion.api.dto.response.transfer.notification.NotificationResponse;
import com.github.gibmir.ion.api.dto.response.transfer.success.SuccessResponse;

public interface JsonRpcResponseProcessor {

  /**
   * Processes error response.
   *
   * @param errorResponse json-rpc error response representation
   */
  default void process(ErrorResponse errorResponse) {
    //do nothing
  }

  /**
   * Processes success response.
   *
   * @param successResponse json-rpc success response representation
   */
  default void process(SuccessResponse successResponse) {
    //do nothing
  }

  /**
   * Processes batch response.
   *
   * @param batchResponseDto json-rpc batch response representation
   */
  default void process(BatchResponseDto batchResponseDto) {
    //do nothing
  }

  /**
   * Processes notification response.
   *
   * @param notificationResponse json-rpc notification response representation
   */
  default void process(NotificationResponse notificationResponse) {
    //do nothing
  }
}

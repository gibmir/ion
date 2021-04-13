package com.github.gibmir.ion.api.dto.processor;

import com.github.gibmir.ion.api.dto.response.transfer.batch.BatchResponseDto;
import com.github.gibmir.ion.api.dto.response.transfer.error.ErrorResponse;
import com.github.gibmir.ion.api.dto.response.transfer.notification.NotificationResponse;
import com.github.gibmir.ion.api.dto.response.transfer.success.SuccessResponse;

public interface JsonRpcResponseProcessor {
  default void process(ErrorResponse errorResponse) {
    //do nothing
  }

  default void process(SuccessResponse successResponse) {
    //do nothing
  }

  default void process(BatchResponseDto batchResponseDto) {
    //do nothing
  }

  default void process(NotificationResponse notificationResponse) {
    //do nothing
  }
}

package com.github.gibmir.ion.api.dto.processor;

import com.github.gibmir.ion.api.dto.response.transfer.error.ErrorResponse;
import com.github.gibmir.ion.api.dto.response.transfer.success.SuccessResponse;

public interface JsonRpcResponseProcessor {
  void process(ErrorResponse errorResponse);

  void process(SuccessResponse successResponse);
}

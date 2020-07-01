package core.environment.mock;

import com.github.gibmir.ion.api.dto.processor.JsonRpcResponseProcessor;
import com.github.gibmir.ion.api.dto.response.transfer.error.ErrorResponse;
import com.github.gibmir.ion.api.dto.response.transfer.success.SuccessResponse;

import java.util.Optional;

public class TestJsonRpcResponseProcessor implements JsonRpcResponseProcessor {
  private ErrorResponse errorResponse;
  private SuccessResponse successResponse;

  @Override
  public void process(ErrorResponse errorResponse) {
    this.errorResponse = errorResponse;
  }

  @Override
  public void process(SuccessResponse successResponse) {
    this.successResponse = successResponse;
  }

  public Optional<ErrorResponse> getErrorResponse() {
    return Optional.ofNullable(errorResponse);
  }

  public Optional<SuccessResponse> getSuccessResponse() {
    return Optional.ofNullable(successResponse);
  }
}

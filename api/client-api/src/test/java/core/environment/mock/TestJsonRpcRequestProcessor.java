package core.environment.mock;

import com.github.gibmir.ion.api.dto.processor.JsonRpcRequestProcessor;
import com.github.gibmir.ion.api.dto.request.transfer.RequestDto;
import com.github.gibmir.ion.api.dto.response.JsonRpcResponse;

import java.util.Optional;

public class TestJsonRpcRequestProcessor implements JsonRpcRequestProcessor {
  private RequestDto requestDto;

  @Override
  public JsonRpcResponse process(RequestDto requestDto) {
    this.requestDto = requestDto;
    return null;
  }

  public Optional<RequestDto> getRequestDto() {
    return Optional.ofNullable(requestDto);
  }
}

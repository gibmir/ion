package com.github.gibmir.ion.lib.netty.server.common.channel.codecs.encoder;

import com.github.gibmir.ion.api.dto.processor.JsonRpcResponseProcessor;
import com.github.gibmir.ion.api.dto.response.JsonRpcResponse;
import com.github.gibmir.ion.api.dto.response.transfer.batch.BatchResponseDto;
import com.github.gibmir.ion.api.dto.response.transfer.error.ErrorResponse;
import com.github.gibmir.ion.api.dto.response.transfer.notification.NotificationResponse;
import com.github.gibmir.ion.api.dto.response.transfer.success.SuccessResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.slf4j.Logger;

import javax.json.bind.Jsonb;
import java.nio.charset.Charset;
import java.util.List;

public final class ResponseEncoder extends MessageToMessageEncoder<JsonRpcResponse> {
  private final Logger logger;
  private final Jsonb jsonb;
  private final Charset charset;

  public ResponseEncoder(final Logger logger, final Jsonb jsonb, final Charset charset) {
    this.logger = logger;
    this.jsonb = jsonb;
    this.charset = charset;
  }

  @Override
  protected void encode(final ChannelHandlerContext ctx, final JsonRpcResponse msg, final List<Object> out) {
    msg.processWith(new JsonRpcResponseProcessor() {
      @Override
      public void process(final ErrorResponse errorResponse) {
        logger.debug("Sending error response for request with id [{}]", errorResponse.getId());
        out.add(jsonb.toJson(errorResponse).getBytes(charset));
      }

      @Override
      public void process(final SuccessResponse successResponse) {
        logger.debug("Sending response for request with id [{}]", successResponse.getId());
        out.add(jsonb.toJson(successResponse).getBytes(charset));
      }

      @Override
      public void process(final BatchResponseDto batchResponseDto) {
        JsonRpcResponse[] responses = batchResponseDto.getJsonRpcResponses();
        logger.debug("Sending batch response with size [{}]", responses.length);
        out.add(jsonb.toJson(responses).getBytes(charset));
      }

      @Override
      public void process(final NotificationResponse notificationResponse) {
        //there is no answer for notification
        logger.trace("Notification was processed");
      }
    });
  }
}

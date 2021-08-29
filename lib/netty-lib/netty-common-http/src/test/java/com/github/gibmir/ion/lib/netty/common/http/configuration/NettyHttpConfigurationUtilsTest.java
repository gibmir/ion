package com.github.gibmir.ion.lib.netty.common.http.configuration;

import com.github.gibmir.ion.api.configuration.Configuration;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class NettyHttpConfigurationUtilsTest {

  @Test
  void testResolveMaxContentLength() {
    Configuration configuration = mock(Configuration.class);
    int expected = 1;
    doAnswer(__ -> Optional.of(expected)).when(configuration)
      .getOptionalValue(eq(NettyHttpConfigurationUtils.NETTY_SERVER_HTTP_MAX_CONTENT_LENGTH), any());
    assertEquals(expected, NettyHttpConfigurationUtils.resolveMaxContentLength(configuration));
  }

  @Test
  void testResolveDefaultMaxContentLength() {
    Configuration configuration = mock(Configuration.class);
    doAnswer(__ -> Optional.empty()).when(configuration)
      .getOptionalValue(eq(NettyHttpConfigurationUtils.NETTY_SERVER_HTTP_MAX_CONTENT_LENGTH), any());
    assertEquals(NettyHttpConfigurationUtils.DEFAULT_AGGREGATOR_MAX_CONTENT_LENGTH,
      NettyHttpConfigurationUtils.resolveMaxContentLength(configuration));
  }
}

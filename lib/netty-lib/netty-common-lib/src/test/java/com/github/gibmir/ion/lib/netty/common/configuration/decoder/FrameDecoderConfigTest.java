package com.github.gibmir.ion.lib.netty.common.configuration.decoder;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

class FrameDecoderConfigTest {
  @Test
  void smoke() {
    int testMaxFrameLength = 0;
    int testLengthFieldOffset = 0;
    int testLengthFieldLength = 0;
    int testLengthAdjustment = 0;
    int testInitialBytesToStrip = 0;
    FrameDecoderConfig frameDecoderConfig = new FrameDecoderConfig(testMaxFrameLength, testLengthFieldOffset,
      testLengthFieldLength, testLengthAdjustment, testInitialBytesToStrip);
    assertThat(frameDecoderConfig.getMaxFrameLength(), equalTo(testMaxFrameLength));
    assertThat(frameDecoderConfig.getLengthFieldOffset(), equalTo(testLengthFieldOffset));
    assertThat(frameDecoderConfig.getLengthFieldLength(), equalTo(testLengthFieldLength));
    assertThat(frameDecoderConfig.getLengthAdjustment(), equalTo(testLengthAdjustment));
    assertThat(frameDecoderConfig.getInitialBytesToStrip(), equalTo(testInitialBytesToStrip));
  }
}

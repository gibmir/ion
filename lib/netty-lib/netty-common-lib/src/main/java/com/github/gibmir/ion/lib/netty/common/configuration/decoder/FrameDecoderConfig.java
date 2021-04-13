package com.github.gibmir.ion.lib.netty.common.configuration.decoder;

public class FrameDecoderConfig {
  private final int maxFrameLength;
  private final int lengthFieldOffset;
  private final int lengthFieldLength;
  private final int lengthAdjustment;
  private final int initialBytesToStrip;

  public FrameDecoderConfig(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength,
                            int lengthAdjustment, int initialBytesToStrip) {
    this.maxFrameLength = maxFrameLength;
    this.lengthFieldOffset = lengthFieldOffset;
    this.lengthFieldLength = lengthFieldLength;
    this.lengthAdjustment = lengthAdjustment;
    this.initialBytesToStrip = initialBytesToStrip;
  }

  public int getMaxFrameLength() {
    return maxFrameLength;
  }

  public int getLengthFieldOffset() {
    return lengthFieldOffset;
  }

  public int getLengthFieldLength() {
    return lengthFieldLength;
  }

  public int getLengthAdjustment() {
    return lengthAdjustment;
  }

  public int getInitialBytesToStrip() {
    return initialBytesToStrip;
  }
}

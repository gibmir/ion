package com.github.gibmir.ion.lib.netty.common.configuration.decoder;

public class FrameDecoderConfig {
  private final int maxFrameLength;
  private final int lengthFieldOffset;
  private final int lengthFieldLength;
  private final int lengthAdjustment;
  private final int initialBytesToStrip;

  public FrameDecoderConfig(final int maxFrameLength, final int lengthFieldOffset, final int lengthFieldLength,
                            final int lengthAdjustment, final int initialBytesToStrip) {
    this.maxFrameLength = maxFrameLength;
    this.lengthFieldOffset = lengthFieldOffset;
    this.lengthFieldLength = lengthFieldLength;
    this.lengthAdjustment = lengthAdjustment;
    this.initialBytesToStrip = initialBytesToStrip;
  }

  /**
   * @return max frame length
   */
  public int getMaxFrameLength() {
    return maxFrameLength;
  }

  /**
   * @return length field offset
   */
  public int getLengthFieldOffset() {
    return lengthFieldOffset;
  }

  /**
   * @return length field length
   */
  public int getLengthFieldLength() {
    return lengthFieldLength;
  }

  /**
   * @return length adjustment
   */
  public int getLengthAdjustment() {
    return lengthAdjustment;
  }

  /**
   * @return initial bytes to strip
   */
  public int getInitialBytesToStrip() {
    return initialBytesToStrip;
  }
}

package com.github.gibmir.ion.lib.netty.client.http.configuration;

public final class HttpResponseDecoderConfiguration {
  private final int maxInitialLineLength;
  private final int maxHeaderSize;
  private final int maxChunkSize;

  public HttpResponseDecoderConfiguration(final int maxInitialLineLength, final int maxHeaderSize,
                                          final int maxChunkSize) {
    this.maxInitialLineLength = maxInitialLineLength;
    this.maxHeaderSize = maxHeaderSize;
    this.maxChunkSize = maxChunkSize;
  }

  /**
   * @return max initial line length
   */
  public int getMaxInitialLineLength() {
    return maxInitialLineLength;
  }

  /**
   * @return max header size
   */
  public int getMaxHeaderSize() {
    return maxHeaderSize;
  }

  /**
   * @return max chunk size
   */
  public int getMaxChunkSize() {
    return maxChunkSize;
  }
}

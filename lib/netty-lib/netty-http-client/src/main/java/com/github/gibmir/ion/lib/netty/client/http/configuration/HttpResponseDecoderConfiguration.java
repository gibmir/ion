package com.github.gibmir.ion.lib.netty.client.http.configuration;

public class HttpResponseDecoderConfiguration {
  private final int maxInitialLineLength;
  private final int maxHeaderSize;
  private final int maxChunkSize;

  public HttpResponseDecoderConfiguration(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize) {
    this.maxInitialLineLength = maxInitialLineLength;
    this.maxHeaderSize = maxHeaderSize;
    this.maxChunkSize = maxChunkSize;
  }

  public int getMaxInitialLineLength() {
    return maxInitialLineLength;
  }

  public int getMaxHeaderSize() {
    return maxHeaderSize;
  }

  public int getMaxChunkSize() {
    return maxChunkSize;
  }
}

package com.github.gibmir.ion.lib.nio.common.pool.reader;

import com.github.gibmir.ion.lib.nio.common.pool.ByteBufferPool;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;

public class ByteBufferReader {
  private final ByteBufferPool readerPool;

  public ByteBufferReader(ByteBufferPool readerPool) {
    this.readerPool = readerPool;
  }

  public byte[] readFrom(ReadableByteChannel channel)
    throws InterruptedException, IOException {
    ByteBuffer buffer = readerPool.takeFromPool();
    int readCount = channel.read(buffer);
    byte[] request = new byte[readCount];
    for (int i = 0; i < request.length; i++) {
      request[i] = buffer.get(i);
    }
    buffer.clear();
    readerPool.returnToPool(buffer);
    return request;
  }
}

package com.github.gibmir.ion.lib.nio.common.pool.writer;

import com.github.gibmir.ion.lib.nio.common.pool.ByteBufferPool;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ByteBufferWriter {
  private final ByteBufferPool writerPool;

  public ByteBufferWriter(ByteBufferPool writerPool) {
    this.writerPool = writerPool;
  }

  public void writeInto(byte[] payload, SocketChannel channel)
    throws InterruptedException, IOException {
    ByteBuffer byteBuffer = writerPool.takeFromPool();
    byteBuffer.put(payload);
    byteBuffer.flip();
    channel.write(byteBuffer);
    byteBuffer.flip();
    byteBuffer.clear();
    writerPool.returnToPool(byteBuffer);
  }
}

package com.github.gibmir.ion.lib.nio.common.pool;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class ByteBufferPool {
  private final BlockingQueue<ByteBuffer> pool;
  private final int size;

  public ByteBufferPool(int poolSize, int bufferCapacity) {
    this.size = poolSize;
    List<ByteBuffer> byteBuffers = new ArrayList<>(poolSize);
    for (int i = 0; i < poolSize; i++) {
      byteBuffers.add(ByteBuffer.allocateDirect(bufferCapacity));
    }
    this.pool = new LinkedBlockingDeque<>(poolSize);
    pool.addAll(byteBuffers);
  }

  public ByteBuffer takeFromPool() throws InterruptedException {
    return pool.take();
  }

  public void returnToPool(ByteBuffer byteBuffer) {
    pool.add(byteBuffer);
  }

  public int getSize() {
    return size;
  }
}

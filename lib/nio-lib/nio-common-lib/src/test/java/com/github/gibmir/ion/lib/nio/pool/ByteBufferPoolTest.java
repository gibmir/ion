package com.github.gibmir.ion.lib.nio.pool;

import com.github.gibmir.ion.lib.nio.common.pool.ByteBufferPool;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

public class ByteBufferPoolTest {
  @Test
  void smoke() throws InterruptedException {
    ByteBufferPool byteBufferPool = new ByteBufferPool(3, 1);
    ByteBuffer byteBuffer1 = byteBufferPool.takeFromPool();
    System.out.println(byteBuffer1);
    ByteBuffer byteBuffer2 = byteBufferPool.takeFromPool();
    System.out.println(byteBuffer1);
    ByteBuffer byteBuffer3 = byteBufferPool.takeFromPool();
    System.out.println(byteBuffer1);
    ByteBuffer byteBuffer4 = byteBufferPool.takeFromPool();
    System.out.println(byteBuffer1);
  }
}

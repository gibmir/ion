package com.github.gibmir.ion.lib.nio.client.sender.selector;

import com.github.gibmir.ion.api.client.callback.JsonRpcResponseCallback;
import com.github.gibmir.ion.api.client.context.RequestContext;
import com.github.gibmir.ion.lib.nio.common.pool.reader.ByteBufferReader;
import com.github.gibmir.ion.lib.nio.common.pool.writer.ByteBufferWriter;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

public class IonClientSelector implements AutoCloseable, Runnable {
  private final Selector selector;
  private final BlockingQueue<byte[]> requestPayloadQueue;
  private final Map<SelectionKey, JsonRpcResponseCallback> asyncCallbackCache;
  private final ByteBufferReader byteBufferReader;
  private final ByteBufferWriter byteBufferWriter;

  public static IonClientSelector open(BlockingQueue<byte[]> payloadQueue, ByteBufferReader byteBufferReader,
                                       ByteBufferWriter byteBufferWriter,
                                       Map<SelectionKey, JsonRpcResponseCallback> asyncCallbackCache) {
    try {
      Selector selector = Selector.open();
      return new IonClientSelector(selector, payloadQueue, byteBufferReader, byteBufferWriter, asyncCallbackCache);
    } catch (IOException e) {
      throw new ExceptionInInitializerError("Couldn't open selector." + e.getMessage());
    }
  }

  private IonClientSelector(Selector selector, BlockingQueue<byte[]> requestPayloadQueue, ByteBufferReader byteBufferReader,
                            ByteBufferWriter byteBufferWriter, Map<SelectionKey, JsonRpcResponseCallback> asyncCallbackCache) {
    this.selector = selector;
    this.requestPayloadQueue = requestPayloadQueue;
    this.asyncCallbackCache = asyncCallbackCache;
    this.byteBufferReader = byteBufferReader;
    this.byteBufferWriter = byteBufferWriter;
  }

  public void register(SocketChannel channel) throws IOException {
    channel.configureBlocking(false);
    channel.register(selector, SelectionKey.OP_CONNECT);
    SelectionKey key = channel.keyFor(selector);
    key.interestOps(SelectionKey.OP_WRITE);
    selector.wakeup();
  }

  public void sendRequest(RequestContext requestContext, JsonRpcResponseCallback callback, SocketChannel channel) {
    SelectionKey selectionKey = channel.keyFor(selector);
    selectionKey.attach(requestContext.getId());
    selectionKey.interestOps(SelectionKey.OP_WRITE);
    requestPayloadQueue.add(requestContext.getPayload());
    asyncCallbackCache.put(selectionKey, callback);
    selector.wakeup();
  }

  @Override
  public void close() throws Exception {
    selector.close();
  }

  @Override
  public void run() {
    while (true) {
      runClient();
    }
  }

  private void runClient() {
    try {
      //await available channels
      selector.select();
      Set<SelectionKey> selectionKeys = selector.selectedKeys();
      Iterator<SelectionKey> iterator = selectionKeys.iterator();
      while (iterator.hasNext()) {
        SelectionKey key = iterator.next();
        if (key.isConnectable()) {
          connect(key);
        } else if (key.isReadable()) {
          readResponse(key);
        } else if (key.isWritable()) {
          writeRequest(key);
        }
        iterator.remove();
      }
    } catch (IOException exception) {

    }
  }

  private void connect(SelectionKey key) {
    try {
      SocketChannel socketChannel = (SocketChannel) key.channel();
      socketChannel.finishConnect();
      key.interestOps(SelectionKey.OP_WRITE);
    } catch (IOException e) {
      exceptionally(key, e);
    }
  }

  private void writeRequest(SelectionKey selectionKey) {
    try {
      byte[] requestPayload = requestPayloadQueue.poll();
      if (requestPayload != null) {
        byteBufferWriter.writeInto(requestPayload, (SocketChannel) selectionKey.channel());
        selectionKey.interestOps(SelectionKey.OP_READ);
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      exceptionally(selectionKey, e);
    } catch (Throwable e) {
      exceptionally(selectionKey, e);
    }
  }

  private void readResponse(SelectionKey key) {
    try {
      byte[] response = byteBufferReader.readFrom((SocketChannel) key.channel());
      asyncCallbackCache.computeIfPresent(key, (__, callback) -> {
        callback.onComplete(response, null);
        return /*remove key from map*/null;
      });
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      exceptionally(key, e);
    } catch (Throwable e) {
      exceptionally(key, e);
    }
  }

  private void exceptionally(SelectionKey key, Throwable e) {
    //todo compute with logging
    asyncCallbackCache.computeIfPresent(key, (__, callback) -> {
      callback.onComplete(null, e);
      return null;
    });
  }
}

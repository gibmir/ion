package com.github.gibmir.ion.lib.nio.server;

import com.github.gibmir.ion.lib.nio.common.pool.reader.ByteBufferReader;
import com.github.gibmir.ion.lib.nio.common.pool.writer.ByteBufferWriter;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

public class NioServer implements Runnable {
  private final Processor processor;
  private final ServerSocketChannel serverSocketChannel;
  private final Selector selector;
  private final ByteBufferReader byteBufferReader;
  private final ByteBufferWriter byteBufferWriter;
  private final BlockingQueue<byte[]> responseQueue = new LinkedBlockingDeque<>();
  private final Set<Channel> registry = ConcurrentHashMap.newKeySet();

  public NioServer(Processor processor, ServerSocketChannel serverSocketChannel, Selector selector,
                   ByteBufferReader byteBufferReader, ByteBufferWriter byteBufferWriter) {
    this.processor = processor;
    this.serverSocketChannel = serverSocketChannel;
    this.selector = selector;
    this.byteBufferReader = byteBufferReader;
    this.byteBufferWriter = byteBufferWriter;
  }

  public static NioServer create(SocketAddress socketAddress, Processor processor,
                                 ByteBufferReader byteBufferReader, ByteBufferWriter byteBufferWriter) throws IOException {
    Selector selector = Selector.open();
    ServerSocketChannel channel = ServerSocketChannel.open();
    channel.configureBlocking(false);
    channel.socket().bind(socketAddress);
    channel.register(selector, SelectionKey.OP_ACCEPT);

    return new NioServer(processor, channel, selector, byteBufferReader, byteBufferWriter);
  }

  @Override
  public void run() {
    while (true) {
      try {
        selector.select();
        Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
        while (keyIterator.hasNext()) {
          SelectionKey key = keyIterator.next();
          if (!key.isValid()) {
            continue;
          }
          if (key.isAcceptable()) {
            accept(key);
          } else if (key.isReadable()) {
            readRequest(key);
          } else if (key.isWritable()) {
            writeResponse(key);
          }
          keyIterator.remove();
        }
      } catch (IOException e) {
        System.out.println(e);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
  }

  private void accept(SelectionKey key) throws IOException {
    ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
    SocketChannel channel = serverChannel.accept();
    channel.configureBlocking(false);
    Socket socket = channel.socket();
    SocketAddress remoteSocketAddress = socket.getRemoteSocketAddress();
    System.out.println("Connected to: " + remoteSocketAddress);

    // register channel with selector for further IO
    channel.register(selector, SelectionKey.OP_READ);
    registry.add(channel);
  }

  private void readRequest(SelectionKey key) throws IOException, InterruptedException {
    SocketChannel channel = (SocketChannel) key.channel();
    byte[] request = byteBufferReader.readFrom(channel);
    processor.process(request, (responsePayload -> {
      try {
        responseQueue.put(responsePayload);
        SelectionKey selectionKey = channel.keyFor(selector);
        selectionKey.interestOps(SelectionKey.OP_WRITE);
        selector.wakeup();
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }));

  }

  private void writeResponse(SelectionKey key) throws InterruptedException, IOException {
    SocketChannel channel = (SocketChannel) key.channel();
    byte[] responsePayload = responseQueue.take();
    byteBufferWriter.writeInto(responsePayload, channel);
  }

}

package com.github.gibmir.ion.lib.netty.common.configuration.group;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class NettyGroupTypeTest {
  @Test
  void smoke() {
    NettyGroupType nio = NettyGroupType.valueOf("NIO");
    assertThat(nio, is(NettyGroupType.NIO));

    NettyGroupType epoll = NettyGroupType.valueOf("EPOLL");
    assertThat(epoll, is(NettyGroupType.EPOLL));
  }
}

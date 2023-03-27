package com.wgcloud.common;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {
   public static void start(int port) throws InterruptedException {
      EventLoopGroup bossGroup = new NioEventLoopGroup();
      NioEventLoopGroup workerGroup = new NioEventLoopGroup();

      try {
         ServerBootstrap serverBootstrap = new ServerBootstrap();
         ((ServerBootstrap)serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)).childHandler(new NettyServerInitializer());
         ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
         channelFuture.channel().closeFuture().sync();
      } finally {
         bossGroup.shutdownGracefully();
         workerGroup.shutdownGracefully();
      }

   }
}

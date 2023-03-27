package com.wgcloud.common;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

//import java.nio.channels.SocketChannel;

public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {
   protected void initChannel(SocketChannel socketChannel) throws Exception {
      ChannelPipeline pipeline = socketChannel.pipeline();
      pipeline.addLast(new ChannelHandler[]{new HttpServerCodec()});
      pipeline.addLast(new ChannelHandler[]{new ChunkedWriteHandler()});
      pipeline.addLast(new ChannelHandler[]{new HttpObjectAggregator(63488)});
      pipeline.addLast(new ChannelHandler[]{new WebSocketServerProtocolHandler("/ws")});
      pipeline.addLast(new ChannelHandler[]{new NettytHandler()});
   }
}

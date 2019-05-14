package game.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * Created by Emre Erinç
 * Istanbul University - 13016150016
 */
public class ChatClientInitializer extends ChannelInitializer<SocketChannel> {
  @Override
  protected void initChannel(SocketChannel socketChannel) throws Exception {
    ChannelPipeline channelPipeline = socketChannel.pipeline();

    //channelPipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
    channelPipeline.addLast("decoder", new StringDecoder());
    channelPipeline.addLast("encoder", new StringEncoder());
    channelPipeline.addLast("handler", new ChatClientHandler());
  }
}

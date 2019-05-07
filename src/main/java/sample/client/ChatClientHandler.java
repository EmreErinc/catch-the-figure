package sample.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by emre on 28.04.2019
 */
//public class ChatClientHandler extends SimpleChannelInboundHandler<String> {
//
//  String message = "";
//
//  @Override
//  protected void channelRead0(ChannelHandlerContext ctx, String message) throws Exception {
//    /*if (message.contains("Server Time")) {
//      String time = message.split(Pattern.quote(" : "))[1];
//
//      System.out.println(Instant.now().toString() + " -------------- " + time);
//    }*/
//
//    this.message = message;
//    System.out.println(message);
//  }
//}
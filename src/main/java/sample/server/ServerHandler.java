package sample.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.DefaultEventExecutor;
import sample.client.Client;
import sample.client.RectType;

import java.net.SocketAddress;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

/**
 * Created by emre on 28.04.2019
 */
public class ServerHandler extends SimpleChannelInboundHandler<String> implements ChannelHandler {
  private static final ChannelGroup CHANNELS = new DefaultChannelGroup(new DefaultEventExecutor());

  private static List<Client> clients = new ArrayList<Client>();

  @Override
  public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
    Channel incoming = ctx.channel();

    // TODO server time - client time

    incoming.writeAndFlush("[SYS] - " + "WELCOME TO LOBBY!\r\n");
    incoming.writeAndFlush("[SYS] - " + "YOUR INFO : " + incoming.remoteAddress() + "\r\n");
    incoming.writeAndFlush("[SYS] - Server Time : " + Instant.now() + "\r\n");
    for (Channel channel : CHANNELS) {
      channel.writeAndFlush("[SYS] - A new user has joined\n");
    }
    CHANNELS.add(ctx.channel());
    log.print("[SYS] - A new user has joined on <<" + incoming.remoteAddress() + ">>\n");
  }

  @Override
  public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
    Channel incoming = ctx.channel();
    for (Channel channel : CHANNELS) {
      channel.writeAndFlush("[SYS] - <<" + getUserByRemoteAddress(incoming.remoteAddress()) + ">> has left\n");
    }
    removeUser(incoming);
    CHANNELS.remove(ctx.channel());
    log.print("[SYS] - <<" + getUserByRemoteAddress(incoming.remoteAddress()) + ">> has left from <<" + incoming.remoteAddress() + ">>\n");
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, String text) throws Exception {
    Channel incoming = ctx.channel();

    String command = text.substring(1,4);
    switch (command) {
      case "USR":
        addUser(incoming, text);
        break;
      case "MSG":
        if (text.contains("-ls")) {
          listChannels(incoming);
        }
        else {
          sendMessage(incoming, text);
        }
        break;
      case "CMD":
        sendClickInfo(incoming, parseRectType(text), parseClickTime(text));
      default:
        break;
    }
  }

  private void sendMessage(Channel incoming, String text) {
    String message = parseMessage(text);
    if (!message.equals("EMPTY_MESSAGE")) {
      for (Channel channel : CHANNELS) {
        if (channel.equals(incoming)) {
          channel.writeAndFlush("[MSG] - <<YOU>> " + message + "\n");
        } else {
          channel.writeAndFlush("[MSG] - <<" + getUserByRemoteAddress(incoming.remoteAddress()) + ">> " + message + "\n");
        }
      }
      log.print("[LOG] - <<" + incoming.remoteAddress() + ">> | <<" + getUserByRemoteAddress(incoming.remoteAddress()) + ">> - [MSG] : " + message + "\n");
    }
  }

  private void sendClickInfo(Channel incoming, RectType rectType, long clickTime) {
    for (Channel channel : CHANNELS){
      if (channel.equals(incoming)) {
        //TODO coordinate detayı da verilebilir
        channel.writeAndFlush("[CMD] - <<YOU>> Clicked : " + rectType + " at : " + clickTime + "\n");
      } else {
        channel.writeAndFlush("[CMD] - <<" + getUserByRemoteAddress(incoming.remoteAddress()) + ">> Clicked : " + rectType + " at : " + clickTime + "\n");
      }
    }
    log.print("[LOG] - <<" + incoming.remoteAddress() + ">> | <<" + getUserByRemoteAddress(incoming.remoteAddress()) + ">> - Clicked : " + rectType + " at : " + clickTime +"\n");
  }

  private Long parseClickTime(String text){
    return Long.valueOf(text.split(Pattern.quote("<<"))[1].split(Pattern.quote(">>"))[0]);
  }

  private RectType parseRectType(String text){
    return RectType.valueOf(text.split(Pattern.quote("<<"))[2].split(Pattern.quote(">>"))[0]);
  }

  private void addUser(Channel incoming, String text) {
    if (clients.stream().noneMatch(client -> client.getChannel().equals(incoming))) {
      String nick = parseNick(text);

      Client client = new Client();
      client.setId(Instant.now().getEpochSecond());
      client.setNick(nick);
      client.setChannel(incoming);
      clients.add(client);
    }
  }

  private void removeUser(Channel incoming){
    if (clients.stream().anyMatch(client -> client.getChannel().equals(incoming))) {
      Client removedUser = clients.stream().filter(client -> client.getChannel().equals(incoming)).findFirst().get();
      clients.remove(removedUser);
    }
  }

  private void listChannels(Channel incoming) {
    for (Channel channel : CHANNELS) {
      if (channel.equals(incoming)) {
        channel.writeAndFlush(stringifyUserList());
      }
    }
  }

  private String stringifyUserList(){
    StringBuilder users = new StringBuilder();
    users.append("Connected User List : \n");
    int userCount = 1;
    for (Client client : clients) {
      users.append(userCount++)
          .append(" - ")
          .append(getUserByRemoteAddress(client.getChannel().remoteAddress()))
          .append("\r\n");
    }
    return users.toString();
  }

  private String parseMessage(String text) {
    if (text.contains("[MSG]")) {
      return text.split(Pattern.quote("[MSG]"))[1].split(Pattern.quote("\r\n"))[0];
    }
    return "EMPTY_MESSAGE";
  }

  private String parseNick(String text) {
    return text.split(Pattern.quote("[USR]"))[1].split(Pattern.quote("\r\n"))[0];
  }

  private String getUserByRemoteAddress(SocketAddress socketAddress) {
    for (Client client : clients) {
      if (client.getChannel().remoteAddress().equals(socketAddress)) {
        return client.getNick();
      }
    }
    return "UNDEFINED_USER";
  }
}
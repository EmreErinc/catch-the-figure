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
import sample.client.Utils;

import java.net.SocketAddress;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

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

    incoming.writeAndFlush("[SYS] - " + "WELCOME TO LOBBY!\r\n" +
        "[SYS] - " + "YOUR INFO : " + incoming.remoteAddress() + "\r\n" +
        "[SYS] - Server Time : " + Instant.now() + "\r\n" +
        "[SYS] - Client Role : " + ((clients.size() == 0) ? "<<HOST>>" : "<<SUBSCRIBER>>") +"\r\n");
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

    String command = text.substring(1, 4);
    switch (command) {
      case "USR":
        addUser(incoming, text);
        break;
      case "MSG":
        if (text.contains("-ls")) {
          listChannels(incoming);
        } else {
          sendMessage(incoming, text);
        }
        break;
      case "CMD":
        if (text.contains("CREATED")) {
          createShape(incoming, Utils.parseRectTypeForServer(text), Utils.parseColorStr(text), Utils.parseBoundsStr(text), Utils.parseFigureCreatedAt(text));
        }
        if (text.contains("CLICKED")){
          sendClickInfo(incoming, Utils.parseRectTypeForServer(text), Utils.parseClickTime(text), Utils.parseFigureCreatedAtOnClick(text));
        }
        if (text.contains("start-game")){
          startGame(incoming);
        }
      default:
        break;
    }
  }

  private void startGame(Channel incoming){
    for (Channel channel : CHANNELS){
      if (channel.equals(incoming)){
        channel.writeAndFlush("[CMD] - <<YOU>> Started game");
      }else {
        channel.writeAndFlush("[CMD] - <<" + getUserByRemoteAddress(incoming.remoteAddress()) + ">> Started game");
      }
    }
    log.print("[LOG] - <<" + incoming.remoteAddress() + ">> | - <<" + getUserByRemoteAddress(incoming.remoteAddress()) + ">> Started game");
  }

  private void createShape(Channel incoming, RectType type, String color, String bounds, String createdAt) {
    for (Channel channel : CHANNELS) {
      if (channel.equals(incoming)) {
        channel.writeAndFlush("[CMD] - <<YOU>> CREATED " +
            "| Type : <<" + type + ">> " +
            "| Color : <<" + color + ">> " +
            "| Bounds : <<" + bounds + ">> " +
            "| FigureCreation : <<" + createdAt + ">> \r\n");
      } else {
        channel.writeAndFlush("[CMD] - <<" + getUserByRemoteAddress(incoming.remoteAddress()) + ">> CREATED " +
            "| Type : <<" + type + ">> " +
            "| Color : <<" + color + ">> " +
            "| Bounds : <<" + bounds + ">> " +
            "| FigureCreation : <<" + createdAt + ">> \r\n");
      }
    }
    log.print("[LOG] - <<" + incoming.remoteAddress() + ">> " +
        "| <<" + getUserByRemoteAddress(incoming.remoteAddress()) + ">> - CREATED " +
        "| Type : <<" + type + ">> " +
        "| Color : <<" + color + ">> " +
        "| Bounds : <<" + bounds + ">> " +
        "| FigureCreation : <<" + createdAt + ">> \r\n");
  }

  private void sendClickInfo(Channel incoming, RectType rectType, long clickTime, String figureCreation) {
    for (Channel channel : CHANNELS) {
      if (channel.equals(incoming)) {
        channel.writeAndFlush("[CMD] - <<YOU>> CLICKED : <<" + rectType + ">> " +
            "| Time : <<" + clickTime + ">> " +
            "| FigureCreation : <<" + figureCreation + ">>\n");
      } else {
        channel.writeAndFlush("[CMD] - <<" + getUserByRemoteAddress(incoming.remoteAddress()) + ">> CLICKED : <<" + rectType + ">> " +
            "| Time : <<" + clickTime + ">> " +
            "| FigureCreation : <<" + figureCreation + ">>\n");
      }
    }
    log.print("[LOG] - <<" + incoming.remoteAddress() + ">> | <<" + getUserByRemoteAddress(incoming.remoteAddress()) + ">> - CLICKED : " + rectType + " at : " + clickTime + "\n");
  }

  private void sendMessage(Channel incoming, String text) {
    String message = Utils.parseMessage(text);
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

  private void addUser(Channel incoming, String text) {
    if (clients.stream().noneMatch(client -> client.getChannel().equals(incoming))) {
      String nick = Utils.parseNick(text);

      Client client = new Client();
      client.setId(Instant.now().getEpochSecond());
      client.setNick(nick);
      client.setChannel(incoming);
      clients.add(client);
    }
  }

  private void removeUser(Channel incoming) {
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

  private String stringifyUserList() {
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


  private String getUserByRemoteAddress(SocketAddress socketAddress) {
    for (Client client : clients) {
      if (client.getChannel().remoteAddress().equals(socketAddress)) {
        return client.getNick();
      }
    }
    return "UNDEFINED_USER";
  }
}
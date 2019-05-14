package game.server;

import game.client.Client;
import game.client.RectType;
import game.client.Utils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.DefaultEventExecutor;

import java.net.SocketAddress;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

/**
 * Created by Emre Erinç
 * Istanbul University - 13016150016
 */
public class ServerHandler extends SimpleChannelInboundHandler<String> {
  private static final ChannelGroup CHANNELS = new DefaultChannelGroup(new DefaultEventExecutor());

  private static List<Client> clients = new ArrayList<>();
  private static List<FigureClick> figureClicks = new ArrayList<>();
  private static int figureCount = 0;
  private static int clickedShapeCount = 0;
  private static List<GameResult> results = new ArrayList<>();

  @Override
  public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
    Channel incoming = ctx.channel();

    // TODO server time - client time

    incoming.writeAndFlush("[SYS] - " + "WELCOME TO LOBBY!\r\n" +
        "[SYS] - " + "YOUR INFO : " + incoming.remoteAddress() + "\r\n" +
        "[SYS] - Server Time : " + Instant.now() + "\r\n" +
        "[SYS] - Client Role : " + ((clients.size() == 0) ? "<<HOST>>" : "<<SUBSCRIBER>>") + "\r\n");
    for (Channel channel : CHANNELS) {
      channel.writeAndFlush("[SYS] - A new user has joined\n");
    }
    CHANNELS.add(ctx.channel());
    log.print("[SYS] - A new user has joined on <<" + incoming.remoteAddress() + ">>\n");
  }

  @Override
  public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
    String nick = getUserByRemoteAddress(ctx.channel().remoteAddress());

    Channel incoming = ctx.channel();
    for (Channel channel : CHANNELS) {
      channel.writeAndFlush("[SYS] - <<" + nick + ">> has left\n");
    }
    removeUser(incoming);
    CHANNELS.remove(ctx.channel());
    log.print("[SYS] - <<" + nick + ">> has left from <<" + incoming.remoteAddress() + ">>\n");
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
        if (text.contains("CLICKED")) {
          sendClickInfo(incoming, Utils.parseClickTime(text), Utils.parseFigureCreatedAtOnClick(text));
        }
        if (text.contains("START")) {
          startGame(incoming, Utils.parseIntervalForServer(text), Utils.parseXForServer(text), Utils.parseYForServer(text), Utils.parseShapeCountForServer(text), Utils.parsePointsForServer(text));
        }
        if (text.contains("RESULT")) {
          collectResult(incoming, Utils.parsePoints(text));
        }
      default:
        break;
    }
  }

  private void collectResult(Channel incoming, int points) {
    GameResult gameResult = new GameResult();
    gameResult.nick = getUserByRemoteAddress(incoming.remoteAddress());
    gameResult.points = points;
    gameResult.socketAddress = incoming.remoteAddress();

    if (results.stream().noneMatch(result -> result.socketAddress.equals(incoming.remoteAddress()))) {
      results.add(gameResult);
    }
    results.sort(Comparator.comparing(result -> result.points));

    if (results.size() == CHANNELS.size()) {
      StringBuilder result = new StringBuilder();
      for (int i = 0; i < results.size(); i++) {
        result.append(i + 1)
            .append(" - ")
            .append(results.get(results.size() - i - 1).nick)
            .append("\t\t")
            .append(results.get(results.size() - i - 1).points)
            .append("\n");
      }
      CHANNELS.writeAndFlush("[SYS] - RESULT - \n" + result);
    }
  }

  private void startGame(Channel incoming, String interval, String x, String y, String figureCount, String points) {
    this.figureCount = Integer.valueOf(figureCount);
    for (Channel channel : CHANNELS) {
      channel.writeAndFlush("[SYS] - <<" + (channel.equals(incoming) ? "YOU" : getUserByRemoteAddress(incoming.remoteAddress())) + ">> STARTED " +
          "| Interval : <<" + interval + ">> " +
          "| X : <<" + x + ">> " +
          "| Y : <<" + y + ">> " +
          "| FigureCount : <<" + figureCount + ">> " +
          "| Points : <<" + points + ">>\r\n");
    }
    log.print("[LOG] - <<" + incoming.remoteAddress() + ">> | <<" + getUserByRemoteAddress(incoming.remoteAddress()) + ">> - STARTED " +
        "| Interval : <<" + interval + ">> " +
        "| X : <<" + x + ">> " +
        "| Y : <<" + y + ">> " +
        "| FigureCount : <<" + figureCount + ">> " +
        "| Points : <<" + points + ">>\r\n");
  }

  private void createShape(Channel incoming, RectType type, String color, String bounds, String createdAt) {
    for (Channel channel : CHANNELS) {
      channel.writeAndFlush("[SYS] - <<" + (channel.equals(incoming) ? "YOU" : getUserByRemoteAddress(incoming.remoteAddress())) + ">> CREATED " +
          "| Type : <<" + type + ">> " +
          "| Color : <<" + color + ">> " +
          "| Bounds : <<" + bounds + ">> " +
          "| FigureCreation : <<" + createdAt + ">> \r\n");
    }
    log.print("[LOG] - <<" + incoming.remoteAddress() + ">> " +
        "| <<" + getUserByRemoteAddress(incoming.remoteAddress()) + ">> - CREATED " +
        "| Type : <<" + type + ">> " +
        "| Color : <<" + color + ">> " +
        "| Bounds : <<" + bounds + ">> " +
        "| FigureCreation : <<" + createdAt + ">> \r\n");
  }

  private void sendClickInfo(Channel incoming, long clickTime, String figureCreation) {
    if (figureClicks.stream().noneMatch(figureClick -> figureClick.rectCreatedAt.equals(figureCreation))) {
      FigureClick figureClick = new FigureClick();
      figureClick.clickTime = clickTime;
      figureClick.rectCreatedAt = figureCreation;
      figureClick.clickOwner = incoming.remoteAddress();
      figureClicks.add(figureClick);

      clickedShapeCount++;

      for (Channel channel : CHANNELS) {
        channel.writeAndFlush("[SYS] - <<" + (channel.equals(incoming) ? "YOU" : getUserByRemoteAddress(incoming.remoteAddress())) + ">> CLICKED " +
            "| Time : <<" + clickTime + ">> " +
            "| FigureCreation : <<" + figureCreation + ">>\n");
      }
    } else {
      for (Channel channel : CHANNELS) {
        if (channel.equals(incoming)) {
          channel.writeAndFlush("[SYS] - ALREADY_CLICKED\n");
        }
      }
    }
    if (clickedShapeCount == figureCount) {
      CHANNELS.writeAndFlush("[SYS] - COLLECT");
    }
    log.print("[LOG] - <<" + incoming.remoteAddress() + ">> | <<" + getUserByRemoteAddress(incoming.remoteAddress()) + ">> - CLICKED " +
        "| Time : <<" + clickTime + ">> " +
        "| FigureCreation : <<" + figureCreation + ">>\n");
  }

  private void sendMessage(Channel incoming, String text) {
    String message = Utils.parseMessage(text);
    if (!message.equals("EMPTY_MESSAGE")) {
      for (Channel channel : CHANNELS) {
        channel.writeAndFlush("[MSG] - <<" + (channel.equals(incoming) ? "YOU" : getUserByRemoteAddress(incoming.remoteAddress())) + ">> " + message + "\n");
      }
      log.print("[LOG] - <<" + incoming.remoteAddress() + ">> | <<" + getUserByRemoteAddress(incoming.remoteAddress()) + ">> - SEND : " + message + "\n");
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
package sample.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by emre on 28.04.2019
 */
public class ChatClient {
  private final String host;
  private final int port;
  private static boolean registered = false;

  private static String nick = "";

  public ChatClient(String host, int port) {
    this.host = host;
    this.port = port;
  }

  private static void createAndShowGameArea() throws IOException {
    GameArea game = new GameArea();
    game.setVisible(true);
    Runtime.getRuntime().exec("java -cp ./target/classes:. sample.client.GameArea"); //öncesinde 'javac filename.java' demek gerekiyor
  }

  public static void main(String[] args) throws IOException {
    if (!registered) {
      System.out.println("Kullanıcı Adınızı Girin : ");
      BufferedReader nickInput = new BufferedReader(new InputStreamReader(System.in));
      nick = nickInput.readLine();
    }

    new ChatClient("localhost", 8000).run();
  }

  public void run() {
    EventLoopGroup group = new NioEventLoopGroup();

    try {
      Bootstrap bootstrap = new Bootstrap()
          .group(group)
          .channel(NioSocketChannel.class)
          .handler(new ChatClientInitializer());

      Channel channel = bootstrap.connect(host, port).sync().channel();

      if (!registered) {
        registered = true;
        channel.writeAndFlush("[USR]" + nick + "\r\n");
      }

      BufferedReader input = new BufferedReader(new InputStreamReader(System.in));


      // TODO add custom options
      if (input.readLine().equals("start-game")) {
        SwingUtilities.invokeLater(() -> {
          try {
            createAndShowGameArea();
          } catch (IOException e) {
            e.printStackTrace();
          }
        });

      }

      StringBuilder messageBuilder = new StringBuilder();
      messageBuilder.append("[MSG]");

      // TODO mesaj ve command ayrımı yapılacak

      while (true) {
        channel.writeAndFlush("[MSG]" + input.readLine() + "\r\n");
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      group.shutdownGracefully();
    }
  }
}
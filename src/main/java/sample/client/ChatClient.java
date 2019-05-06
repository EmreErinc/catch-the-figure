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
//  private final String host;
//  private final int port;
//  private static boolean registered = false;
//
//  private static String nick = "";
//
//  public ChatClient(String host, int port) {
//    this.host = host;
//    this.port = port;
//  }
//
//  private static void createAndShowGameArea(int bound) throws IOException {
//    //ClientArea game = new ClientArea();
//    //ClientArea game = new ClientArea(bound);
//    //game.setVisible(true);
//    //Runtime.getRuntime().exec("javac ./src/main/java/sample/client/ClientArea.java");
//    Runtime.getRuntime().exec("java -cp ./target/classes:. sample.client.ClientArea");
//  }
//
//  public static void main(String[] args) throws IOException {
//    if (!registered) {
//      System.out.println("Kullanıcı Adınızı Girin : ");
//      BufferedReader nickInput = new BufferedReader(new InputStreamReader(System.in));
//      nick = nickInput.readLine();
//    }
//
//    new ChatClient("localhost", 8000).run();
//  }
//
//  public void run() {
//    EventLoopGroup group = new NioEventLoopGroup();
//
//    try {
//      Bootstrap bootstrap = new Bootstrap()
//          .group(group)
//          .channel(NioSocketChannel.class)
//          .handler(new ChatClientInitializer());
//
//      Channel channel = bootstrap.connect(host, port).sync().channel();
//
//      if (!registered) {
//        registered = true;
//        channel.writeAndFlush("[USR]" + nick + "\r\n");
//      }
//
//      BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
//
//      StringBuilder messageBuilder = new StringBuilder();
//      messageBuilder.append("[MSG]");
//
//      // TODO mesaj ve command(oyun içi bilgiler) ayrımı yapılacak
//
//      while (true) {
//        // TODO fix it. too complicated
//        // TODO add custom options
//        String inputValue = input.readLine();
//        if (inputValue.equals("start-game")) {
//          System.out.println("Pencere Boyutunu Girin (700 - 1500) : ");
//          BufferedReader optionsInput = new BufferedReader(new InputStreamReader(System.in));
//          int bound = 700;
//          try {
//            bound = Integer.valueOf(optionsInput.readLine());
//          } catch (Exception e) {
//            System.out.println("Geçersiz değer girdiniz!");
//          }
//
//          if (bound > 700 && bound < 1500) {
//            int finalBound = bound;
//            SwingUtilities.invokeLater(() -> {
//              try {
//                createAndShowGameArea(finalBound);
//              } catch (IOException e) {
//                e.printStackTrace();
//              }
//            });
//          } else {
//            System.out.println("Tanımsız değer girdiniz.");
//          }
//        }else {
//          channel.writeAndFlush("[MSG]" + inputValue + "\r\n");
//        }
//      }
//    } catch (Exception e) {
//      e.printStackTrace();
//    } finally {
//      group.shutdownGracefully();
//    }
//  }
}
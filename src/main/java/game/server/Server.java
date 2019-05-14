package game.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created by Emre Erinç
 * Istanbul University - 13016150016
 */
public class Server {
  private static int port = 8000;

  public Server(int port) {
    this.port = port;
  }

  public static void main(String[] args) {
    System.out.println("[SYS] - Server Started");
    new Server(port).run();
  }

  public void run() {
    EventLoopGroup managerGroup = new NioEventLoopGroup();
    EventLoopGroup workerGroup = new NioEventLoopGroup();

    try {
      ServerBootstrap bootstrap = new ServerBootstrap()
          .group(managerGroup, workerGroup)
          .channel(NioServerSocketChannel.class)
          .childHandler(new ServerInitializer());

      bootstrap.bind(port).sync().channel().closeFuture().sync();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      managerGroup.shutdownGracefully();
      workerGroup.shutdownGracefully();
    }
  }
}

package sample.server;

import sample.client.RectType;

import java.net.SocketAddress;

/**
 * Created by emre on 11.05.2019
 */
public class FigureClick {
  RectType rectType;
  String rectCreatedAt;
  SocketAddress clickOwner;
  Long clickTime;
}

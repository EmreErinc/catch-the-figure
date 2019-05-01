package sample.client;

import io.netty.channel.Channel;

/**
 * Created by emre on 28.04.2019
 */
public class Client {
  private Long id;
  private String nick;
  private Channel channel;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNick() {
    return nick;
  }

  public void setNick(String nick) {
    this.nick = nick;
  }

  public Channel getChannel() {
    return channel;
  }

  public void setChannel(Channel channel) {
    this.channel = channel;
  }
}

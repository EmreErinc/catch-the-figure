package game.client;

import io.netty.channel.Channel;

/**
 * Created by Emre Erinç
 * Istanbul University - 13016150016
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

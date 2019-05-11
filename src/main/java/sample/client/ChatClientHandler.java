package sample.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by emre on 28.04.2019
 */
public class ChatClientHandler extends SimpleChannelInboundHandler<String> {
  private boolean dedicated = false;

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, String message) {

    if (!dedicated) {
      dedicated = true;
      if (!message.contains("HOST")) {
        ClientArea.lblSizeX.setVisible(false);
        ClientArea.txtSizeX.setVisible(false);
        ClientArea.lblSizeY.setVisible(false);
        ClientArea.txtSizeY.setVisible(false);
        ClientArea.lblInterval.setVisible(false);
        ClientArea.txtInterval.setVisible(false);
        ClientArea.lblShapeLimit.setVisible(false);
        ClientArea.txtShapeLimit.setVisible(false);
        ClientArea.btnStartGame.setText("Join Game");
      }
    }

    if (message.contains("[SYS] - A new user has joined")) {
      ClientArea.txtMsgArea.setText(ClientArea.txtMsgArea.getText() + "A new user has joined" + "\r\n");
    }

    if (message.contains("has left")) {
      ClientArea.txtMsgArea.setText(ClientArea.txtMsgArea.getText() + Utils.parseMsgNick(message) + " has left lobby" + "\r\n");
    }

    if (message.contains("[MSG]")) {
      ClientArea.txtMsgArea.setText(ClientArea.txtMsgArea.getText() + Utils.parseMsgNick(message) + " : " + Utils.parseMsg(message) + "\r\n");
    }

    if (message.contains("[CMD]") && !message.contains("ALREADY_CLICKED") && !message.contains("YOU")) {
      if (message.contains("CREATED")) {
        Figure serverCreatedFigure = new Figure();
        RectType type = Utils.parseRectType(message);
        switch (type) {
          case CIRCLE:
            serverCreatedFigure.shape = Utils.parseCircle(message);
            break;
          case SQUARE:
            serverCreatedFigure.shape = Utils.parseSquare(message);
            break;
          case TRIANGLE:
            serverCreatedFigure.shape = Utils.parseTriangle(message);
            break;
        }
        serverCreatedFigure.type = type;
        serverCreatedFigure.createdAt = Long.valueOf(Utils.parseFigureCreatedAtForRemoteCreation(message));
        serverCreatedFigure.color = Utils.parseColor(message);
        ClientArea.figures.add(serverCreatedFigure);
      }

      if (message.contains("CLICKED")) {
        Long createdAt = Long.valueOf(Utils.parseFigureCreatedAt(message));
        Figure clickedFigure = ClientArea.figures.stream().filter(figure -> figure.createdAt.equals(createdAt)).findFirst().get();
        ClientArea.figures.remove(clickedFigure);
      }

      if (message.contains("STARTED")) {
        ClientArea.txtInterval.setText(Utils.parseInterval(message));
        ClientArea.txtSizeX.setText(Utils.parseX(message));
        ClientArea.txtSizeY.setText(Utils.parseY(message));
        ClientArea.txtShapeLimit.setText(Utils.parseShapeCount(message));

        ClientArea.pointsArray = Utils.parsePointsArray(message);

        ClientArea.btnStartGame.doClick();
      }
    }

    System.out.println(message);
  }
}
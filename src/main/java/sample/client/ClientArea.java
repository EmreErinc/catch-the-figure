package sample.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.Instant;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

/**
 * Created by emre on 29.04.2019
 */
public class ClientArea extends JFrame {
  private static Vector<Figure> figures = new Vector<>();

  private JLabel lblConnect;
  private JButton btnConnect;
  private JLabel lblSendMsg;
  private JButton btnSendMsg;
  private JTextField txtNick;
  private JTextField txtMsg;
  private static JTextArea txtMsgArea;
  private JScrollPane scrollMsgArea;
  private static JButton btnStartGame;
  private static JLabel lblSizeX;
  private static JTextField txtSizeX;
  private static JLabel lblSizeY;
  private static JTextField txtSizeY;
  private static JLabel lblInterval;
  private static JTextField txtInterval;
  private static JLabel lblShapeLimit;
  private static JTextField txtShapeLimit;

  private static GameArea gameArea;
  private int windowWidth = 700;
  private int windowHeight = 700;
  private int[] pointsArray = new int[3];

  private int totalPoints = 0;

  private static final String host = "localhost";
  private static final int port = 8000;
  private static boolean registered = false;
  private static Channel channel;
  private EventLoopGroup group;
  private Bootstrap bootstrap;

  private boolean generate = false;
  static boolean dedicated = false;

  public ClientArea() {
    initComponents();
  }

  // create the GUI explicitly on the Swing event thread
  @SuppressWarnings("unchecked")
  private void initComponents() {
    //ClientArea mainFrame = new ClientArea();

    lblConnect = new JLabel("Your Nick");
    txtNick = new JTextField();
    txtNick.setColumns(20);
    txtNick.setText("");
    btnConnect = new JButton("Register");
    btnConnect.addActionListener(this::btnConnectActionPerformed);

    lblSendMsg = new JLabel("Message");
    txtMsg = new JTextField();
    txtMsg.setColumns(20);
    txtMsg.setText("");
    btnSendMsg = new JButton("Send Message");
    btnSendMsg.addActionListener(this::btnSendMsgActionPerformed);

    txtMsgArea = new JTextArea();
    txtMsgArea.setColumns(15);
    txtMsgArea.setRows(25);
    scrollMsgArea = new JScrollPane();
    scrollMsgArea.setViewportView(txtMsgArea);

    lblSizeX = new JLabel("Width");
    txtSizeX = new JTextField("700");
    txtSizeX.setColumns(10);
    lblSizeY = new JLabel("Height");
    txtSizeY = new JTextField("700");
    txtSizeY.setColumns(10);
    lblInterval = new JLabel("Time Interval");
    txtInterval = new JTextField("1");
    txtInterval.setColumns(10);
    lblShapeLimit = new JLabel("Shape Limit");
    txtShapeLimit = new JTextField("20");
    txtShapeLimit.setColumns(10);

    btnStartGame = new JButton("Start Game");
    btnStartGame.addActionListener(this::btnStartGameActionPerformed);
    btnStartGame.setVisible(false);

    switchOptions(false);

    gameArea = new GameArea();
    gameArea.setVisible(false);
    //gameArea.setVisible(true);
    //gameArea.setPreferredSize(new Dimension(windowWidth, windowHeight));

    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setPreferredSize(new Dimension(windowWidth, windowHeight));
    //setResizable(false);

    //GridBagLayout gridBagLayout = new GridBagLayout();
    setLayout(new GridBagLayout());
    setTitle("...catch-the-figure...");

    GridBagConstraints constraints = new GridBagConstraints();

    constraints.gridx = 0;
    constraints.gridy = 0;
    constraints.ipadx = 20;
    add(lblConnect, constraints);

    constraints.gridx = 1;
    constraints.gridy = 0;
    add(txtNick, constraints);

    constraints.gridx = 2;
    constraints.gridy = 0;
    add(btnConnect, constraints);

    constraints.gridx = 0;
    constraints.gridy = 1;
    constraints.ipadx = 20;
    add(lblSendMsg, constraints);

    constraints.gridx = 1;
    constraints.gridy = 1;
    add(txtMsg, constraints);

    constraints.gridx = 2;
    constraints.gridy = 1;
    add(btnSendMsg, constraints);

    constraints.gridwidth = 3;
    constraints.gridx = 0;
    constraints.gridy = 2;
    constraints.ipadx = 300;
    add(txtMsgArea, constraints);

    constraints.gridwidth = 2;
    constraints.gridx = 0;
    constraints.gridy = 3;
    add(lblSizeX, constraints);

    constraints.gridx = 1;
    constraints.gridy = 3;
    add(txtSizeX, constraints);

    constraints.gridwidth = 2;
    constraints.gridx = 0;
    constraints.gridy = 4;
    add(lblSizeY, constraints);

    constraints.gridx = 1;
    constraints.gridy = 4;
    add(txtSizeY, constraints);

    constraints.gridx = 0;
    constraints.gridy = 5;
    add(lblInterval, constraints);

    constraints.gridx = 1;
    constraints.gridy = 5;
    add(txtInterval, constraints);

    constraints.gridx = 0;
    constraints.gridy = 6;
    add(lblShapeLimit, constraints);

    constraints.gridx = 1;
    constraints.gridy = 6;
    add(txtShapeLimit, constraints);

    constraints.gridwidth = 3;
    constraints.gridx = 1;
    constraints.gridy = 7;
    add(btnStartGame, constraints);

    constraints.gridx = 4;
    constraints.gridy = 2;
    add(gameArea, constraints);

//    GroupLayout groupLayout = new GroupLayout(getContentPane());
//    getContentPane().setLayout(groupLayout);
//
//    groupLayout.setAutoCreateGaps(true);
//    groupLayout.setAutoCreateContainerGaps(true);
//
//    groupLayout.setHorizontalGroup(
//        groupLayout.createParallelGroup(LEADING)
//            .addGroup(groupLayout.createParallelGroup(LEADING)
//                .addGroup(
//                    groupLayout.createParallelGroup(LEADING)
//                        .addGroup(groupLayout.createParallelGroup(LEADING)
//                            .addComponent(lblConnect)
//                            .addComponent(lblSendMsg)
//                        )
//                        .addGroup(groupLayout.createParallelGroup(LEADING)
//                            .addComponent(txtNick)
//                            .addComponent(txtMsg)
//                        )
//                        .addGroup(groupLayout.createParallelGroup(LEADING)
//                            .addComponent(btnConnect)
//                            .addComponent(btnSendMsg)
//                        )
//                )
//                .addComponent(txtMsgArea)
//                .addComponent(btnStartGame)
//            )
//            .addComponent(lblSizeX)
//            .addComponent(txtSizeX)
//            .addComponent(lblSizeY)
//            .addComponent(txtSizeY)
//            .addComponent(lblInterval)
//            .addComponent(txtInterval)
//            .addComponent(gameArea)
//    );
//
//    groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
//        .addGroup(groupLayout.createSequentialGroup()
//            .addGroup(groupLayout.createSequentialGroup()
//                .addComponent(lblConnect)
//                .addComponent(txtNick)
//                .addComponent(btnConnect)
//            )
//            .addGroup(groupLayout.createSequentialGroup()
//                .addComponent(lblSendMsg)
//                .addComponent(txtMsg)
//                .addComponent(btnSendMsg)
//            )
//            .addComponent(txtMsgArea)
//            .addComponent(btnStartGame)
//            .addComponent(lblSizeX)
//            .addComponent(txtSizeX)
//            .addComponent(lblSizeY)
//            .addComponent(txtSizeY)
//            .addComponent(lblInterval)
//            .addComponent(txtInterval)
//        )
//        .addComponent(gameArea)
//    );

    pack();

//    GroupLayout layout = new GroupLayout(getContentPane());
//    getContentPane().setLayout(layout);
//    layout.setHorizontalGroup(
//        layout.createParallelGroup(GroupLayout.Alignment.LEADING)
//            .addGroup(layout.createSequentialGroup()
//                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
//                    .addGroup(layout.createSequentialGroup()
//                        .addContainerGap()
//                        .addComponent(lblConnect)
//                        .addGap(27, 27, 27)
//                        .addComponent(btnConnect, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
//                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
//                        .addComponent(txtNick, GroupLayout.PREFERRED_SIZE, 61, GroupLayout.PREFERRED_SIZE))
//                    .addGroup(layout.createSequentialGroup()
//                        .addContainerGap()
//                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
//                        .addComponent(lblSendMsg)
//                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
//                        .addComponent(txtMsg, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
//                        .addGap(18, 18, 18)
//                        .addComponent(btnSendMsg))
//                    .addGroup(layout.createSequentialGroup()
//                        .addGap(21, 21, 21)
//                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
//                            .addGroup(layout.createSequentialGroup()
//                                .addComponent(lblSendMsg)
//                                .addGap(18, 18, 18)
//                                .addComponent(txtMsg, GroupLayout.PREFERRED_SIZE, 158, GroupLayout.PREFERRED_SIZE)
//                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
//                                .addComponent(btnSendMsg))
//                            .addComponent(scrollMsgArea, GroupLayout.PREFERRED_SIZE, 456, GroupLayout.PREFERRED_SIZE))))
//                .addContainerGap(155, Short.MAX_VALUE))
//    );
//    layout.setVerticalGroup(
//        layout.createParallelGroup(GroupLayout.Alignment.LEADING)
//            .addGroup(layout.createSequentialGroup()
//                .addContainerGap()
//                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
//                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
//                    .addComponent(txtNick, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
//                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
//                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
//                    .addComponent(btnSendMsg)
//                    .addComponent(txtMsg, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
//                    .addComponent(lblSendMsg))
//                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
//                .addComponent(scrollMsgArea, GroupLayout.PREFERRED_SIZE, 419, GroupLayout.PREFERRED_SIZE)
//                .addContainerGap())
//    );
  }

  public static void main(String[] args) {
    EventQueue.invokeLater(() -> new ClientArea().setVisible(true));
  }

  private void btnConnectActionPerformed(ActionEvent evt) {
    lblConnect.setVisible(false);
    txtNick.setVisible(false);
    btnConnect.setVisible(false);

    switchOptions(true);
    btnStartGame.setVisible(true);

    connect();
  }

  private void btnSendMsgActionPerformed(ActionEvent evt) {
    sendMessage();
  }

  private void btnStartGameActionPerformed(ActionEvent evt) {
    switchOptions(false);
    generate = true;

    setSize(new Dimension((Integer.valueOf(txtSizeX.getText()) < 400 ? 400 : Integer.valueOf(txtSizeX.getText())) + 800, (Integer.valueOf(txtSizeY.getText()) < 400 ? 400 : Integer.valueOf(txtSizeY.getText())) + 100));

    //TODO oyun kurucu oyunu başat tuşuna bastığında diğer oyunlarda otomatik başlamalı

    windowWidth = Integer.valueOf(txtSizeX.getText()) - 300;
    windowHeight = Integer.valueOf(txtSizeY.getText());
    gameArea.setPreferredSize(new Dimension(windowWidth, windowHeight));
    gameArea.setBorder(BorderFactory.createTitledBorder("game-area"));
    gameArea.setVisible(true);
  }

  private void switchOptions(boolean state) {
    lblSizeX.setVisible(state);
    txtSizeX.setVisible(state);
    lblSizeY.setVisible(state);
    txtSizeY.setVisible(state);
    lblInterval.setVisible(state);
    txtInterval.setVisible(state);
    lblShapeLimit.setVisible(state);
    txtShapeLimit.setVisible(state);
  }

  private void connect() {
    group = new NioEventLoopGroup();

    try {
      bootstrap = new Bootstrap()
          .group(group)
          .channel(NioSocketChannel.class)
          .handler(new ChatClientInitializer());

      channel = bootstrap.connect(host, port).sync().channel();
      System.out.println(txtNick.getText());
      channel.writeAndFlush("[USR]" + txtNick.getText() + "\r\n");
      registered = true;

    } catch (InterruptedException e1) {
      e1.printStackTrace();
    }
  }

  private void sendMessage() {
    channel.writeAndFlush("[MSG]" + txtMsg.getText() + "\r\n");
  }

  public class GameArea extends JPanel {
    public GameArea() {
      MyMouseAdapter myMouseAdapter = new MyMouseAdapter();
      addMouseListener(myMouseAdapter);

      Timer timer = new Timer();
      TimerTask task = new TimerTask() {
        @Override
        public void run() {
          if (generate && dedicated) {
            Figure figure = Generators.generateOneShape(windowWidth, windowHeight);
            if (figure.type.equals(RectType.TRIANGLE)){
              channel.writeAndFlush("[CMD] - Created | Type : <<" + figure.type + ">> " +
                  "| Color : <<" + figure.color + ">> " +
                  "| Bounds : <<" + Arrays.toString(((Polygon) figure.shape).xpoints) + "|" + Arrays.toString(((Polygon) figure.shape).ypoints) + "|" + ((Polygon)figure.shape).npoints + ">> \r\n");
            }else{
              channel.writeAndFlush("[CMD] - Created | Type : <<" + figure.type + ">> | Color : <<" + figure.color + ">> | Bounds : <<" + figure.shape.getBounds() + ">> \r\n");
            }
            figures.add(figure);
            repaint();
            if (figures.size() == Integer.valueOf(txtShapeLimit.getText()))
              timer.cancel();
          }
        }
      };
      timer.schedule(task, 0, Integer.valueOf(txtInterval.getText()) * 1000);

      pointsArray[0] = Generators.generatePoint();//circle point
      pointsArray[1] = Generators.generatePoint();//triangle point
      pointsArray[2] = Generators.generatePoint();//square point
    }

    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      Graphics2D g2d = (Graphics2D) g;

      //render figures
      for (Figure figure : figures) {
        g2d.setColor(figure.color);
        g2d.fill(figure.shape);
      }
    }

    private class MyMouseAdapter extends MouseAdapter {
      @Override
      public void mousePressed(MouseEvent e) {
        Point mousePoint = e.getPoint();
        System.out.println(mousePoint.x + "---" + mousePoint.y);
        for (int i = 0; i < figures.size(); i++) {
          if (figures.get(i).shape.contains(mousePoint)) {
            totalPoints = totalPoints + pointsArray[figures.get(i).type.ordinal()];
            System.out.println("Points : " + totalPoints);

            long clickTime = Instant.now().toEpochMilli();
            channel.writeAndFlush("[CMD] - Clicked : <<" + figures.get(i).type + ">> | Time : <<" + clickTime + ">> \r\n");

            figures.remove(figures.get(i));
            repaint();
          }
        }
      }
    }
  }

  static String sysMsg = "";

  public static class ChatClientHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String message) {

      if (message.contains("[MSG]")) {
        txtMsgArea.setText(txtMsgArea.getText() + message + "\r\n");
      }

      if (message.contains("[CMD]") && message.contains("Created") && !message.contains("YOU")){
        Figure serverCreatedFigure = new Figure();

        RectType type = Utils.parseRectType(message);

        switch (type){
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

        serverCreatedFigure.color = Utils.parseColor(message);
        figures.add(serverCreatedFigure);
      }

      if (!dedicated) {
        sysMsg = message;

        if (!sysMsg.contains("HOST")){
          lblSizeX.setVisible(false);
          txtSizeX.setVisible(false);
          lblSizeY.setVisible(false);
          txtSizeY.setVisible(false);
          lblInterval.setVisible(false);
          txtInterval.setVisible(false);
          lblShapeLimit.setVisible(false);
          txtShapeLimit.setVisible(false);
          btnStartGame.setText("Join Game");

          dedicated = true;
        }
      }

      System.out.println(message);
    }
  }
}

package sample.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.Instant;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import static javax.swing.GroupLayout.Alignment.*;

/**
 * Created by emre on 29.04.2019
 */
public class ClientArea extends JFrame {
  private Vector<Figure> figures = new Vector<>();

  private JLabel lblConnect;
  private JButton btnConnect;
  private JLabel lblSendMsg;
  private JButton btnSendMsg;
  private JTextField txtNick;
  private JTextField txtMsg;
  private JTextArea txtMsgArea;
  private JScrollPane scrollMsgArea;
  private JButton btnStartGame;
  private JLabel lblSizeX;
  private JTextField txtSizeX;
  private JLabel lblSizeY;
  private JTextField txtSizeY;
  private JLabel lblInterval;
  private JTextField txtInterval;

  private GameArea gameArea;
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

    btnStartGame = new JButton("Start Game");
    btnStartGame.addActionListener(this::btnStartGameActionPerformed);

    gameArea = new GameArea();
    gameArea.setVisible(false);
    gameArea.setPreferredSize(new Dimension(windowWidth, windowHeight));

    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

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
//    constraints.ipadx = 160;
    add(txtMsgArea, constraints);

    constraints.gridx = 0;
    constraints.gridy = 3;
    //constraints.ipadx = 30;
    add(lblSizeX, constraints);

    constraints.gridx = 1;
    constraints.gridy = 3;
    add(txtSizeX, constraints);

    constraints.gridx = 0;
    constraints.gridy = 4;
    //constraints.ipadx = 30;
    add(lblSizeY, constraints);

    constraints.gridx = 1;
    constraints.gridy = 4;
    add(txtSizeY, constraints);

    constraints.gridx = 0;
    constraints.gridy = 5;
    //constraints.ipadx = 30;
    add(lblInterval, constraints);

    constraints.gridx = 1;
    constraints.gridy = 5;
    add(txtInterval, constraints);

    constraints.gridheight = 3;
    constraints.gridx = 2;
    constraints.gridy = 3;
    add(btnStartGame, constraints);


//    gridBagLayout.setBorder(BorderFactory.createTitledBorder("Personal Info"));
//    gridBagLayout.setOpaque(true);
//    gridBagLayout.setBackground(Color.white);
//    gridBagLayout.add(lblConnect, constraints,0);
//    gridBagLayout.add(txtNick, constraints, 1);
//    gridBagLayout.add(lblSendMsg, constraints,0);
//    gridBagLayout.add(txtMsg, constraints, 1);
//    getContentPane().setLayout(gridBagLayout.getLayout());


    /*GroupLayout groupLayout = new GroupLayout(getContentPane());
    getContentPane().setLayout(groupLayout);

    groupLayout.setAutoCreateGaps(true);
    groupLayout.setAutoCreateContainerGaps(true);

    groupLayout.setHorizontalGroup(
        groupLayout.createParallelGroup(LEADING)
            .addGroup(groupLayout.createParallelGroup(LEADING)
                .addGroup(
                    groupLayout.createParallelGroup(LEADING)
                        .addGroup(groupLayout.createParallelGroup(LEADING)
                            .addComponent(lblConnect)
                            .addComponent(lblSendMsg)
                        )
                        .addGroup(groupLayout.createParallelGroup(LEADING)
                            .addComponent(txtNick)
                            .addComponent(txtMsg)
                        )
                        .addGroup(groupLayout.createParallelGroup(LEADING)
                            .addComponent(btnConnect)
                            .addComponent(btnSendMsg)
                        )
                )
                .addComponent(txtMsgArea)
                .addComponent(btnStartGame)
            )
            .addComponent(lblSizeX)
            .addComponent(txtSizeX)
            .addComponent(lblSizeY)
            .addComponent(txtSizeY)
            .addComponent(lblInterval)
            .addComponent(txtInterval)
            .addComponent(gameArea)
    );

    groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
        .addGroup(groupLayout.createSequentialGroup()
            .addGroup(groupLayout.createSequentialGroup()
                .addComponent(lblConnect)
                .addComponent(txtNick)
                .addComponent(btnConnect)
            )
            .addGroup(groupLayout.createSequentialGroup()
                .addComponent(lblSendMsg)
                .addComponent(txtMsg)
                .addComponent(btnSendMsg)
            )
            .addComponent(txtMsgArea)
            .addComponent(btnStartGame)
            .addComponent(lblSizeX)
            .addComponent(txtSizeX)
            .addComponent(lblSizeY)
            .addComponent(txtSizeY)
            .addComponent(lblInterval)
            .addComponent(txtInterval)
        )
        .addComponent(gameArea)
    );*/

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
    connect();
  }

  private void btnSendMsgActionPerformed(ActionEvent evt) {
    sendMessage();
  }

  private void btnStartGameActionPerformed(ActionEvent evt) {
    //TODO gereksizlerin visible = false yap


    gameArea.setSize(new Dimension(Integer.valueOf(txtSizeX.getText()), Integer.valueOf(txtSizeY.getText())));
    gameArea.setVisible(true);
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
    txtMsgArea.setText("<<YOU>> : " + txtMsg.getText() + "\r\n");
  }

  public class GameArea extends JPanel {
    public GameArea() {
      MyMouseAdapter myMouseAdapter = new MyMouseAdapter();
      addMouseListener(myMouseAdapter);

      Timer timer = new Timer();
      TimerTask task = new TimerTask() {
        @Override
        public void run() {
          figures.add(Generators.generateOneShape(700));
          repaint();
          if (figures.size() == 20)
            timer.cancel();
        }
      };
      timer.schedule(task, 0, Integer.valueOf(txtInterval.getText()) * 1000);

      pointsArray[0] = Generators.generatePoint();//circle point
      pointsArray[1] = Generators.generatePoint();//triangle point
      pointsArray[2] = Generators.generatePoint();//square point

      //TODO resize windows size
    }

    /*private void createAndShowGui() {
      GameArea mainPanel = new GameArea();

      JFrame frame = new JFrame("GameArea");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.getContentPane().add(mainPanel);
      frame.setSize(BOUND, BOUND);
      frame.setVisible(true);
      frame.setResizable(false);
    }*/

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

      @Override
      public void mouseReleased(MouseEvent e) {
        repaint();
      }
    }
  }
}

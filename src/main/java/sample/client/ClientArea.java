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
import java.util.*;
import java.util.List;
import java.util.Timer;

/**
 * Created by emre on 29.04.2019
 */
public class ClientArea extends JFrame {
  public static Vector<Figure> figures = new Vector<>();

  private JLabel lblConnect;
  private JButton btnConnect;
  private JLabel lblSendMsg;
  private JButton btnSendMsg;
  private JTextField txtNick;
  private JTextField txtMsg;
  public static JTextArea txtMsgArea;
  private JScrollPane scrollMsgArea;
  public static JButton btnStartGame;
  public static JLabel lblSizeX;
  public static JTextField txtSizeX;
  public static JLabel lblSizeY;
  public static JTextField txtSizeY;
  public static JLabel lblInterval;
  public static JTextField txtInterval;
  public static JLabel lblShapeLimit;
  public static JTextField txtShapeLimit;
  private JLabel lblCirclePoint;
  private JLabel lblTrianglePoint;
  private JLabel lblSquarePoint;
  private JLabel lblTotalPoints;

  public static GameArea gameArea;
  private int windowWidth = 700;
  private int windowHeight = 700;
  public static int[] pointsArray = new int[3];

  private int totalPoints = 0;

  private static final String host = "localhost";
  private static final int port = 8000;
  private static Channel channel;
  private EventLoopGroup group;
  private Bootstrap bootstrap;

  private int count = 0;
  private boolean generate = false;

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
    btnSendMsg.setVisible(false);

    txtMsgArea = new JTextArea();
    txtMsgArea.setColumns(15);
    txtMsgArea.setRows(25);
    txtMsgArea.setEditable(false);
    txtMsgArea.setWrapStyleWord(true);
    scrollMsgArea = new JScrollPane(txtMsgArea);

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

    lblCirclePoint = new JLabel();
    lblCirclePoint.setVisible(false);
    lblTrianglePoint = new JLabel();
    lblTrianglePoint.setVisible(false);
    lblSquarePoint = new JLabel();
    lblSquarePoint.setVisible(false);
    lblTotalPoints = new JLabel();
    lblTotalPoints.setVisible(false);

    switchOptions(false);

    gameArea = new GameArea();
    gameArea.setVisible(false);

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
    //add(txtMsgArea, constraints);
    add(scrollMsgArea, constraints);

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

    constraints.gridx = 0;
    constraints.gridy = 4;
    add(lblCirclePoint, constraints);

    constraints.gridx = 0;
    constraints.gridy = 5;
    add(lblTrianglePoint, constraints);

    constraints.gridx = 0;
    constraints.gridy = 6;
    add(lblSquarePoint, constraints);

    constraints.gridx = 0;
    constraints.gridy = 7;
    add(lblTotalPoints, constraints);

    pack();
  }

  public static void main(String[] args) {
    EventQueue.invokeLater(() -> new ClientArea().setVisible(true));
  }

  private void btnConnectActionPerformed(ActionEvent evt) {
    lblConnect.setVisible(false);
    txtNick.setVisible(false);
    btnConnect.setVisible(false);

    btnSendMsg.setVisible(true);
    switchOptions(true);
    btnStartGame.setVisible(true);

    connect();
  }

  private void btnSendMsgActionPerformed(ActionEvent evt) {
    sendMessage();
  }

  private void btnStartGameActionPerformed(ActionEvent evt) {
    switchOptions(false);

    if (btnStartGame.getText().equals("Start Game")) {
      pointsArray[0] = Generators.generatePoint();//circle point
      pointsArray[1] = Generators.generatePoint();//triangle point
      pointsArray[2] = Generators.generatePoint();//square point

      channel.writeAndFlush("[CMD] - START " +
          "| Interval : <<" + txtInterval.getText() + ">> " +
          "| X : <<" + txtSizeX.getText() + ">> " +
          "| Y : <<" + txtSizeY.getText() + ">> " +
          "| ShapeCount : <<" + txtShapeLimit.getText() + ">> " +
          "| Points : <<" + Arrays.toString(pointsArray) + ">>\r\n");

      generate = true;
    }

    setSize(new Dimension((Integer.valueOf(txtSizeX.getText()) < 400 ? 400 : Integer.valueOf(txtSizeX.getText())) + 800, (Integer.valueOf(txtSizeY.getText()) < 400 ? 400 : Integer.valueOf(txtSizeY.getText())) + 130));
    gameArea.setPreferredSize(new Dimension(Integer.valueOf(txtSizeX.getText()) - 300, Integer.valueOf(txtSizeY.getText())));
    gameArea.setBorder(BorderFactory.createTitledBorder("game-area"));
    gameArea.setVisible(true);

    lblCirclePoint.setText("Circle Point : " + pointsArray[0]);
    lblTrianglePoint.setText("Triangle Point : " + pointsArray[1]);
    lblSquarePoint.setText("Square Point : " + pointsArray[2]);
    lblTotalPoints.setText("Your Total Points : " + totalPoints);
    lblCirclePoint.setVisible(true);
    lblTrianglePoint.setVisible(true);
    lblSquarePoint.setVisible(true);
    lblTotalPoints.setVisible(true);

    btnStartGame.setVisible(false);
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
      refresher();
      Timer timer = new Timer();
      TimerTask task = new TimerTask() {
        @Override
        public void run() {
          if (generate) {
            Figure figure = Generators.generateOneShape(windowWidth, windowHeight);
            if (figure.type.equals(RectType.TRIANGLE)) {
              channel.writeAndFlush("[CMD] - CREATED " +
                  "| Type : <<" + figure.type + ">> " +
                  "| Color : <<" + figure.color + ">> " +
                  "| Bounds : <<" + Arrays.toString(((Polygon) figure.shape).xpoints) + "|" + Arrays.toString(((Polygon) figure.shape).ypoints) + "|" + ((Polygon) figure.shape).npoints + ">> " +
                  "| FigureCreation : <<" + figure.createdAt + ">> \r\n");
            } else {
              channel.writeAndFlush("[CMD] - CREATED " +
                  "| Type : <<" + figure.type + ">> " +
                  "| Color : <<" + figure.color + ">> " +
                  "| Bounds : <<" + figure.shape.getBounds() + ">> " +
                  "| FigureCreation : <<" + figure.createdAt + ">> \r\n");
            }
            figures.add(figure);
            count++;
            if (count == Integer.valueOf(txtShapeLimit.getText())) {
              timer.cancel();
            }
          }
          repaint();
        }
      };
      timer.schedule(task, 0, Integer.valueOf(txtInterval.getText()) * 1000);
    }

    private void refresher() {
      Timer timer = new Timer();
      TimerTask task = new TimerTask() {
        @Override
        public void run() {
          repaint();
        }
      };
      timer.schedule(task, 0, 100);
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

    private List<Figure> clickedFigures = new ArrayList<>();
    Long createdAt = 0L;
    Long clickTime = 0L;

    private class MyMouseAdapter extends MouseAdapter {
      @Override
      public void mousePressed(MouseEvent e) {
        Point mousePoint = e.getPoint();
        for (Figure figure : figures) {
          if (figure.shape.contains(mousePoint)) {
            createdAt = figure.createdAt;
            clickTime = Instant.now().toEpochMilli();
            if (clickedFigures.stream().noneMatch(clicked -> clicked.createdAt.equals(createdAt))) {
              clickedFigures.add(figure);
            }
          }
        }

        if (clickedFigures.size() != 0) {
          int lastIndex = 0;
          if (clickedFigures.size() > 1) {
            lastIndex = clickedFigures.size() - 1;
            clickedFigures.sort(Comparator.comparing(figure -> figure.createdAt));
          }
          totalPoints = totalPoints + pointsArray[clickedFigures.get(lastIndex).type.ordinal()];
          System.out.println("Points : " + totalPoints);
          figures.remove(clickedFigures.get(lastIndex));
          channel.writeAndFlush("[CMD] - CLICKED : <<" + clickedFigures.get(lastIndex).type + ">> " +
              "| Time : <<" + clickTime + ">> " +
              "| FigureCreation : <<" + clickedFigures.get(lastIndex).createdAt + ">> \r\n");
          clickedFigures.clear();
          repaint();
        }
        lblTotalPoints.setText("Your Total Points : " + totalPoints);
      }
    }
  }
}

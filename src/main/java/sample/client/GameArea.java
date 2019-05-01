package sample.client;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;
import java.util.List;
import javax.swing.*;

/**
 * Created by emre on 29.04.2019
 */
public class GameArea extends JPanel {
  public static Integer BOUND = 700;
  private static int totalShapeCount = 0;
  private static Vector<Figure> figures = new Vector<>();
  private static List<String> rectTypes = new ArrayList<>();

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

  // create the GUI explicitly on the Swing event thread
  private static void createAndShowGui() {
    GameArea mainPanel = new GameArea();

    JFrame frame = new JFrame("GameArea");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane().add(mainPanel);
    frame.setSize(BOUND, BOUND);
    frame.setVisible(true);
    frame.setResizable(false);
  }

  //generate triangle
  //private static Polygon generateTriangle() {
  private static Shape generateTriangle() {
    Polygon triangle = new Polygon();
    int base = generateNumber(40, 80);

    triangle.xpoints[0] = generateNumber(1, BOUND);
    triangle.ypoints[0] = generateNumber(1, BOUND);

    for (int i = 1; i < 3; i++) {
      triangle.xpoints[i] = generateCoordinate(triangle.xpoints[i - 1], base);
      triangle.ypoints[i] = generateCoordinate(triangle.ypoints[i - 1], base);
    }

    triangle.npoints = 3;
    return triangle;
  }

  //generate circle
  private static Shape generateCircle() {
    Ellipse2D.Double circle = new Ellipse2D.Double();
    int radius = generateNumber(20, 60);
    circle.x = generateNumber(0, BOUND - (radius * 2));
    circle.y = generateNumber(0, BOUND - (radius * 2));
    circle.width = radius * 2;
    circle.height = radius * 2;

    return circle;
  }

  //generate square
  private static Shape generateSquare() {
    Rectangle square = new Rectangle();
    int edge = generateNumber(20, 60);
    square.x = generateNumber(0, BOUND - (edge * 2));
    square.y = generateNumber(0, BOUND - (edge * 2));
    square.width = edge * 2;
    square.height = edge * 2;

    return square;
  }

  private static Integer generateNumber(int lower, int upper) {
    Random random = new Random();

    if (lower > BOUND && upper > BOUND) {
      lower = lower - 50;
      upper = upper - 50;
    }

    // TODO lower ve upper değerleri aynı anda 700 den büyük olursa patlıyor. fix it
    boolean check = true;
    int generated = random.nextInt(upper - lower) + lower;
    while (check) {
      if (generated < BOUND) {
        check = false;
      }
      generated = random.nextInt(upper - lower) + lower;
    }

//    int generated = random.nextInt(upper - lower) + lower;
//
//    if (generated > 700) {
//      return generateNumber(lower, upper);
//    }
    return generated;
  }

  private static Integer generateCoordinate(int bound, int base) {
    int leftGenerated = generateNumber(bound - base, bound - 30);
    int rightGenerated = generateNumber(bound + 30, bound + base);

    int select = new Random().nextInt(2) + 1;
    return select == 1 ? leftGenerated : rightGenerated;
  }

  private static class Figure {
    Shape shape;
    Color color;
    RectType type;
  }

  private enum RectType {
    CIRCLE, TRIANGLE, SQUARE
  }

  private class MyMouseAdapter extends MouseAdapter {
    @Override
    public void mousePressed(MouseEvent e) {
      Point mousePoint = e.getPoint();
      for (int i = 0; i < totalShapeCount; i++) {
        if (figures.get(i).shape.contains(mousePoint)) {
          System.out.println("you clicked a " + figures.get(i).type);
          figures.remove(figures.get(i));//TODO click bilgisi server a göderilecek
          totalShapeCount--;
          repaint();
        }
      }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
      repaint();
    }
  }

  public GameArea() {
    MyMouseAdapter myMouseAdapter = new MyMouseAdapter();
    addMouseListener(myMouseAdapter);
  }

  public static void main(String[] args) {
    Random random = new Random();
    rectTypes.add("CIRCLE");
    rectTypes.add("SQUARE");
    rectTypes.add("TRIANGLE");

    for (int i = 0; i < 3; i++) {
      int shapeCount = generateNumber(0, 10);
      for (int j = 0; j < shapeCount; j++) {
        Figure figure = new Figure();

        figure.type = RectType.valueOf(rectTypes.get(i));
        figure.color = new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
        switch (rectTypes.get(i)) {
          case "CIRCLE":
            figure.shape = generateCircle();
            break;
          case "SQUARE":
            figure.shape = generateSquare();
            break;
          case "TRIANGLE":
            figure.shape = generateTriangle();
            break;
        }
        figures.add(figure);
      }
      totalShapeCount = totalShapeCount + shapeCount;
    }
    SwingUtilities.invokeLater(GameArea::createAndShowGui);
  }
}

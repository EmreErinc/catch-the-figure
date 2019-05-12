package sample.client;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by emre on 04.05.2019
 */
public class Generators {
  //generate triangle
  //private static Polygon generateTriangle() {
  public static Shape generateTriangle(int width, int height) {
    Polygon triangle = new Polygon();
    triangle.xpoints[0] = generateNumber(1, width);
    triangle.ypoints[0] = generateNumber(1, height);

    for (int i = 1; i < 3; i++) {
      triangle.xpoints[i] = generateNumber(triangle.xpoints[i - 1], width);
      triangle.ypoints[i] = generateNumber(triangle.ypoints[i - 1], height);
    }

    triangle.npoints = 3;
    return triangle;
  }

  //generate circle
  public static Shape generateCircle(int width, int height) {
    Ellipse2D.Double circle = new Ellipse2D.Double();
    int radius = generateNumber(20, 60);
    circle.x = generateNumber(0, width - (radius * 2));
    circle.y = generateNumber(0, height - (radius * 2));
    circle.width = radius * 2;
    circle.height = radius * 2;

    return circle;
  }

  //generate square
  public static Shape generateSquare(int width, int height) {
    Rectangle square = new Rectangle();
    int edge = generateNumber(20, 60);
    square.x = generateNumber(0, width - (edge * 2));
    square.y = generateNumber(0, height - (edge * 2));
    square.width = edge * 2;
    square.height = edge * 2;

    return square;
  }

  public static Integer generatePoint() {
    Random random = new Random();
    int point = random.nextInt(10);
    if (point == 0){
      return generatePoint();
    }
    return (random.nextInt(2) == 1) ? (0 - point) : point;
  }

  public static Integer generateNumber(int lower, int upper) {
    Random random = new Random();
    return random.nextInt(upper - lower) + lower;
  }

  public static Figure generateOneShape(int width, int height) {
    Random random = new Random();
    List<String> rectTypes = new ArrayList<>();
    rectTypes.add("CIRCLE");
    rectTypes.add("SQUARE");
    rectTypes.add("TRIANGLE");

    int randShapeNumber = generateNumber(0, 3);

    Figure figure = new Figure();

    figure.type = RectType.valueOf(rectTypes.get(randShapeNumber));
    figure.color = new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
    switch (rectTypes.get(randShapeNumber)) {
      case "CIRCLE":
        figure.shape = Generators.generateCircle(width, height);
        break;
      case "SQUARE":
        figure.shape = Generators.generateSquare(width, height);
        break;
      case "TRIANGLE":
        figure.shape = Generators.generateTriangle(width, height);
        break;
    }
    figure.createdAt = Instant.now().toEpochMilli();

    return figure;
  }
}

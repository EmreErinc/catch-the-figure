package sample.client;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

/**
 * Created by emre on 04.05.2019
 */
public class Generators {
  //TODO generator ları tek çatı altında topla
  //generate triangle
  //private static Polygon generateTriangle() {
  public static Shape generateTriangle(int frameBound) {
    Polygon triangle = new Polygon();
    int base = generateNumber(40, 80, frameBound);

    triangle.xpoints[0] = generateNumber(1, frameBound, frameBound);
    triangle.ypoints[0] = generateNumber(1, frameBound, frameBound);

    for (int i = 1; i < 3; i++) {
      triangle.xpoints[i] = generateCoordinate(triangle.xpoints[i - 1], base, frameBound);
      triangle.ypoints[i] = generateCoordinate(triangle.ypoints[i - 1], base, frameBound);
    }

    triangle.npoints = 3;
    return triangle;
  }

  //generate circle
  public static Shape generateCircle(int frameBound) {
    Ellipse2D.Double circle = new Ellipse2D.Double();
    int radius = generateNumber(20, 60, frameBound);
    circle.x = generateCoordinate(0, frameBound - (radius * 2), frameBound);
    circle.y = generateCoordinate(0, frameBound - (radius * 2), frameBound);
    circle.width = radius * 2;
    circle.height = radius * 2;

    return circle;
  }

  //generate square
  public static Shape generateSquare(int frameBound) {
    Rectangle square = new Rectangle();
    int edge = generateNumber(20, 60, frameBound);
    square.x = generateCoordinate(0, frameBound - (edge * 2), frameBound);
    square.y = generateCoordinate(0, frameBound - (edge * 2), frameBound);
    square.width = edge * 2;
    square.height = edge * 2;

    return square;
  }

  public static Integer generateNumber(int lower, int upper, int frameBound) {
    Random random = new Random();

    //TODO fix algorithmic problem
    if (lower > frameBound && upper > frameBound) {
      lower = lower - 50;
      upper = upper - 50;
    }

    // TODO lower ve upper değerleri aynı anda 700 den büyük olursa patlıyor. fix it
    boolean check = true;
    int generated = random.nextInt(upper - lower) + lower;
    while (check) {
      if (generated < frameBound) {
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

  public static Integer generatePoint(){
    Random random = new Random();
    int point = random.nextInt(30);
    return (random.nextInt(2) == 1) ? (0 - point) : point;
  }

  public static Integer generateCoordinate(int bound, int base, int frameBound) {
    int leftGenerated = generateNumber(bound - base, bound - 30, frameBound);
    int rightGenerated = generateNumber(bound + 30, bound + base, frameBound);

    int select = new Random().nextInt(2) + 1;
    return select == 1 ? leftGenerated : rightGenerated;
  }

  /*public static Vector<Figure> generateAllShapes(int frameBound){
    Vector<Figure> figures = new Vector<>();

    //for (int i = 0; i < 3; i++) {
      int shapeCount = Generators.generateNumber(0, 10, frameBound);
      for (int j = 0; j < shapeCount; j++) {
        figures.add(generateOneShape(frameBound));
      }
    //}
    return figures;
  }*/

  public static Figure generateOneShape(int frameBound) {
    Random random = new Random();
    List<String> rectTypes = new ArrayList<>();
    rectTypes.add("CIRCLE");
    rectTypes.add("SQUARE");
    rectTypes.add("TRIANGLE");

    int randShapeNumber = generateNumber(0, 3, frameBound);

    Figure figure = new Figure();

    figure.type = RectType.valueOf(rectTypes.get(randShapeNumber));
    figure.color = new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
    switch (rectTypes.get(randShapeNumber)) {
      case "CIRCLE":
        figure.shape = Generators.generateCircle(frameBound);
        break;
      case "SQUARE":
        figure.shape = Generators.generateSquare(frameBound);
        break;
      case "TRIANGLE":
        figure.shape = Generators.generateTriangle(frameBound);
        break;
    }

    return figure;
  }
}

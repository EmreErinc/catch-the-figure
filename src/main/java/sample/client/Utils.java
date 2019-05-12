package sample.client;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.regex.Pattern;

/**
 * Created by emre on 07.05.2019
 */
public class Utils {
  public static RectType parseRectType(String text) {
    return RectType.valueOf(text.split(Pattern.quote("<<"))[2].split(Pattern.quote(">>"))[0]);
  }

  public static RectType parseRectTypeForServer(String text) {
    return RectType.valueOf(text.split(Pattern.quote("<<"))[1].split(Pattern.quote(">>"))[0]);
  }

  public static Color parseColor(String text) {
    String colorRGB = text.split(Pattern.quote("<<"))[3].split(Pattern.quote(">>"))[0];

    int r = Integer.valueOf(colorRGB.split(Pattern.quote("="))[1].split(Pattern.quote(","))[0]);
    int g = Integer.valueOf(colorRGB.split(Pattern.quote("="))[2].split(Pattern.quote(","))[0]);
    int b = Integer.valueOf(colorRGB.split(Pattern.quote("="))[3].split(Pattern.quote("]"))[0]);
    return new Color(r, g, b);
  }

  public static String parseFigureCreatedFromRemote(String text) {
    return text.split(Pattern.quote("<<"))[5].split(Pattern.quote(">>"))[0];
  }

  public static String parseFigureCreatedAtOnRemoteClick(String text) {
    return text.split(Pattern.quote("<<"))[3].split(Pattern.quote(">>"))[0];
  }

  public static String parseFigureCreatedAt(String text) {
    return text.split(Pattern.quote("<<"))[4].split(Pattern.quote(">>"))[0];
  }

  public static String parseFigureCreatedAtOnClick(String text) {
    return text.split(Pattern.quote("<<"))[2].split(Pattern.quote(">>"))[0];
  }

  public static Polygon parseTriangle(String text) {
    String bounds = text.split(Pattern.quote("<<"))[4].split(Pattern.quote(">>"))[0];

    Polygon triangle = new Polygon();
    triangle.xpoints = parseTrianglePoints(bounds.split(Pattern.quote("|"))[0].split(Pattern.quote("<<"))[0]);
    triangle.ypoints = parseTrianglePoints(bounds.split(Pattern.quote("|"))[1]);
    triangle.npoints = Integer.valueOf(bounds.split(Pattern.quote("|"))[2].split(Pattern.quote(">>"))[0]);
    return triangle;
  }

  public static Shape parseCircle(String message) {
    Ellipse2D.Double circle = new Ellipse2D.Double();
    circle.x = Utils.parseBounds(message).x;
    circle.y = Utils.parseBounds(message).y;
    circle.width = Utils.parseBounds(message).width;
    circle.height = Utils.parseBounds(message).height;
    return circle;
  }

  public static Shape parseSquare(String message) {
    Rectangle square = new Rectangle();
    square.x = Utils.parseBounds(message).x;
    square.y = Utils.parseBounds(message).y;
    square.width = Utils.parseBounds(message).width;
    square.height = Utils.parseBounds(message).height;
    return square;
  }

  public static int[] parseTrianglePoints(String pointsString) {
    int[] result = new int[3];

    result[0] = Integer.valueOf(pointsString.split(Pattern.quote(","))[0].split(Pattern.quote("["))[1]);
    result[1] = Integer.valueOf(pointsString.split(Pattern.quote(", "))[1]);
    result[2] = Integer.valueOf(pointsString.split(Pattern.quote(", "))[2]);
    return result;
  }

  public static ShapeBound parseBounds(String text) {
    String bounds = text.split(Pattern.quote("<<"))[4].split(Pattern.quote(">>"))[0];

    ShapeBound shapeBound = new ShapeBound();
    shapeBound.x = Integer.valueOf(bounds.split(Pattern.quote("="))[1].split(Pattern.quote(","))[0]);
    shapeBound.y = Integer.valueOf(bounds.split(Pattern.quote("="))[2].split(Pattern.quote(","))[0]);
    shapeBound.width = Integer.valueOf(bounds.split(Pattern.quote("="))[3].split(Pattern.quote(","))[0]);
    shapeBound.height = Integer.valueOf(bounds.split(Pattern.quote("="))[4].split(Pattern.quote("]"))[0]);
    return shapeBound;
  }


  public static String parseMessage(String text) {
    if (text.contains("[MSG]")) {
      return text.split(Pattern.quote("[MSG]"))[1].split(Pattern.quote("\r\n"))[0];
    }
    return "EMPTY_MESSAGE";
  }

  public static String parseNick(String text) {
    return text.split(Pattern.quote("[USR]"))[1].split(Pattern.quote("\r\n"))[0];
  }

  public static Long parseClickTime(String text) {
    return Long.valueOf(text.split(Pattern.quote("<<"))[2].split(Pattern.quote(">>"))[0]);
  }

  public static String parseColorStr(String text) {
    return text.split(Pattern.quote("<<"))[2].split(Pattern.quote(">>"))[0];
  }

  public static String parseBoundsStr(String text) {
    return text.split(Pattern.quote("<<"))[3].split(Pattern.quote(">>"))[0];
  }

  public static String parseIntervalForServer(String text) {
    return text.split(Pattern.quote("<<"))[1].split(Pattern.quote(">>"))[0];
  }

  public static String parseXForServer(String text) {
    return text.split(Pattern.quote("<<"))[2].split(Pattern.quote(">>"))[0];
  }

  public static String parseYForServer(String text) {
    return text.split(Pattern.quote("<<"))[3].split(Pattern.quote(">>"))[0];
  }

  public static String parseShapeCountForServer(String text) {
    return text.split(Pattern.quote("<<"))[4].split(Pattern.quote(">>"))[0];
  }

  public static String parsePointsForServer(String text) {
    return text.split(Pattern.quote("<<"))[5].split(Pattern.quote(">>"))[0];
  }

  public static String parseInterval(String text) {
    return text.split(Pattern.quote("<<"))[2].split(Pattern.quote(">>"))[0];
  }

  public static String parseX(String text) {
    return text.split(Pattern.quote("<<"))[3].split(Pattern.quote(">>"))[0];
  }

  public static String parseY(String text) {
    return text.split(Pattern.quote("<<"))[4].split(Pattern.quote(">>"))[0];
  }

  public static String parseShapeCount(String text) {
    return text.split(Pattern.quote("<<"))[5].split(Pattern.quote(">>"))[0];
  }

  public static int[] parsePointsArray(String text) {
    int[] points = new int[3];
    String pointsStr = text.split(Pattern.quote("<<"))[6].split(Pattern.quote(">>"))[0];
    points[0] = Integer.valueOf(pointsStr.split(Pattern.quote(","))[0].split(Pattern.quote("["))[1]);
    points[1] = Integer.valueOf(pointsStr.split(Pattern.quote(", "))[1]);
    points[2] = Integer.valueOf(pointsStr.split(Pattern.quote(", "))[2].split(Pattern.quote("]"))[0]);

    return points;
  }

  public static String parseMsgNick(String text) {
    return text.split(Pattern.quote("<<"))[1].split(Pattern.quote(">>"))[0];
  }

  public static String parseMsg(String text) {
    return text.split(Pattern.quote(">> "))[1];
  }

  public static int parsePoints(String text) {
    return Integer.valueOf(text.split(Pattern.quote("<<"))[1].split(Pattern.quote(">>"))[0].split(Pattern.quote(": "))[1]);
  }
}

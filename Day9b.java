import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;

public class Day9b {
  public static String myRead(Scanner scanner) {
    String line;
    try {
      line = scanner.nextLine().trim();
    } catch (Exception e) {
      line = null;
    }
    return line;
  }

  public static Scanner myScanner(File text) {
    Scanner scanner;
    try {
      scanner = new Scanner(text);
    } catch (Exception e) {
      System.out.println("No Scanner");
      return null;
    }
    return scanner;
  }

  public static void main(String[] args) {
    File text = new File("/Users/axelteo/Desktop/AdventofCode/Day9input.txt");
    Scanner scanner = myScanner(text);
    if (scanner == null) {
      return;
    }

    ArrayList<Point> tailPos = new ArrayList<>();
    // Array of 10 knots. knots[0] is head, knots[9] is tail.
    Point[] knots = new Point[10];
    for (int i = 0; i < 10; i++) {
      knots[i] = new Point(0, 0);
    }
    tailPos.add(knots[9]);

    String line;
    String[] tokens;
    while ((line = myRead(scanner)) != null) {
      tokens = line.split(" ", 0);
      int moves = Integer.valueOf(tokens[1]);
      for (int i = 0; i < moves; ++i) {
        Point prevKnotPos = knots[0];
        knots[0] = knots[0].move(tokens[0]);
	for (int j = 0; j < 9; ++j) {
	  knots[j + 1] = knots[j].tailMovesTo(knots[j + 1]);
	}
	if (!tailPos.contains(knots[9])) {
	  tailPos.add(knots[9]);
	}
      }
    }
    System.out.println("Num of points tail visited is: " + tailPos.size());
  }
}

class Point {
  int x;
  int y;

  public Point(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public boolean isAdj(Point pt) {
    int xDiff = this.x - pt.x;
    int yDiff = this.y - pt.y;
    if (xDiff <= 1 && xDiff >= -1) {
      if (yDiff <= 1 && yDiff >= -1) {
        return true;
      }
    }
    return false;
  }

  public Point move(String dir) {
    if (dir.equals("U")) {
      return new Point(this.x, this.y + 1);
    }
    if (dir.equals("D")) {
      return new Point(this.x, this.y - 1);
    }
    if (dir.equals("L")) {
      return new Point(this.x - 1, this.y);
    }
    if (dir.equals("R")) {
      return new Point(this.x + 1, this.y);
    }
    System.out.println("error with move");
    return null;
  }

  public Point tailMovesTo(Point tail) {
    if (this.isAdj(tail)) {
      return tail;
    }
    if (tail.x == this.x) {
      return new Point(this.x, (this.y + tail.y) / 2);
    }
    if (tail.y == this.y) {
      return new Point((this.x + tail.x) / 2, this.y);
    }
    int xDiff = this.x - tail.x;
    int yDiff = this.y - tail.y;
    xDiff = (int) Math.signum((double) xDiff);
    yDiff = (int) Math.signum((double) yDiff);
    return new Point(tail.x + xDiff, tail.y + yDiff);
    
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Point)) {
      return false;
    }
    Point tmp = (Point) obj;
    if (this.x == tmp.x && this.y == tmp.y) {
      return true;
    }
    return false;
  }

  @Override
  public String toString() {
    return "(" + this.x + ", " + this.y + ")";
  }
}

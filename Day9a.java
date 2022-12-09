import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;

public class Day9a {
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
    Point head = new Point(0, 0);
    Point tail = new Point(0, 0);
    tailPos.add(tail);

    String line;
    String[] tokens;
    while ((line = myRead(scanner)) != null) {
      tokens = line.split(" ", 0);
      int moves = Integer.valueOf(tokens[1]);
      for (int i = 0; i < moves; ++i) {
        Point oldHead = head;
        head = head.move(tokens[0]);
	if (!head.isAdj(tail)) {
	  tail = oldHead;
	  if (!tailPos.contains(tail)) {
	    tailPos.add(tail);
	  }
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
}


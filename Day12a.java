import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.util.LinkedList;
import java.util.Queue;

public class Day12a {
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
    File text = new File("/Users/axelteo/Desktop/AdventofCode/Day12input.txt");
    Scanner scanner = myScanner(text);
    if (scanner == null) {
      return;
    }

    int ROWS = 41;
    int COLS = 114;
    Square[][] heightMap = new Square[ROWS][COLS];
    Square startSquare = null;

    String line;
    int row = 0;
    while ((line = myRead(scanner)) != null) {
      int col = 0;
      for (int i = 0; i < line.length(); ++i) {
        char c = line.charAt(i);
	if (c == 'S') {
	  startSquare = new Square(row, col, 0, false);
	  heightMap[row][col] = startSquare;
	} else if (c == 'E') {
	  heightMap[row][col] = new Square(row, col, 26, true);
	} else if (c != '\n') {
	  heightMap[row][col] = new Square(row, col, c - 'a' + 1, false);
	}
	++col;
      }
      ++row;
    }

    Queue<Square> q = new LinkedList<>();
    startSquare.isPassed();
    q.add(startSquare);
    int steps = 0;
    int counter = 1;
    int nextCounter;
    while (counter > 0) {
      Square tmp;
      nextCounter = 0;
      while (counter > 0) {
        tmp = q.poll();
        if (tmp.isEnd()) {
	  System.out.println("Steps: " + steps);
	  return;
	}
        nextCounter += tmp.addNewAdj(q, heightMap, ROWS, COLS);
	--counter;
      }
      counter = nextCounter;
      steps++;
    }
    System.out.println("no way to End");
  } 
}

class Square {
  int row;
  int col;
  int height;
  boolean passed;
  boolean isEnd;

  public Square(int x, int y, int height, boolean isEnd) {
    this.row = x;
    this.col = y;
    this.height = height;
    this.passed = false;
    this.isEnd = isEnd;
  }

  public int addNewAdj(Queue<Square> q, Square[][] heightMap, int rows, int cols) {
    Square tmp;
    int counter = 0;
    if (row - 1 >= 0) {
      tmp = heightMap[row - 1][col];
      if (!tmp.passed && this.isClimable(tmp)) {
        tmp.isPassed();
        q.add(tmp);
	++counter;
      }
    }
    if (col - 1 >= 0) {
      tmp = heightMap[row][col - 1];
      if (!tmp.passed && this.isClimable(tmp)) {
        tmp.isPassed();
        q.add(tmp);
	++counter;
      }
    }
    if (row + 1 < rows) {
      tmp = heightMap[row + 1][col];
      if (!tmp.passed && this.isClimable(tmp)) {
        tmp.isPassed();
        q.add(tmp);
	++counter;
      }
    }
    if (col + 1 < cols) {
      tmp = heightMap[row][col + 1];
      if (!tmp.passed && this.isClimable(tmp)) {
        tmp.isPassed();
        q.add(tmp);
	++counter;
      }
    }
    return counter;
  }

  private boolean isClimable(Square next) {
    int heightDiff = next.height - this.height;
    if (heightDiff <= 1) {
      return true;
    }
    return false;
  }

  public boolean isEnd() {
    return this.isEnd;
  }

  public void isPassed() {
    this.passed = true;
  }

  public int getHeight() {
    return this.height;
  }

  @Override
  public String toString() {
    return Integer.toString(this.height);
  }
}

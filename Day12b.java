import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.util.LinkedList;
import java.util.Queue;

public class Day12b {
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

    // initialise minSteps to an arbitrary large number.
    int minSteps = 1000000;
    for (int i = 0; i < ROWS; ++i) {
      for (int j = 0; j < COLS; ++j) {
        if (heightMap[i][j].isA()) {
	  int currSteps = stepsToEnd(heightMap[i][j], heightMap, ROWS, COLS);
	  if (currSteps > 0 && currSteps < minSteps) {
	    minSteps = currSteps;
	  }
	} 
      }
    }

    System.out.println("min steps to E is: " + minSteps);
  }

  public static int stepsToEnd(Square startSquare, Square[][] heightMap, int ROWS, int COLS) {
    for (int i = 0; i < ROWS; ++i) {
      for (int j = 0; j < COLS; ++j) {
        heightMap[i][j].resetPassed();
      }
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
	  return steps;
	}
        nextCounter += tmp.addNewAdj(q, heightMap, ROWS, COLS);
	--counter;
      }
      counter = nextCounter;
      steps++;
    }
    return -1;
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

  public boolean isA() {
    if (this.height == 1) {
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

  public void resetPassed() {
    this.passed = false;
  }

  public int getHeight() {
    return this.height;
  }

  @Override
  public String toString() {
    return Integer.toString(this.height);
  }
}

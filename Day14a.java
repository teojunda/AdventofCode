import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;

public class Day14a {
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
    File text = new File("/Users/axelteo/Desktop/AdventofCode/Day14input.txt");
    Scanner scanner = myScanner(text);
    if (scanner == null) {
      return;
    }

    int[][] occupied = new int[200][600];
    int yLimit = 0;

    String line;
    while ((line = myRead(scanner)) != null) {
      String[] coordList = line.split(" -> ", 0);
      Coord curr, next;
      String[] xyCoord;
      xyCoord = coordList[0].split(",", 0);
      if (Integer.valueOf(xyCoord[1]) > yLimit) {
        yLimit = Integer.valueOf(xyCoord[1]);
      }
      curr = new Coord(Integer.valueOf(xyCoord[0]), Integer.valueOf(xyCoord[1]));
      curr.addTo(occupied);

      for (int i = 1; i < coordList.length; ++i) {
        xyCoord = coordList[i].split(",", 0);
        if (Integer.valueOf(xyCoord[1]) > yLimit) {
          yLimit = Integer.valueOf(xyCoord[1]);
        }
        next = new Coord(Integer.valueOf(xyCoord[0]), Integer.valueOf(xyCoord[1]));
	ArrayList<Coord> tmp = curr.rocksFromTo(next);
	for (Coord x: tmp) {
	  x.addTo(occupied);
	}
	curr = next;
      }
    }

    Coord newSand;
    Coord spawn = new Coord(500, 0);
    int count = 0;
    while ((newSand = Coord.restsAt(spawn, occupied, yLimit)) != null) {
      ++count;
      newSand.addTo(occupied);
    }

    System.out.println("units of sand that rest: " + count);
  }
}

class Coord {
  int x;
  int y;

  public Coord(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public void addTo(int[][] occupied) {
    occupied[this.y][this.x] = 1;
  }

  // returns arrayList of Coords between this and next, excluding this.
  public ArrayList<Coord> rocksFromTo(Coord next) {
    ArrayList<Coord> res = new ArrayList<>();
    int xDiff = next.x - this.x;
    int yDiff = next.y - this.y;
    for (int i = 0; i < xDiff; ++i) {
      res.add(new Coord(this.x + i + 1, this.y));
    }
    for (int i = 0; i > xDiff; --i) {
      res.add(new Coord(this.x + i - 1, this.y));
    }
    for (int i = 0; i < yDiff; ++i) {
      res.add(new Coord(this.x, this.y + i + 1));
    }
    for (int i = 0; i > yDiff; --i) {
      res.add(new Coord(this.x, this.y + i - 1));
    }
    return res;
  }

  public static Coord restsAt(Coord start, int[][] grid, int yLimit) {
    if (start.y > yLimit) {
      return null;
    }
    if (grid[start.y+1][start.x] == 0) {
      return Coord.restsAt(new Coord(start.x, start.y + 1), grid, yLimit);
    }
    else if (grid[start.y+1][start.x-1] == 0) {
      return Coord.restsAt(new Coord(start.x-1, start.y + 1), grid, yLimit);
    }
    else if (grid[start.y+1][start.x+1] == 0) {
      return Coord.restsAt(new Coord(start.x+1, start.y + 1), grid, yLimit);
    } else {
      return start;
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Coord)) {
      return false;
    }
    Coord some = (Coord) obj;
    if (this.x == some.x && this.y == some.y) {
      return true;
    }
    return false;
  }

  @Override
  public String toString() {
    return "(" + this.x + "," + this.y + ")";
  }
}

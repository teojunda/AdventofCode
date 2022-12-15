import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.util.LinkedList;
import java.util.Queue;

public class Day15a {
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

  public static int toInt(String line) {
    String res = "";
    for (int i = 0; i < line.length(); ++i) {
      char c = line.charAt(i);
      if (Character.isDigit(c) || c == '-') {
        res += c;
      }
    }
    return Integer.valueOf(res);
  }

  public static void main(String[] args) {
    File text = new File("/Users/axelteo/Desktop/AdventofCode/Day15input.txt");
    Scanner scanner = myScanner(text);
    if (scanner == null) {
      return;
    }

    int ROW = 2000000;
    ArrayList<Range> ranges = new ArrayList<>();
    String line;
    while ((line = myRead(scanner)) != null) {
      String[] tokens = line.split(" ", 0);
      Coord sensor, beacon;
      sensor = new Coord(toInt(tokens[2]), toInt(tokens[3]));
      beacon = new Coord(toInt(tokens[8]), toInt(tokens[9]));

      int dist = sensor.mhDist(beacon);
      // x + (sensor.row - y) <= dist
      int nomRange = dist - Math.abs(sensor.row - ROW);
      if (nomRange <= 0) {
        continue;
      }

      Range curr = new Range(0, 0);
      if (beacon.row != ROW) {
        curr = new Range(sensor.col - nomRange, sensor.col + nomRange);
      } else if (beacon.col == sensor.col - nomRange) {
        curr = new Range(sensor.col - nomRange + 1, sensor.col + nomRange);
      } else if (beacon.col == sensor.col + nomRange) {
        curr = new Range(sensor.col - nomRange, sensor.col + nomRange - 1);
      }

      ArrayList<Range> currRange = new ArrayList<>();
      currRange.add(curr);
      for (Range prev: ranges) {
        ArrayList<Range> tmp = new ArrayList<>();
	for (Range x: currRange) {
	  tmp.addAll(prev.unOverlapRanges(x));
	}
	currRange = tmp;
      }

      ranges.addAll(currRange);
    }
    int count = 0;
    for (Range x: ranges) {
      System.out.println(x);
      count += x.size();
    }
    System.out.println(count + " positions cannot contain a beacon in row " + ROW);
  }

}

class Coord {
  int col;
  int row;

  public Coord(int col, int row) {
    this.col = col;
    this.row = row;
  }

  public int mhDist(Coord x) {
    return Math.abs(this.col - x.col) + Math.abs(this.row - x.row);
  }

  @Override
  public String toString() {
    return "(" + this.col + "," + this.row + ")";
  }
}

class Range {
  int start;
  int end;

  public Range(int start, int end) {
    this.start = start;
    this.end = end;
  }

  public int size() {
    return this.end - this.start + 1;
  }

  public ArrayList<Range> unOverlapRanges(Range newRange) {
    ArrayList<Range> res = new ArrayList<>();
    if (this.end < newRange.start || newRange.end < this.start) {
      res.add(newRange);
      return res;
    }
    if (newRange.start >= this.start && newRange.end <= this.end) {
      return res;
    }
    if (newRange.start < this.start && newRange.end > this.end) {
      res.add(new Range(newRange.start, this.start - 1));
      res.add(new Range(this.end + 1, newRange.end));
      return res;
    }
    if (newRange.start < this.start && newRange.end <= this.end) {
      res.add(new Range(newRange.start, this.start - 1));
      return res;
    }
    if (newRange.start >= this.start && newRange.end > this.end) {
      res.add(new Range(this.end + 1, newRange.end));
      return res;
    }
    return res;
  }

  @Override
  public String toString() {
    return "(" + this.start + " - " + this.end + ")";
  }
}

import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.util.LinkedList;
import java.util.Queue;

public class Day15b {
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

  public static int notInRanges(ArrayList<Range> ranges, int low, int high) {
    Range curr = new Range(low, high);
    ArrayList<Range> newRanges = new ArrayList<>();
    newRanges.add(curr);
    for (Range prev: ranges) {
      ArrayList<Range> tmp = new ArrayList<>();
      for (Range x: newRanges) {
        tmp.addAll(prev.unOverlapRanges(x));
      }
      newRanges = tmp;
      if (tmp.size() == 0) {
        return -1;
      }
    }
    return newRanges.get(0).start;

  }

  public static ArrayList<Range> colRangeForRow(int ROW, Coord sensor, Coord beacon, ArrayList<Range> prevRanges) {
    int dist = sensor.mhDist(beacon);
    // x + (sensor.row - y) <= dist
    int nomRange = dist - Math.abs(sensor.row - ROW);
    if (nomRange <= 0) {
      return null;
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
    for (Range prev: prevRanges) {
      ArrayList<Range> tmp = new ArrayList<>();
      for (Range x: currRange) {
        tmp.addAll(prev.unOverlapRanges(x));
      }
      currRange = tmp;
    }
    return currRange;
  }

  public static void main(String[] args) {
    File text = new File("/Users/axelteo/Desktop/AdventofCode/Day15input.txt");
    Scanner scanner = myScanner(text);
    if (scanner == null) {
      return;
    }

    ArrayList<Coord> beacons = new ArrayList<>();
    ArrayList<Range>[] colRangeForRow = new ArrayList[4000000];
    for (int i = 0; i < 4000000; ++i) {
      colRangeForRow[i] = new ArrayList<Range>();
    }
    String line;
    while ((line = myRead(scanner)) != null) {
      String[] tokens = line.split(" ", 0);
      Coord sensor, beacon;
      sensor = new Coord(toInt(tokens[2]), toInt(tokens[3]));
      beacon = new Coord(toInt(tokens[8]), toInt(tokens[9]));
      beacons.add(beacon);

      for (int i = 0; i < 4000000; ++i) {
        ArrayList<Range> tmp = colRangeForRow(i, sensor, beacon, colRangeForRow[i]);
        if (tmp != null) {
	  colRangeForRow[i].addAll(tmp);
	}
      }
    }

    int LIMIT = 4000000;
    int beaconCol = -1;
    for (int i = 0; i < LIMIT; ++i) {
      beaconCol = notInRanges(colRangeForRow[i], 0, LIMIT);
      if (beaconCol != -1 && !beacons.contains(new Coord(beaconCol, i))) {
        System.out.println("Beacon is at row " + i + ", col " + beaconCol);
	long score = (long) beaconCol * (long) 4000000 + (long) i;
	System.out.println("score is " + score);
	return;
      }
    }
    System.out.println("not here?");
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
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Coord)) {
      return false;
    }
    Coord some = (Coord) obj;
    if (some.col == this.col && some.row == this.row) {
      return true;
    }
    return false;
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

  public boolean inRange(int x) {
    if (x >= this.start && x <= this.end) {
      return true;
    }
    return false;
  }

  @Override
  public String toString() {
    return "(" + this.start + " - " + this.end + ")";
  }
}

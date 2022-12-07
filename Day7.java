import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;

public class Day7 {
  public static String myRead(Scanner scanner) {
    String line;
    try {
      line = scanner.nextLine().trim();
    } catch (Exception e) {
      line = null;
    }
    return line;
  }

  public static void main(String[] args) {
    File text = new File("/Users/axelteo/Desktop/Day7input.txt");
    Scanner scanner;
    try {
      scanner = new Scanner(text);
    } catch (Exception e) {
      System.out.println("No scanner");
      return;
    }

    String line = scanner.nextLine().trim();
    String[] inputs;
    ArrayList<Dir> path = new ArrayList<>();
    path.add(new Dir("/"));
    while (line != null) {
      // possible lines are "$ cd x", "$ cd ..", "$ ls", "1212 abc", and "dir abc"
      inputs = line.split(" ", 0);
      if (!inputs[0].equals("$")) {
        System.out.println("broken");
        break;
      }

      if (inputs[1].equals("cd")) {
        if (inputs[2].equals("..")) {
	  path.remove(path.size() - 1);
	} else {
          Dir curr = path.get(path.size() - 1);
          for (Dir d: curr.getList()) {
	    if (d.getName().equals(inputs[2])) {
	      path.add(d);
	      break;
	    }
	  }
	}
	line = myRead(scanner);
      }

      if (inputs[1].equals("ls")) {
        line = scanner.nextLine().trim();
	while (line != null) {
          inputs = line.split(" ", 0);
	  if (inputs[0].equals("$")) {
	    break;
	  }
	  Dir curr = path.get(path.size() - 1);
	  if (inputs[0].equals("dir")) {
	    curr.add(new Dir(inputs[1]));
	  } else {
	    curr.add(Integer.valueOf(inputs[0]));
	  }
	  line = myRead(scanner);
	}
      }
    }
    Dir curr = path.get(0);
    int currSize = curr.getTotalSize();
    int diskSpace = 70000000;
    int freeSpace = diskSpace - currSize;
    curr.printAllTotalSizesMin(30000000 - freeSpace);
    return;
  }
}

class Dir {
  String name;
  int totalSize;
  int fileSize;
  ArrayList<Dir> arr;

  public Dir(String name) {
    this.name = name;
    this.totalSize = -1;
    this.fileSize = 0;
    this.arr = new ArrayList<>();
  }

  public void add(int num) {
    this.fileSize += num;
  }

  public void add(Dir dir) {
    this.arr.add(dir);
  }

  public int getTotalSize() {
    if (this.totalSize == -1) {
      int tmp = 0;
      for (Dir d: arr) {
        tmp += d.getTotalSize();
      }
      tmp += this.fileSize;
      this.totalSize = tmp;
    }
    return this.totalSize;
  }

  public String getName() {
    return this.name;
  }

  public ArrayList<Dir> getList() {
    return this.arr;
  }

  public void printAllTotalSizesLimit(int n) {
    if (this.totalSize < n) {
      System.out.println(this.name + ": " + this.totalSize);
    }
    for(Dir d: this.arr) {
      d.printAllTotalSizesLimit(n);
    }
  }

  public void printAllTotalSizesMin(int n) {
    if (this.totalSize > n) {
      System.out.println(this.name + ": " + this.totalSize);
    }
    for(Dir d: this.arr) {
      d.printAllTotalSizesMin(n);
    }
  }
}

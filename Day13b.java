import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;

public class Day13b {
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
    File text = new File("/Users/axelteo/Desktop/AdventofCode/Day13input.txt");
    Scanner scanner = myScanner(text);
    if (scanner == null) {
      return;
    }

    String line1;
    String line2;
    ArrayList<Integer> orders = new ArrayList<>();
    ValOrList divider1 = new ValOrList().add(new ValOrList().add(new ValOrList(2)));
    int divider1pos = 1;
    int divider2pos = 2;
    ValOrList divider2 = new ValOrList().add(new ValOrList().add(new ValOrList(6)));
    while ((line1 = myRead(scanner)) != null && (line2 = myRead(scanner)) != null) {
      ValOrList left = ValOrList.createNew(line1);
      ValOrList right = ValOrList.createNew(line2);
      if (left.isRightOrder(divider1) > 0) {
        ++divider1pos;
      }
      if (right.isRightOrder(divider1) > 0) {
        ++divider1pos;
      }
      if (left.isRightOrder(divider2) > 0) {
        ++divider2pos;
      }
      if (right.isRightOrder(divider2) > 0) {
        ++divider2pos;
      }
      // discard the empty line
      myRead(scanner);
    }

    System.out.println("product of divider positions: " + (divider1pos * divider2pos));
  }
}

class ValOrList {
  final int val;
  ArrayList<ValOrList> vlist;

  public ValOrList(int x) {
    this.val = x;
    this.vlist = null;
  }

  public ValOrList() {
    this.val = -1;
    this.vlist = new ArrayList<>();
  }

  public ValOrList add(ValOrList x) {
    this.vlist.add(x);
    return this;
  }

  public static ValOrList createNew(String line) {
    ArrayList<ValOrList> path = new ArrayList<>();
    // skip the last close bracket.
    for (int i = 0; i < line.length() - 1; ++i) {
      char c = line.charAt(i);
      if (c == '[') {
        ValOrList tmp = new ValOrList();
	if (path.isEmpty()) {
	  path.add(tmp);
	} else {
          path.get(path.size() - 1).add(tmp);
          path.add(tmp);
        }
	continue;
      } else if (c == ']') {
        path.remove(path.size() - 1);
	continue;
      } else if (c == '1') {
        if (line.charAt(i + 1) == '0') {
	  path.get(path.size() - 1).add(new ValOrList(10));
	  ++i;
	  continue;
	} else {
	  path.get(path.size() - 1).add(new ValOrList(Character.getNumericValue(c)));
	  continue;
	}
      } else if (Character.isDigit(c)) {
        path.get(path.size() - 1).add(new ValOrList(Character.getNumericValue(c)));
	continue;
      } else if (c == ',') {
        continue;
      }
    }
    return path.get(0);
  }

  // positive int return means right order: left side is smaller, or left has fewer items
  // 0 return means equality
  // negative int return means wrong order: right side is smaller, or right has fewer items
  public int isRightOrder(ValOrList right) {
    if (this.val != -1 && right.val != -1) {
      return right.val - this.val;
    }
    else if(this.val != -1) {
      int res = right.vlist.size() - 1;
      int res2;
      if (right.vlist.size() > 0 && (res2 = this.isRightOrder(right.vlist.get(0))) != 0) {
        return res2;
      }
      return res;
    }
    else if (right.val != -1) {
      int res = 1 - this.vlist.size();
      int res2;
      if (this.vlist.size() > 0 && (res2 = this.vlist.get(0).isRightOrder(right)) != 0) {
        return res2;
      }
      return res;
    }
    int res = right.vlist.size() - this.vlist.size();
    int min = right.vlist.size() < this.vlist.size() ? right.vlist.size() : this.vlist.size();
    for (int i = 0; i < min; ++i) {
      int res2;
      if ((res2 = this.vlist.get(i).isRightOrder(right.vlist.get(i))) != 0) {
        return res2;
      }
    }
    return res;
  }

  @Override
  public String toString() {
    if (this.val != -1) {
      return Integer.toString(this.val);
    }
    String line = "";
    for (ValOrList x: this.vlist) {
      line += x.toString() + " ";
    }
    return line;
  }
}

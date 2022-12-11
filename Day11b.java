import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.util.function.Function;

// will find the lowest common demoninator of all test values
// and modulo worry level by lcd to manage it
public class Day11b {
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

  public static long gcd(long a, long b) {
    while (b > 0) {
      long temp = b;
      b = a % b; // % is remainder
      a = temp;
    }
    return a;  
  }

  private static long lcm(long a, long b) {
    return a * (b / gcd(a, b));
  }

  public static void main(String[] args) {
    File text = new File("/Users/axelteo/Desktop/AdventofCode/Day11input.txt");
    Scanner scanner = myScanner(text);
    if (scanner == null) {
      return;
    }

    String line;
    ArrayList<Long> testValues = new ArrayList<>();
    ArrayList<Monkey> monkeys = new ArrayList<>();
    while (myRead(scanner) != null) {
      Monkey curr = new Monkey();
      curr.setItems(myRead(scanner));
      curr.setOp(myRead(scanner));
      curr.setTest(line = myRead(scanner));
      testValues.add(Long.valueOf(line.split(" ", 0)[3]));
      curr.setPosNeg(myRead(scanner), myRead(scanner));
      myRead(scanner);
      monkeys.add(curr);
    }

    long testsLCM = 1;
    for (long i: testValues) {
      testsLCM = lcm(testsLCM, i);
    }

    System.out.println("LCM: " + testsLCM);

    for (int i = 0; i < 10000; ++i) {
      for (Monkey m: monkeys) {
        m.inspectItems(testsLCM);
	m.throwItems(monkeys);
      }
    }

    for (Monkey tmp: monkeys) {
      System.out.println("Monkey i:" + tmp.getInspections());
    }
  }
}

class Monkey {
  ArrayList<Item> items;
  Function<Long, Long> op;
  Function<Long, Boolean> test;
  int pos;
  int neg;
  int inspections;

  public Monkey() {
    this.items = null;
    this.op = null;
    this.test = null;
    this.pos = -1;
    this.neg = -1;
    this.inspections = 0;
  }

  public int getInspections() {
    return this.inspections;
  }

  public void setItems(String itemList) {
    ArrayList<Item> itemArr = new ArrayList<>();
    String[] arr = itemList.split(" ", 0);
    int arrLength = arr.length;
    for (int i = 2; i < arrLength; ++i) {
      String tmp = "";
      for (int j = 0; j < arr[i].length(); ++j) {
        if (Character.isDigit(arr[i].charAt(j))) {
	  tmp += arr[i].charAt(j);
	}
      }
      itemArr.add(new Item(Long.valueOf(tmp)));
    }
    this.items = itemArr;
  }

  public void setOp(String op) {
    String[] ops = op.split(" ", 0);
    if (ops[4].equals("+")) {
      this.op = x -> x + Integer.valueOf(ops[5]);
    } else if (ops[4].equals("*")) {
      if (ops[5].equals("old")) {
        this.op = x -> x * x;
      } else {
        this.op = x -> x * Integer.valueOf(ops[5]);
      }
    }
  }

  public void setTest(String test) {
    String[] tests = test.split(" ", 0);
    this.test = x -> x % Integer.valueOf(tests[3]) == 0;
  }

  public void setPosNeg(String pos, String neg) {
    this.pos = Integer.valueOf(pos.split(" ", 0)[5]);
    this.neg = Integer.valueOf(neg.split(" ", 0)[5]);
  }

  public void inspectItems(long testsLCM) {
    for (Item i: this.items) {
      i.inspect(this.op, testsLCM);
      this.inspections += 1;
    }
  }

  public void throwItems(ArrayList<Monkey> monkeys) {
    ArrayList<Item> toPos = new ArrayList<>();
    ArrayList<Item> toNeg = new ArrayList<>();
    for (Item i: this.items) {
      if (!i.isInspected()) {
        continue;
      }
      i.thrown();
      if (i.check(this.test)) {
        toPos.add(i);
      } else {
        toNeg.add(i);
      }
    }
    this.items.removeAll(toPos);
    this.items.removeAll(toNeg);
    monkeys.get(this.pos).catchItems(toPos);
    monkeys.get(this.neg).catchItems(toNeg);
  }

  public void catchItems(ArrayList<Item> i) {
    this.items.addAll(i);
  }

  @Override
  public String toString() {
    String tmp = "";
    for (Item i: this.items) {
      tmp += i.worryLvl + " ";
    }
    return "items: " + tmp + " pos: " + this.pos + " neg: " + this.neg;
  }
}

class Item {
  long worryLvl;
  boolean inspected;

  public Item(long worryLvl) {
    this.worryLvl = worryLvl;
    this.inspected = false;
  }

  public boolean isInspected() {
    return this.inspected;
  }

  public Item inspect(Function<Long, Long> fn, long testsLCM) {
    this.worryLvl = fn.apply(this.worryLvl) % testsLCM;
    this.inspected = true;
    return this;
  }

  public boolean check(Function<Long, Boolean> fn) {
    return fn.apply(this.worryLvl);
  }

  public void thrown() {
    this.inspected = false;
  }
}

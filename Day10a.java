import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;

public class Day10a {
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
    File text = new File("/Users/axelteo/Desktop/AdventofCode/Day10input.txt");
    Scanner scanner = myScanner(text);
    if (scanner == null) {
      return;
    }

    String line;
    String[] inputs;
    CRT curr = new CRT(1, 1);
    int sigSum = 0;

    while ((line = myRead(scanner)) != null) {
      inputs = line.split(" ", 0);
      if (inputs[0].equals("noop")) {
        sigSum += curr.sigStrength();
        curr = curr.noop();
      } else {
        for (int step = 0; step < 2; ++step) {
	  sigSum += curr.sigStrength();
	  curr = curr.addX(step, Integer.valueOf(inputs[1]));
	}
      }
    }

    System.out.println("Sum of signal strengths: " + sigSum);
  }
}

class CRT {
  int xVal;
  int clockCycle;

  public CRT(int x, int cc) {
    this.xVal = x;
    this.clockCycle = cc;
  }

  public int sigStrength() {
    if (this.clockCycle <= 220 && this.clockCycle % 40 == 20) {
      return this.xVal * this.clockCycle;
    }
    return 0;
  }

  // simulates one clock cycle of noop instruction
  public CRT noop() {
    return new CRT(this.xVal, this.clockCycle + 1);
  }

  // simulates one clock cycle of a addx instruction
  // since addx takes 2 cycles, addX will only occur when cycle is 1
  public CRT addX(int cycle, int x) {
    if (cycle == 1) {
      return new CRT(this.xVal + x, this.clockCycle + 1);
    } else {
      return new CRT(this.xVal, this.clockCycle + 1);
    }
  }

}

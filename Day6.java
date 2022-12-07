import java.io.*;
import java.util.Scanner;
import java.io.File;

public class Day6 {
  // check if all element in char[] are different and initialised.
  public static boolean checkDiff(char[] charL) {
    for (int i = 0; i < charL.length; ++i) {
      if (charL[i] == '\0') {
        return false;
      }
    }
    for (int i = 0; i < charL.length; ++i) {
      for (int j = i + 1; j < charL.length; ++j) {
        if (charL[i] == charL[j]) {
	  return false;
	}
      }
    }
    return true;
  }

  public static void main(String[] args) {
    char[] txt = new char[14];
    for (int i = 0; i < 14; ++i) {
      txt[i] = '\0';
    }
    int count = 0;
    File text = new File("/Users/axelteo/Desktop/input.txt");
    try {
      Scanner scanner = new Scanner(text);
      String line;
      while (scanner.hasNext()) {
        line = scanner.next();
        int length = line.length();
        for (int i = 0; i < length; ++i) {
          ++count;
          txt[i % 14] = line.charAt(i);
          if (checkDiff(txt)) {
            System.out.println(count);
	    return;
	  }
        }
      }
    } catch (Exception e) {
      return;
    }
  }
}

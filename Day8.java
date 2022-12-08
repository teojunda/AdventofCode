import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;

public class Day8 {
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
    File text = new File("/Users/axelteo/Desktop/AdventofCode/Day8input.txt");
    Scanner scanner = myScanner(text);
    if (scanner == null) {
      return;
    }

    int[][] input = new int[150][150];
    for (int i = 0; i < 150; ++i) {
      for (int j = 0; j < 150; ++j) {
        input [i][j] = -1;
      }
    }

    // Reading tree heights into a 2d array
    String line;
    int lineCount = 0;
    while ((line = myRead(scanner)) != null) {
      int lineLen = line.length();
      for (int col = 0; col < lineLen; ++col) {
        input[lineCount][col] = Character.getNumericValue(line.charAt(col));
      }
      ++lineCount;
    }

    // Part 1 solution
    int visTrees = numVisible(input);
    System.out.println("num of visible trees: " + visTrees);

    // Part 2 Solution
    int maxScore = 0;
    int posRow = 0;
    int posCol = 0;
    for (int i = 0; i < 150; ++i) {
      for (int j = 0; j < 150; ++j) {
        int tmp = getScore(input, i, j);
	if (tmp > maxScore) {
	  maxScore = tmp;
	  posRow = i;
	  posCol = j;
	}
      }
    }
    System.out.println("max score is: " + maxScore);
  }

  public static int getScore(int[][] input, int row, int col) {
    int top, down, left, right;
    top = down = left = right = 0;
    int height = input[row][col];
    for (int i = row - 1; i >= 0; --i) {
      if (input[i][col] == -1) {
        break;
      }
      ++left;
      if (height <= input[i][col]) {
        break;
      }
    }
    for (int i = row + 1; i < 150 ; ++i) {
      if (input[i][col] == -1) {
        break;
      }
      ++right;
      if (height <= input[i][col]) {
        break;
      }
    }
    for (int i = col - 1; i >= 0; --i) {
      if (input[row][i] == -1) {
        break;
      }
      ++top;
      if (height <= input[row][i]) {
        break;
      }
    }
    for (int i = col + 1; i < 150; ++i) {
      if (input[row][i] == -1) {
        break;
      }
      ++down;
      if (height <= input[row][i]) {
        break;
      }
    }
    return top * down * left * right;
  }

  public static int numVisible(int[][] input) {
   int[][] output = new int[150][150];
   for (int i = 0; i < 150; ++i) {
     for (int j = 0; j < 150; ++j) {
       output[i][j] = 0;
     }
   }

   for (int row = 0; row < 150; ++row) {
     int highest = -1;
     for (int col = 0; col < 150; ++col) {
       if (input[row][col] > highest) {
         output[row][col] = 1;
	 highest = input[row][col];
       }
     }
   }
   for (int row = 0; row < 150; ++row) {
     int highest = -1;
     for (int col = 149; col >= 0; --col) {
       if (input[row][col] > highest) {
         output[row][col] = 1;
	 highest = input[row][col];
       }
     }
   }
   for (int col = 0; col < 150; ++col) {
     int highest = -1;
     for (int row = 0; row < 150; ++row) {
       if (input[row][col] > highest) {
         output[row][col] = 1;
	 highest = input[row][col];
       }
     }
   }
   for (int col = 0; col < 150; ++col) {
     int highest = -1;
     for (int row = 149; row >= 0; --row) {
       if (input[row][col] > highest) {
         output[row][col] = 1;
	 highest = input[row][col];
       }
     }
   }

   int visNum = 0;
   for (int i = 0; i < 150; ++i) {
     for (int j = 0; j < 150; ++j) {
       if (output[i][j] == 1) {
         ++visNum;
       }
     }
   }

   return visNum;
  }
}

import java.io.*;

public class Day5 {
  public static void main(String[] args) {

    Console cnsl = System.console();
    Stack[] stacks = new Stack[9];
    for (int i = 0; i < 9; ++i) {
      stacks[i] = new Stack();
    }

    String start = cnsl.readLine();
    while (start.length() > 2) {
      for (int i = 0; i < (start.length() / 4) + 1; ++i) {
        if (start.charAt(i*4) == '[') {
	  Box tmp = new Box(start.charAt(i*4 + 1));
	  stacks[i].add(tmp);
	}
      }
      start = cnsl.readLine();
    }
    for (int i = 0; i < 9; ++i) {
      stacks[i].reverse();
    }

    String command = cnsl.readLine();
    int num, from, to;
    num = from = to = 0;
    while (command.length() > 2) {
      String[] input = command.split(" ", 0);
      num = Integer.valueOf(input[1]);
      from = Integer.valueOf(input[3]);
      to = Integer.valueOf(input[5]);
      
      // Solution from part 1
      /*
      for (int i = 0; i < num; ++i) {
        stacks[to - 1].add(stacks[from - 1].remove());
      }
      */

      // Solution for part 2
      Stack tmp = new Stack();
      for (int i = 0; i < num; ++i) {
        tmp.add(stacks[from - 1].remove());
      }
      tmp.reverse();
      stacks[to - 1].add(tmp);

      command = cnsl.readLine();
    }

    for (int i = 0; i < 9; ++i) {
      System.out.println("stack " + (i+1) + " starting from top: " + stacks[i]);
    }
  }
}

class Box {
  char letter;
  Box under;
  
  public Box(char x) {
    this.letter = x;
    this.under = null;
  }

  public void over(Box x) {
    this.under = x;
  }

  public Box under() {
    return this.under;
  }

  public String toString() {
    return String.valueOf(this.letter);
  }
}

class Stack {
  Box top;

  public Stack() {
    this.top = null;
  }

  public void add(Box x) {
    x.over(this.top);
    this.top = x;
  }

  public void add(Stack x) {
    Box head = x.top;
    Box curr = head;
    while (curr.under() != null) {
      curr = curr.under();
    }
    curr.over(this.top);
    this.top = head;
  }

  public Box remove() {
    Box res = this.top;
    this.top = res.under();
    return res;
  }

  public void reverse() {
    Box prev = null;
    Box curr = this.top;
    Box next = null;

    while (curr != null) {
      next = curr.under();
      curr.over(prev);
      prev = curr;
      curr = next;
    }

    this.top = prev;
  }

  public String toString() {
    String res = "";
    Box tmp = this.top;
    while (tmp != null) {
      res += tmp.toString();
      tmp = tmp.under();
    }
    return res;
  }
}

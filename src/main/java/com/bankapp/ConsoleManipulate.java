package main.java.com.bankapp;

public class ConsoleManipulate {
  // Class that will delete a certain number of lines in the console

  public static void clearLines(int i) {

    for (int j = 0; j < i; j++) {
      System.out.print(String.format("\033[%dA", 1)); // Move up
      System.out.print("\033[2K"); // Erase line content
    }
  }

  public static void clearLine() {
    System.out.print("\033[2K"); // Erase line content
  }

  public static void moveUp(int i) {
    System.out.print(String.format("\033[%dA", i)); // Move up
  }

  public static void moveTo(int i, int k) {
    System.out.print(String.format("\033[%dA", i)); // Move up
    System.out.print(String.format("\033[%dC", k)); // Move right
  }

  public static void moveRight(int i) {
    System.out.print(String.format("\033[%dC", i));
  }

  public static void clearAll() {
    System.out.print("\033[H\033[2J");
  }
}

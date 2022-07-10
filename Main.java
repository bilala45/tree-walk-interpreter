package lox_interpreter;

public class Main {

  public static void main(String[] args) {
    if (args.length > 1) {
      // exit program - too many arguments
      System.exit(1);
    } else if (args.length == 1) {
      // runs file path passed in as argument
      Run.runFile(args[0]);
    } else {
      // runs interpreter interactively
      Run.runPrompt();
    }
  }
}

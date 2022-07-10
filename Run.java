package lox_interpreter;

import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.charset.Charset;
import java.io.IOException;

public class Run {

  // method to run file path specified in command line call
  public static void runFile(String filePath) throws IOException {
    // convert input string to Path object
    Path file = Paths.get(filePath);

    // ensure file path is valid
    if (Files.exists(file)) {
      // scans characters from file and saves in a byte array
      // file contents are stored in a byte array because function reads bytes
      // readAllBytes throws IOException
      byte[] fileBytes = Files.readAllBytes(file);
      // convert byte array to String using JVM's default Charset
      String fileAsString = new String(fileBytes, Charset.defaultCharset());
      // run file contents
      System.out.println(fileAsString);
      // run(fileAsString);
    } else {
      System.out.println("Invalid file path.");
      System.exit(1);
    }
  }

  // method to run interactive prompt for interpreter
  // we'll fill this in later
  public static void runPrompt() {
    System.out.println("runPrompt working");
  }
}

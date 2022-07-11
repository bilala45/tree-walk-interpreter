package lox_interpreter;

import java.util.List;
import java.util.ArrayList;

public class Scanner {

  // input text
  private String input;
  // list to hold tokens
  private List<Token> tokens;
  // curr tracks current position in lexeme
  // start tracks the start of the lexeme
  private int start, curr;
  // track line in file
  private int line;

  // constructor
  public Scanner(String input) {
    this.input = input;
    // initialize tokens as ArrayList
    // dynamic resizing allows for variable input strings
    tokens = new ArrayList<Token>();
    // initialize line to start at 1
    line = 1;
    // initialize start and curr to 0
    start = curr = 0;
  }

  // scans input string to generate tokens
  public List<Token> scanTokens() {
    // iterate through input string until we reach the end
    while (curr < input.length()) {
      // move start to start of new lexeme (where curr is currently pointing to)
      start = curr;
      // call method to scan individual lexeme
      scanSingleToken();
    }

    // add EOF token to tokens array
    // EOF has no associated string representation, marked by reaching the end of the input string
    // if you print out the lexemes associated with your token, EOF will result in an empty line
    tokens.add(new Token(TokenType.EOF, "", line));
    return tokens;
  }

  // scan single token
  private void scanSingleToken() {
    // fill with switch statements that check string and token pairs
    // store char at current index of string AND THEN increment char
    /* same as writing the following:
     * char currChar = input.charAt(curr);
     * curr++;
     */
    char currChar = input.charAt(curr++);
    // check currChar against single character options
    // if currChar matches option, add corresponding token to list
    switch (currChar) {
      // handle skippable characters
      case ' ': break;
      case '\n': line++; break;
      case '\t': break;
      // match single characters
      case '(': addTokenToList(TokenType.LEFT_PAR); break;
      case ')': addTokenToList(TokenType.RIGHT_PAR); break;
      case '{': addTokenToList(TokenType.LEFT_BRACE); break;
      case '}': addTokenToList(TokenType.RIGHT_BRACE); break;
      case ',': addTokenToList(TokenType.COMMA); break;
      case '.': addTokenToList(TokenType.DOT); break;
      case '-': addTokenToList(TokenType.MINUS); break;
      case '+': addTokenToList(TokenType.PLUS); break;
      case ';': addTokenToList(TokenType.SEMICOLON); break;
      case '*': addTokenToList(TokenType.STAR); break;
      // handle slash operator (conflict with comment marker)
      case '/':
        if (matchNextChar('/')) {
          // iterate curr pointer to next line (since a comment takes the entire line)
          while (curr < input.length() && input.charAt(curr) != '\n') {
            curr++;
          }
        } else {
          addTokenToList(TokenType.SLASH);
        }
        break;
      // match potential two character lexemes
      case '!':
        addTokenToList(matchNextChar('=') ? TokenType.NOT_EQUAL : TokenType.NOT);
        break;
      case '=':
        addTokenToList(matchNextChar('=') ? TokenType.EQUAL_EQUAL : TokenType.EQUAL);
        break;
      case '>':
        addTokenToList(matchNextChar('=') ? TokenType.GREATER_EQUAL : TokenType.GREATER);
        break;
      case '<':
        addTokenToList(matchNextChar('=') ? TokenType.LESS_EQUAL : TokenType.LESS);
        break;
      // default case where character is not recognized
      default:
        System.out.println("Unrecognized character.");
        break;
    }
  }

  // match second character in two character lexemes
  private boolean matchNextChar(char nextChar) {
    // store char that curr is pointing to
    char newCurrChar = input.charAt(curr);
    // ensure curr is still within the input string
    // check if next character matches nextChar
    if (curr < input.length() && newCurrChar == nextChar) {
      curr++;
      return true;
    }
    return false;
  }

  // add scanned token to tokens list field
  private void addTokenToList(TokenType token) {
    // extract lexeme substring from input string
    tokens.add(new Token(token, input.substring(start, curr), line));
  }
}

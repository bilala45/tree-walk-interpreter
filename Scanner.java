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
    tokens = new ArrayList<Token>;
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
    tokens.add(new Token(EOF, "", line))
    return tokens;
  }

  // scan single token
  private Token scanSingleToken() {
    // fill with switch statements that check string and token pairs
    // store char at current index of string
    char currChar = input.charAt(curr);
    // check currChar against single character options
    // if currChar matches option, add corresponding token to list
    switch (currChar) {
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
          // HANDLE COMMENTS
        } else {
          addTokenToList(TokenType.SLASH);
        }
        break;
      // match potential two character lexemes
      case '!':
        addTokenToList(matchNextChar('=') ? TokenType.NOT_EQUAL : TokenType.NOT);
      case '=':
        addTokenToList(matchNextChar('=') ? TokenType.EQUAL_EQUAL : TokenType.EQUAL);
      case '>':
        addTokenToList(matchNextChar('=') ? TokenType.GREATER_EQUAL : TokenType.GREATER);
      case '<':
        addTokenToList(matchNextChar('=') ? TokenType.LESS_EQUAL : TokenType.LESS);
      // default case where character is not recognized
      default:
        System.out.println("Unrecognized character.")
        break;
    }
    // move curr forward
    curr++;
  }

  // match second character in two character lexemes
  private boolean matchNextChar(char nextChar) {
    // iterate curr forward
    curr++;
    // ensure curr is still within the input string
    if (curr < input.length()) {
      // check if second character matches '=' sign
      return input.charAt(curr) == nextChar;
    }
    return false;
  }

  // add scanned token to tokens list field
  private void addTokenToList(TokenType token) {
    // extract lexeme substring from input string
    tokens.add(new Token(token, input.substring(start, curr), line));
  }
}

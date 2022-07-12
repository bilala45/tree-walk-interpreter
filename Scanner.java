package lox_interpreter;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class Scanner {

  // input text
  private String input;
  // list to hold tokens
  private List<Token> tokens;
  // curr tracks current position in lexeme
  // start tracks the start of the lexeme
  private int start, curr;
  // track line in file
  int line;

  // hash map to store language keywords
  // static field -> common to all instances of class
  private static final Map<String, TokenType> KEYWORDS;

  // static blocks are blocks of code needed for initialization
  // in this case, we're using a static block to hash lox's keywords with its Token
  static {
    KEYWORDS = new HashMap<>();
    KEYWORDS.put("and", TokenType.AND);
    KEYWORDS.put("class", TokenType.CLASS);
    KEYWORDS.put("else", TokenType.ELSE);
    KEYWORDS.put("false", TokenType.FALSE);
    KEYWORDS.put("fun", TokenType.FUN);
    KEYWORDS.put("for", TokenType.FOR);
    KEYWORDS.put("if", TokenType.IF);
    KEYWORDS.put("nil", TokenType.NIL);
    KEYWORDS.put("or", TokenType.OR);
    KEYWORDS.put("print", TokenType.PRINT);
    KEYWORDS.put("return", TokenType.RETURN);
    KEYWORDS.put("super", TokenType.SUPER);
    KEYWORDS.put("this", TokenType.THIS);
    KEYWORDS.put("true", TokenType.TRUE);
    KEYWORDS.put("var", TokenType.VAR);
    KEYWORDS.put("while", TokenType.WHILE);
  }

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
      // handle strings
      case '"':
        string();
        break;
      /* default case:
       * checks if character is a letter or underscore -> IDENTIFIER token
       * checks if character is a number -> NUMBER token
       * else block handles unrecognized characters
       */
      default:
        // handles floating point numbers (can't begin with a decimal)
        if (Character.isLetter(currChar) || currChar == '_') {
          identifier();
        } else if (Character.isDigit(currChar)) {
          number();
        } else {
          System.out.println("Unrecognized character.");
        }
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

  // helper method to handle strings
  private void string() {
    // iterate through string until end quotes are reached
    while (input.charAt(curr) != '"') {
      // handle end of input or new line without closing string quotes
      if (curr >= input.length() || input.charAt(curr) == '\n') {
        System.out.println("Unfinished string.");
        return;
      }
      // update curr pointer within string
      curr++;
    }
    // pass in substring with updated start to trim quotes off stored lexeme
    addTokenToList(TokenType.STRING, input.substring(start + 1, curr));
    // update curr to pass end quote
    curr++;
  }

  // helper method to loop alphanumeric characters in identifier
  private void identifier() {
    // iterate through characters as long as alphanumeric or underscore
    while (Character.isLetterOrDigit(input.charAt(curr)) || input.charAt(curr) == '_') {
      // handle end of input or new line
      if (curr >= input.length() || input.charAt(curr) == '\n') {
        break;
      }
      curr++;
    }

    // store identifier in a string
    String currIden = input.substring(start, curr);

    // check identifier against keyword by determining if key is in hashmap
    if (KEYWORDS.containsKey(currIden)) {
      // retrieve token from hash map
      addTokenToList(KEYWORDS.get(currIden));
    } else {
      // process sequence as general identifier
      addTokenToList(TokenType.IDENTIFIER);
    }
  }

  // helper method to handle floating point numbers
  private void number() {
    loopDigits();
    // check if curr is pointing at a decimal
    if (input.charAt(curr) == '.') {
      // update curr to next character and check ahead to determine if next character is also a decimal
      curr++;
      // if next character is a decimal, move back and exit to add token
      if (input.charAt(curr) == '.') {
        curr--;
      // if only one decimal is present, iterate through digits after the decimal and then add token for number
      } else {
        loopDigits();
      }
    }
    addTokenToList(TokenType.NUMBER);
  }

  // helper method to loop digits in number
  private void loopDigits() {
    // iterate through number as long as current characters are numbers
    while (Character.isDigit(input.charAt(curr))) {
      // handle end of input or new line
      if (curr >= input.length() || input.charAt(curr) == '\n') {
        break;
      }
      curr++;
    }
  }

  // add scanned token to tokens list
  private void addTokenToList(TokenType token) {
    // extract lexeme substring from input string
    tokens.add(new Token(token, input.substring(start, curr), line));
  }

  // overload addTokenToList method with substring argument
  private void addTokenToList(TokenType token, String substring) {
    // extract lexeme substring from input string
    tokens.add(new Token(token, substring, line));
  }
}

package lox_interpreter;

public class Token {

  // type of token
  private TokenType tokenType;
  // lexeme associated with token
  private String lexeme;
  // tracks line where token was found
  private int line;

  // constructor
  public Token(TokenType tokenType, String lexeme, int line) {
    this.tokenType = tokenType;
    this.lexeme = lexeme;
    this.line = line;
  }
}

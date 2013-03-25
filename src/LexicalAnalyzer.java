import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;

public class LexicalAnalyzer {
    private InputStream inputStream;
    private int currentCharacter;
    private int currentPosition;
    private Token currentToken;

    public LexicalAnalyzer(InputStream inputStream) throws ParseException {
        this.inputStream = inputStream;
        currentPosition = 0;
        nextCharacter();
    }

    private void nextCharacter() throws ParseException {
        currentPosition++;

        try {
            currentCharacter = inputStream.read();
        } catch (IOException e) {
            throw new ParseException(e.getMessage(), currentPosition);
        }
    }

    private boolean isBlank(int character) {
        return character == ' ' || character == '\n' || character == '\t' || character == '\r';
    }

    private boolean isLetter(int character) {
        return (character >= 'a' && character <= 'z') || (character >= 'A' && character <= 'Z');
    }

    public Token getCurrentToken() {
        return currentToken;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void nextToken() throws ParseException {
        while (isBlank(currentCharacter)) {
            nextCharacter();
        }

        if (isLetter(currentCharacter)) {
            nextCharacter();
            currentToken = Token.VARIABLE;
            return;
        }

        switch (currentCharacter) {
            case '(':
                nextCharacter();
                currentToken = Token.LEFT_PARENTHESIS;
                break;
            case ')':
                nextCharacter();
                currentToken = Token.RIGHT_PARENTHESIS;
                break;
            case '|':
            case '^':
            case '&':
            case '!':
                nextCharacter();
                currentToken = Token.OPERATOR;
                break;
            case -1:
                currentToken = Token.END;
                break;
            default:
                throw new ParseException("Illegal character " + (char) currentCharacter, currentPosition);
        }
    }
}

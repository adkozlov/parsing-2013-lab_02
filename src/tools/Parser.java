package tools;

import java.io.InputStream;
import java.text.ParseException;
import java.util.*;

public class Parser {
    final private static Map<Token, String> TOKEN_STRING_MAP = Collections.unmodifiableMap(new HashMap<Token, String>() {{
        put(Token.VARIABLE, "variable");
        put(Token.OR_OPERATOR, "'|'");
        put(Token.XOR_OPERATOR, "'^'");
        put(Token.AND_OPERATOR, "'&'");
        put(Token.NOT_OPERATOR, "'!'");
        put(Token.LEFT_PARENTHESIS, "'('");
        put(Token.RIGHT_PARENTHESIS, "')'");
        put(Token.END, "end of expression");
    }});

    final private static String EXCEPTION_STRING = "expected at position";

    private String makeExceptionString(Token... tokens) {
        String s = "";

        for (Iterator<Token> iterator = Arrays.asList(tokens).listIterator(); iterator.hasNext(); ) {
            s += TOKEN_STRING_MAP.get(iterator.next()) + " ";
            if (iterator.hasNext()) {
                s += "/ ";
            }
        }

        return s + EXCEPTION_STRING;
    }

    private ParseException makeParseException(Token... tokens) {
        return new ParseException(makeExceptionString(tokens), lexicalAnalyzer.getCurrentPosition() - 1);
    }

    private void checkIfLeftParenthesis() {
        if (lexicalAnalyzer.getCurrentToken() == Token.LEFT_PARENTHESIS) {
            balance++;
        }
    }

    private void checkIfRightParenthesis(Token... tokens) throws ParseException {
        if (lexicalAnalyzer.getCurrentToken() == Token.RIGHT_PARENTHESIS) {
            if (balance > 0) {
                balance--;
            } else {
                throw makeParseException(tokens);
            }
        }
    }

    private LexicalAnalyzer lexicalAnalyzer;
    private int balance = 0;

    public Tree parse(InputStream inputStream) throws ParseException {
        lexicalAnalyzer = new LexicalAnalyzer(inputStream);
        lexicalAnalyzer.nextToken();
        return E();
    }

    private Tree E() throws ParseException {
        final String name = "E";

        switch (lexicalAnalyzer.getCurrentToken()) {
            case VARIABLE:
            case LEFT_PARENTHESIS:
            case NOT_OPERATOR:
                checkIfLeftParenthesis();

                // A
                Tree sub = A();
                // E'
                Tree cont = EPrime();

                return new Tree(name, sub, cont);
            default:
                throw makeParseException(Token.VARIABLE, Token.LEFT_PARENTHESIS, Token.NOT_OPERATOR);
        }
    }

    private Tree EPrime() throws ParseException {
        final String name = "E'";

        switch (lexicalAnalyzer.getCurrentToken()) {
            case OR_OPERATOR:
                // |
                lexicalAnalyzer.nextToken();
                // A
                Tree sub = A();
                // E'
                Tree cont = EPrime();

                return new Tree(name, new Tree(TOKEN_STRING_MAP.get(Token.OR_OPERATOR)), sub, cont);
            case RIGHT_PARENTHESIS:
            case END:
                checkIfRightParenthesis(Token.OR_OPERATOR, Token.END);

                // eps
                return new Tree(name);
            default:
                throw makeParseException(Token.OR_OPERATOR, Token.RIGHT_PARENTHESIS, Token.END);
        }
    }

    private Tree A() throws ParseException {
        final String name = "A";

        switch (lexicalAnalyzer.getCurrentToken()) {
            case VARIABLE:
            case LEFT_PARENTHESIS:
            case NOT_OPERATOR:
                checkIfLeftParenthesis();

                // B
                Tree sub = B();
                // A'
                Tree cont = APrime();

                return new Tree(name, sub, cont);
            default:
                throw makeParseException(Token.VARIABLE, Token.LEFT_PARENTHESIS, Token.NOT_OPERATOR);

        }
    }

    private Tree APrime() throws ParseException {
        final String name = "A'";

        switch (lexicalAnalyzer.getCurrentToken()) {
            case XOR_OPERATOR:
                // ^
                lexicalAnalyzer.nextToken();
                // B
                Tree sub = B();
                // A'
                Tree cont = APrime();

                return new Tree(name, new Tree(TOKEN_STRING_MAP.get(Token.XOR_OPERATOR)), sub, cont);
            case OR_OPERATOR:
            case RIGHT_PARENTHESIS:
            case END:
                checkIfRightParenthesis(Token.XOR_OPERATOR, Token.OR_OPERATOR, Token.END);

                // eps
                return new Tree(name);
            default:
                throw makeParseException(Token.XOR_OPERATOR, Token.OR_OPERATOR, Token.RIGHT_PARENTHESIS, Token.END);
        }
    }

    private Tree B() throws ParseException {
        final String name = "B";

        switch (lexicalAnalyzer.getCurrentToken()) {
            case VARIABLE:
            case LEFT_PARENTHESIS:
            case NOT_OPERATOR:
                checkIfLeftParenthesis();

                // C
                Tree sub = C();
                // B'
                Tree cont = BPrime();

                return new Tree(name, sub, cont);
            default:
                throw makeParseException(Token.VARIABLE, Token.LEFT_PARENTHESIS, Token.NOT_OPERATOR);

        }
    }

    private Tree BPrime() throws ParseException {
        final String name = "B'";

        switch (lexicalAnalyzer.getCurrentToken()) {
            case AND_OPERATOR:
                // &
                lexicalAnalyzer.nextToken();
                // C
                Tree sub = C();
                // B'
                Tree cont = BPrime();

                return new Tree(name, new Tree(TOKEN_STRING_MAP.get(Token.AND_OPERATOR)), sub, cont);
            case XOR_OPERATOR:
            case OR_OPERATOR:
            case RIGHT_PARENTHESIS:
            case END:
                checkIfRightParenthesis(Token.AND_OPERATOR, Token.XOR_OPERATOR, Token.OR_OPERATOR, Token.END);

                // eps
                return new Tree(name);
            default:
                throw makeParseException(Token.AND_OPERATOR, Token.XOR_OPERATOR, Token.OR_OPERATOR, Token.RIGHT_PARENTHESIS, Token.END);
        }
    }

    private Tree C() throws ParseException {
        final String name = "C";

        switch (lexicalAnalyzer.getCurrentToken()) {
            case VARIABLE:
            case LEFT_PARENTHESIS:
                checkIfLeftParenthesis();

                // D
                Tree sub = D();

                return new Tree(name, sub);
            case NOT_OPERATOR:
                // !
                lexicalAnalyzer.nextToken();
                // C
                sub = C();

                return new Tree(name, new Tree(TOKEN_STRING_MAP.get(Token.NOT_OPERATOR)), sub);
            default:
                throw makeParseException(Token.VARIABLE, Token.LEFT_PARENTHESIS, Token.NOT_OPERATOR);
        }
    }

    private Tree D() throws ParseException {
        final String name = "D";

        switch (lexicalAnalyzer.getCurrentToken()) {
            case VARIABLE:
                // v
                lexicalAnalyzer.nextToken();

                return new Tree(name, new Tree(TOKEN_STRING_MAP.get(Token.VARIABLE)));
            case LEFT_PARENTHESIS:
                balance++;

                // (
                lexicalAnalyzer.nextToken();
                // E
                Tree sub = E();

                if (lexicalAnalyzer.getCurrentToken() == Token.RIGHT_PARENTHESIS) {
                    balance--;
                } else {
                    throw makeParseException(Token.RIGHT_PARENTHESIS);
                }
                // )
                lexicalAnalyzer.nextToken();

                return new Tree(name, new Tree(TOKEN_STRING_MAP.get(Token.LEFT_PARENTHESIS)), sub, new Tree(TOKEN_STRING_MAP.get(Token.RIGHT_PARENTHESIS)));
            default:
                throw makeParseException(Token.VARIABLE, Token.LEFT_PARENTHESIS);
        }
    }
}

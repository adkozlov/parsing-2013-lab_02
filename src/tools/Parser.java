package tools;

import java.io.InputStream;
import java.text.ParseException;

public class Parser {
    private LexicalAnalyzer lexicalAnalyzer;

    public Tree parse(InputStream inputStream) throws ParseException {
        lexicalAnalyzer = new LexicalAnalyzer(inputStream);
        lexicalAnalyzer.nextToken();
        return E();
    }

    private Tree E() throws ParseException {
        switch (lexicalAnalyzer.getCurrentToken()) {
            case VARIABLE:
            case LEFT_PARENTHESIS:
            case NOT_OPERATOR:
                // A
                Tree sub = A();
                // E'
                Tree cont = EPrime();

                return new Tree("E", sub, cont);
            default:
                throw new AssertionError();
        }
    }

    private Tree EPrime() throws ParseException {
        switch (lexicalAnalyzer.getCurrentToken()) {
            case OR_OPERATOR:
                // |
                lexicalAnalyzer.nextToken();
                // A
                Tree sub = A();
                // E'
                Tree cont = EPrime();

                return new Tree("E'", new Tree("|"), sub, cont);
            case RIGHT_PARENTHESIS:
            case END:
                // eps
                return new Tree("E'");
            default:
                throw new AssertionError();
        }
    }

    private Tree A() throws ParseException {
        switch (lexicalAnalyzer.getCurrentToken()) {
            case VARIABLE:
            case LEFT_PARENTHESIS:
            case NOT_OPERATOR:
                // B
                Tree sub = B();
                // A'
                Tree cont = APrime();

                return new Tree("A", sub, cont);
            default:
                throw new AssertionError();

        }
    }

    private Tree APrime() throws ParseException {
        switch (lexicalAnalyzer.getCurrentToken()) {
            case XOR_OPERATOR:
                // ^
                lexicalAnalyzer.nextToken();
                // B
                Tree sub = B();
                // A'
                Tree cont = APrime();

                return new Tree("A'", new Tree("^"), sub, cont);
            case OR_OPERATOR:
            case RIGHT_PARENTHESIS:
            case END:
                // eps
                return new Tree("A'");
            default:
                throw new AssertionError();
        }
    }

    private Tree B() throws ParseException {
        switch (lexicalAnalyzer.getCurrentToken()) {
            case VARIABLE:
            case LEFT_PARENTHESIS:
            case NOT_OPERATOR:
                // C
                Tree sub = C();
                // B'
                Tree cont = BPrime();

                return new Tree("B", sub, cont);
            default:
                throw new AssertionError();

        }
    }

    private Tree BPrime() throws ParseException {
        switch (lexicalAnalyzer.getCurrentToken()) {
            case AND_OPERATOR:
                // &
                lexicalAnalyzer.nextToken();
                // C
                Tree sub = C();
                // B'
                Tree cont = BPrime();

                return new Tree("B'", new Tree("&"), sub, cont);
            case XOR_OPERATOR:
            case OR_OPERATOR:
            case RIGHT_PARENTHESIS:
            case END:
                // eps
                return new Tree("B'");
            default:
                throw new AssertionError();
        }
    }

    private Tree C() throws ParseException {
        switch (lexicalAnalyzer.getCurrentToken()) {
            case NOT_OPERATOR:
                // !
                lexicalAnalyzer.nextToken();
                // C
                Tree sub = C();

                return new Tree("C", new Tree("!"), sub);
            case VARIABLE:
            case LEFT_PARENTHESIS:
                // D
                sub = D();

                return new Tree("C", sub);
            default:
                throw new AssertionError();
        }
    }

    private Tree D() throws ParseException {
        switch (lexicalAnalyzer.getCurrentToken()) {
            case LEFT_PARENTHESIS:
                // (
                lexicalAnalyzer.nextToken();
                // E
                Tree sub = E();

                if (lexicalAnalyzer.getCurrentToken() != Token.RIGHT_PARENTHESIS) {
                    throw new ParseException(") expected at position", lexicalAnalyzer.getCurrentPosition());
                }
                // )
                lexicalAnalyzer.nextToken();

                return new Tree("D", new Tree("("), sub, new Tree(")"));
            case VARIABLE:
                // v
                lexicalAnalyzer.nextToken();

                return new Tree("D", new Tree("variable"));
            default:
                throw new AssertionError();
        }
    }
}

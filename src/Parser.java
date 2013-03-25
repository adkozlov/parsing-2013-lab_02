import java.io.InputStream;
import java.text.ParseException;

public class Parser {
    private LexicalAnalyzer lexicalAnalyzer;

    public Tree parse(InputStream inputStream) throws ParseException {
        lexicalAnalyzer = new LexicalAnalyzer(inputStream);
        lexicalAnalyzer.nextToken();
        return E();
    }

    private Tree E() {
        switch (lexicalAnalyzer.getCurrentToken()) {
            default:
                throw new AssertionError();
        }
    }
}

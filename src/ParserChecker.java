import java.io.InputStream;
import java.text.ParseException;

public class ParserChecker {

    public static void main(String[] args) {
        InputStream inputStream = System.in;

        try {
            LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(inputStream);

            while (lexicalAnalyzer.getCurrentToken() != Token.END) {
                lexicalAnalyzer.nextToken();
                System.out.println(lexicalAnalyzer.getCurrentToken());
            }
        } catch (ParseException e) {
            System.err.printf("Parse error: " + e.getLocalizedMessage());
        }
    }
}

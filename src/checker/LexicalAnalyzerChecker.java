package checker;

import tools.LexicalAnalyzer;
import tools.Token;

import java.text.ParseException;

public class LexicalAnalyzerChecker {

    public static void main(String[] args) {
        try {
            LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(System.in);

            while (lexicalAnalyzer.getCurrentToken() != Token.END) {
                lexicalAnalyzer.nextToken();
                System.out.println(lexicalAnalyzer.getCurrentToken());
            }
        } catch (ParseException e) {
            System.err.printf("Parse error: " + e.getLocalizedMessage());
        }
    }
}

package checkers;

import tools.LexicalAnalyzer;
import tools.Token;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.ParseException;

public class LexicalAnalyzerChecker {

    public static void main(String[] args) throws FileNotFoundException {
        try {
            LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(new FileInputStream("test.in"));

            while (lexicalAnalyzer.getCurrentToken() != Token.END) {
                lexicalAnalyzer.nextToken();
                System.out.println(lexicalAnalyzer.getCurrentToken());
            }
        } catch (ParseException e) {
            System.err.printf("Parse error: " + e.getLocalizedMessage());
        }
    }
}

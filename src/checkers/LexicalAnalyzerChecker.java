package checkers;

import tools.LexicalAnalyzer;
import tools.Token;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.ParseException;

public class LexicalAnalyzerChecker {

    public static void main(String[] args) {
        try {
            LexicalAnalyzer la = new LexicalAnalyzer(new FileInputStream("test.in"));

            while (la.getCurrentToken() != Token.END) {
                la.nextToken();
                System.out.println(la.getCurrentToken());
            }
        } catch (ParseException e) {
            System.err.printf("%s %d\n", e.getLocalizedMessage(), e.getErrorOffset());
        } catch (FileNotFoundException e) {
            System.err.println(e.getLocalizedMessage());
        }

    }
}

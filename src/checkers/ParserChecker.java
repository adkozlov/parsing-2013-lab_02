package checkers;

import tools.Parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.ParseException;

public class ParserChecker {

    public static void main(String[] args) throws FileNotFoundException {
        try {
            new Parser().parse(new FileInputStream("test.in"));
        } catch (ParseException e) {
            System.err.println("Parse error: " + e.getLocalizedMessage());
        }
    }
}

package checkers;

import tools.Parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.ParseException;

public class ParserChecker {

    public static void main(String[] args) {
        try {
            new Parser().parse(new FileInputStream("test.in"));
        } catch (ParseException e) {
            System.err.printf("%s %d\n", e.getLocalizedMessage(), e.getErrorOffset());
        } catch (FileNotFoundException e) {
            System.err.println(e.getLocalizedMessage());
        }

    }
}

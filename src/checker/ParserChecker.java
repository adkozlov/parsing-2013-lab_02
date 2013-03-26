package checker;

import tools.Parser;

import java.text.ParseException;

public class ParserChecker {

    public static void main(String[] args) {
        try {
            new Parser().parse(System.in);
        } catch (ParseException e) {
            System.err.println("Parse error: " + e.getLocalizedMessage());
        }
    }
}

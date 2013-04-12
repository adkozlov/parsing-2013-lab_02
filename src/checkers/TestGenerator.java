package checkers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class TestGenerator {

    final private static int CORRECT_TESTS_COUNT = 100;
    final private static int INCORRECT_TESTS_COUNT = 200;

    private static PrintWriter makePrintWriter(int index, boolean isCorrect) throws FileNotFoundException {
        return new PrintWriter("tests/" + (isCorrect ? "" : "in") + "correct/" + String.format("%03d", index + 1) + ".in");
    }

    public static void main(String[] args) throws FileNotFoundException {
        new File("tests/correct").mkdirs();
        new File("tests/incorrect").mkdirs();

        PrintWriter pw = null;

        for (int i = 0; i < CORRECT_TESTS_COUNT; i++) {
            pw = makePrintWriter(i, true);
            pw.println();
            pw.close();
        }

        for (int i = 0; i < INCORRECT_TESTS_COUNT; i++) {
            pw = makePrintWriter(i, false);
            pw.println();
            pw.close();
        }
    }
}

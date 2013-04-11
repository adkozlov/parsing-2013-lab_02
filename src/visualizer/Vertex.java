package visualizer;

public class Vertex {
    private String name;

    public Vertex(String name) {
        this.name = name;
    }

    public boolean isTerminal() {
        char c = name.charAt(0);
        return c != 'ε' && (c < 'A' || c > 'Z');
    }

    public boolean isEpsilon() {
        return name.charAt(0) == 'ε';
    }

    @Override
    public String toString() {
        return name;
    }
}

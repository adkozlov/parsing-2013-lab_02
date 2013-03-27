package visualizer;

public class Vertex {
    private String name;

    public Vertex(String name) {
        this.name = name;
    }

    public boolean isTerminal() {
        char c = name.charAt(0);
        return c >= 'A' && c <= 'Z';
    }

    @Override
    public String toString() {
        return name;
    }
}

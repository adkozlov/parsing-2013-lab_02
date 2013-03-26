package tools;

import java.util.Arrays;
import java.util.List;

public class Tree {
    private String node;
    List<Tree> children;

    public Tree(String node) {
        this.node = node;
    }

    public Tree(String node, Tree... children) {
        this.node = node;
        this.children = Arrays.asList(children);
    }
}

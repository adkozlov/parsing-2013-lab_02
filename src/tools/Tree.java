package tools;

import java.util.Arrays;
import java.util.List;

public class Tree {
    private String node;
    private List<Tree> children;

    @Deprecated
    public Tree(String node) {
        this.node = node;
        children = Arrays.asList();
    }

    public Tree(String node, Tree... children) {
        this.node = node;
        this.children = Arrays.asList(children);
    }

    public String getNode() {
        return node;
    }

    public List<Tree> getChildren() {
        return children;
    }
}

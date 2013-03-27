package visualizer;

import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedOrderedSparseMultigraph;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.ConstantTransformer;
import tools.Parser;
import tools.Tree;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.ParseException;

public class Visualizer {

    public static void main(String[] args) throws FileNotFoundException {
        try {
            visualize(new Parser().parse(new FileInputStream("test.in")));
        } catch (ParseException e) {
            System.err.printf("%s %d\n", e.getLocalizedMessage(), e.getErrorOffset());
        }
    }

    private static boolean isNonTerm(String string) {
        return ((int) string.charAt(0) >= 65 && (int) string.charAt(0) <= 90);
    }

    private static boolean isEps(String string) {
        return (string.equals("eps"));
    }

    private static void convert(Vertex vertex, Tree tree, DirectedGraph<Vertex, Edge> graph) {
        for (Tree child : tree.getChildren()) {
            Vertex current = new Vertex(child.getNode());
            graph.addEdge(new Edge("ok"), vertex, current);
            convert(current, child, graph);
        }
    }

    public static void visualize(Tree tree) {
        DirectedGraph<Vertex, Edge> graph = new DirectedOrderedSparseMultigraph<Vertex, Edge>();
        Vertex root = new Vertex(tree.getNode());
        convert(root, tree, graph);

        Forest<Vertex, Edge> forest = new DelegateForest<Vertex, Edge>(graph);

        TreeLayout<Vertex, Edge> treeLayout;
        VisualizationViewer visualizationViewer;

        treeLayout = new TreeLayout<Vertex, Edge>(forest);
        visualizationViewer = new VisualizationViewer<Vertex, Edge>(
                treeLayout, new Dimension(1024, 768));

        // sets edges style
        visualizationViewer.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line());
        // sets how to show cast Label to String
        visualizationViewer.getRenderContext().setVertexLabelTransformer(new Transformer<Vertex, String>() {
            public String transform(Vertex vertex) {
                return vertex.toString();
            }
        });
        // sets edge thickness
        visualizationViewer.getRenderContext().setEdgeStrokeTransformer(new ConstantTransformer<Stroke>(new BasicStroke(3f)));
        // sets position of Label
        visualizationViewer.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
        // sets vertex color
        visualizationViewer.getRenderContext().setVertexFillPaintTransformer(new Transformer<Vertex, Paint>() {
            public Paint transform(Vertex vertex) {
                String child = vertex.toString();
                if (isNonTerm(child)) {
                    return Color.GREEN;
                } else if (isEps(child)) {
                    return Color.white;
                } else {
                    return Color.RED;
                }
            }
        });

        // sets vertex size
        Ellipse2D circle = new Ellipse2D.Double(-15, -15, 30, 30);
        visualizationViewer.getRenderContext().setVertexShapeTransformer(new ConstantTransformer<Shape>(circle));

        visualizationViewer.setVertexToolTipTransformer(new ToStringLabeller());
        visualizationViewer.getRenderContext().setArrowFillPaintTransformer(new ConstantTransformer<Paint>(Color.lightGray));

        JFrame frame = new JFrame();

        GraphZoomScrollPane panel = new GraphZoomScrollPane(visualizationViewer);
        frame.getContentPane().add(panel);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}



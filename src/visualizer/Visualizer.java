package visualizer;

import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedOrderedSparseMultigraph;
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

public class Visualizer implements Runnable {

    private static final String FILENAME = "test.in";

    public static void main(String[] args) {
        new Thread(new Visualizer()).run();
    }

    private DirectedGraph<Vertex, Edge> graph = new DirectedOrderedSparseMultigraph<Vertex, Edge>();

    @Override
    public void run() {
        try {
            Tree root = new Parser().parse(new FileInputStream(FILENAME));
            visualize(root);

            JFrame frame = new JFrame();
            frame.getContentPane().add(new GraphZoomScrollPane(makeVisualizationViewer(new TreeLayout<Vertex, Edge>(new DelegateForest<Vertex, Edge>(graph)))));
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        } catch (ParseException e) {
            System.err.printf("%s %d\n", e.getLocalizedMessage(), e.getErrorOffset());
        } catch (FileNotFoundException e) {
            System.err.printf(e.getLocalizedMessage());
        }
    }

    private void convert(Vertex root, Tree tree) {
        for (Tree child : tree.getChildren()) {
            Vertex childVertex = new Vertex(child.getNode());
            graph.addEdge(new Edge(), root, childVertex);
            convert(childVertex, child);
        }
    }

    private VisualizationViewer<Vertex, Edge> makeVisualizationViewer(TreeLayout<Vertex, Edge> treeLayout) {
        VisualizationViewer result = new VisualizationViewer<Vertex, Edge>(
                treeLayout, new Dimension(1024, 768));

        // sets edges style
        result.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line());
        // sets how to show cast Label to String
        result.getRenderContext().setVertexLabelTransformer(new Transformer<Vertex, String>() {
            public String transform(Vertex vertex) {
                return vertex.toString();
            }
        });
        // sets edge thickness
        result.getRenderContext().setEdgeStrokeTransformer(new ConstantTransformer<Stroke>(new BasicStroke(3f)));
        // sets position of Label
        result.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
        // sets vertex color
        result.getRenderContext().setVertexFillPaintTransformer(new Transformer<Vertex, Paint>() {
            public Paint transform(Vertex vertex) {
                return vertex.isTerminal() ? Color.WHITE : Color.LIGHT_GRAY;
            }
        });

        // sets vertex size
        Ellipse2D circle = new Ellipse2D.Double(-15, -15, 30, 30);
        result.getRenderContext().setVertexShapeTransformer(new ConstantTransformer<Shape>(circle));

        result.setVertexToolTipTransformer(new ToStringLabeller());
        result.getRenderContext().setArrowFillPaintTransformer(new ConstantTransformer<Paint>(Color.WHITE));

        return result;
    }

    public void visualize(Tree tree) {
        Vertex root = new Vertex(tree.getNode());
        convert(root, tree);


    }
}



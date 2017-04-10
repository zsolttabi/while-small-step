package app;

import app.SimpleASTNode;
import ast.AST;
import com.google.code.javafxgraph.*;
import javafx.scene.control.Button;
import org.abego.treelayout.TreeForTreeLayout;
import org.abego.treelayout.util.DefaultTreeForTreeLayout;
import utils.SimpleTreeBuilder;
import utils.Tree;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TreeViewModel {

    public static Tree<SimpleASTNode> createSimpleTree(AST ast) {
        return new Tree<>(new SimpleTreeBuilder().visit(ast));
    }

    private static List<Point2D> calculateChildrenPositions(Tree.Node<?> node, Point2D nodePos, int nodeSize, int nodeDistance) {

        List<Integer> childSizes = node.getChildren()
                .stream()
                .map(n -> nodeSize(n, nodeSize))
                .collect(Collectors.toList());

        double y = nodePos.getY() + nodeDistance;

        if (childSizes.size() == 1) {

            return Collections.singletonList(new Point2D.Double(nodePos.getX(), y));

        } else if (childSizes.size() == 2) {

            Point2D left = new Point2D.Double(nodePos.getX() - childSizes.get(0) / 2.0, y);
            Point2D right = new Point2D.Double(nodePos.getX() + childSizes.get(1) / 2.0, y);

            return Arrays.asList(left, right);

        } else if (childSizes.size() == 3) {

            Point2D left = new Point2D.Double(nodePos.getX() - ((childSizes.get(0) / 2.0) + (childSizes.get(1) / 2.0)),
                    y);
            Point2D center = new Point2D.Double(nodePos.getX(), y);
            Point2D right = new Point2D.Double(nodePos.getX() + ((childSizes.get(2) / 2.0) + (childSizes.get(1) / 2.0)),
                    y);

            return Arrays.asList(left, center, right);
        }

        return Collections.emptyList();
    }

    private static <T> int nodeSize(Tree.Node<T> node, int leafSize) {
        return node.isLeaf() ? leafSize : node.getChildren().stream().mapToInt(n -> nodeSize(n, leafSize)).sum();
    }

    private static <T> FXNode drawNode(Tree.Node<T> node,
                                       Function<T, String> dataToColor,
                                       Function<T, String> dataToString,
                                       Point2D position,
                                       int baseX,
                                       int baseY,
                                       FXNodeBuilder nodeBuilder,
                                       FXEdgeBuilder edgeBuilder) {

        Button button = new Button();
        button.setText(dataToString.apply(node.getData()));
        button.setStyle("-fx-text-fill: " + dataToColor.apply(node.getData()) + ";");
        FXNode fxNode = nodeBuilder.node(button).x(position.getX()).y(position.getY()).build();

        List<Point2D> childrenPositions = calculateChildrenPositions(node, position, baseX, baseY);

        for (int i = 0; i < node.size(); ++i) {
            FXNode childFxNode = drawNode(node.getChild(i),
                    dataToColor,
                    dataToString,
                    childrenPositions.get(i),
                    baseX,
                    baseY,
                    nodeBuilder,
                    edgeBuilder);
            edgeBuilder.source(fxNode).destination(childFxNode).build();
        }

        return fxNode;
    }

    private static <T> FXGraph drawTree(Tree<T> tree, Function<T, String> dataToColor, Function<T, String> dataToString, Point2D position, int baseX, int baseY) {
        FXGraph graph = FXGraphBuilder.create().build();
        FXNodeBuilder theNodeBuilder = new FXNodeBuilder(graph);
        FXEdgeBuilder theEdgeBuilder = new FXEdgeBuilder(graph);
        drawNode(tree.getRoot(),
                dataToColor,
                dataToString,
                position,
                baseX,
                baseY,
                theNodeBuilder,
                theEdgeBuilder);
        return graph;
    }

    private static <T> void addChildren(DefaultTreeForTreeLayout<T> tree, Tree.Node<T> parent) {
        parent.getChildren().forEach(c -> {
            tree.addChild(parent.getData(), c.getData());
            addChildren(tree, c);
        });
    }

    public static <T> TreeForTreeLayout<T> convert(Tree.Node<T> root) {
        DefaultTreeForTreeLayout<T> tree = new DefaultTreeForTreeLayout<>(root.getData());
        addChildren(tree, root);
        return tree;
    }
}

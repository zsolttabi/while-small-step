package app;

import ast.AST;
import ast.expression.abstract_operations.ArithBinOp;
import ast.expression.values.BoolLiteral;
import ast.expression.Expression;
import ast.statement.If;
import ast.statement.Sequence;
import ast.statement.Statement;
import com.google.code.javafxgraph.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import lombok.val;
import app.AstNode.AstType;
import main.antlr4.com.while_parser.WhileParser;
import utils.Either;
import utils.SimpleTreeBuilder;
import utils.Tree;
import utils.Tree.Node;

import java.awt.geom.Point2D;
import java.util.function.Function;


public class Main extends Application {

    private static Tree<SimpleASTNode> createSimpleTree(AST ast) {
        return new Tree<>(new SimpleTreeBuilder().visit(ast.getStm()));
    }

    private static Point2D calculateChildPosition(int childIndex, int nodeSize, Point2D parentPos, double distanceX, double distanceY) {
        double y = parentPos.getY() + distanceY;
        double x = parentPos.getX() + (nodeSize == 1 ? 0 : (childIndex == 0 ? -distanceX : childIndex == 2 || (childIndex == 1 && nodeSize == 2)? distanceX : 0));
        return new Point2D.Double(x, y);
    }

    private static <T> FXNode drawNode(Node<T> node,
                                        Function<T, String> dataToString,
                                        Point2D position,
                                        double distanceX,
                                        double distanceY,
                                        FXNodeBuilder nodeBuilder,
                                        FXEdgeBuilder edgeBuilder) {

        Button button = new Button();
        button.setText(dataToString.apply(node.getData()));
        FXNode fxNode = nodeBuilder.node(button).x(position.getX()).y(position.getY()).build();

        for (int i = 0; i < node.size(); ++i) {
            Point2D childPos = calculateChildPosition(i, node.size(), position, distanceX, distanceY);
            FXNode childFxNode = drawNode(node.getChild(i), dataToString, childPos, distanceX, distanceY, nodeBuilder, edgeBuilder);
            edgeBuilder.source(fxNode).destination(childFxNode).build();
        }

        return fxNode;
    }

    private static <T> FXGraph drawTree(Tree<T> tree, Function<T, String> dataToString, Point2D position, double distanceX, double distanceY) {
        FXGraph graph = FXGraphBuilder.create().build();
        FXNodeBuilder theNodeBuilder = new FXNodeBuilder(graph);
        FXEdgeBuilder theEdgeBuilder = new FXEdgeBuilder(graph);
        drawNode(tree.getRoot(), dataToString, position, distanceX, distanceY, theNodeBuilder, theEdgeBuilder);
        return graph;
    }

    @Override
    public void start(Stage aStage) throws Exception {
        aStage.setMinWidth(800);
        aStage.setMinHeight(600);
        aStage.setTitle(getClass().getSimpleName());

        String example = "while tt do SKIP";
        AST ast = WhileParser.parseAst(example);

        Tree<SimpleASTNode> astTree = createSimpleTree(ast);

        aStage.setScene(new Scene(drawTree(astTree, SimpleASTNode::toString, new Point2D.Double(300, 50), 100, 100)));
        aStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

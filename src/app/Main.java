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
import utils.Tree;
import utils.Tree.Node;

import java.awt.geom.Point2D;
import java.util.function.Function;


public class Main extends Application {


    private static Node<AstNode<?>> createNode(Either<Statement, Expression> eitherStmExpr) {

        Node<AstNode<?>> node = new Node<>();
        if (eitherStmExpr.getLeft().isPresent()) {
            val left = eitherStmExpr.getLeft().get();
            if (left instanceof Sequence) {
                node.setData(new AstNode<>(AstType.Seq));
                node.addChild(createNode(Either.left(((Sequence) left).getS1())));
                node.addChild(createNode(Either.left(((Sequence) left).getS2())));
            } else if (left instanceof If) {
                node.setData(new AstNode<>(AstType.If));
                node.addChild(createNode(Either.right(((If) left).getCondition())));
                node.addChild(createNode(Either.left(((If) left).getS1())));
                node.addChild(createNode(Either.left(((If) left).getS2())));
            } else {
                node.setData(new AstNode<>(AstType.Skip));
            }
        } else if (eitherStmExpr.getRight().isPresent()){
            val right = eitherStmExpr.getRight().get();
            if (right instanceof ArithBinOp) {
                node.setData(new AstNode<>(AstType.IntBinOp));
                node.addChild(createNode(Either.right(((ArithBinOp) right).getLhs())));
                node.addChild(createNode(Either.right(((ArithBinOp) right).getRhs())));
            } else if (right instanceof BoolLiteral) {
                node.setData(new AstNode<>(AstType.BoolLit, ((BoolLiteral) right).getValue()));
            }
        }
        return node;
    }

    private static Tree<AstNode<?>> createTree(AST ast) {
        return new Tree<>(createNode(Either.left(ast.getStm())));
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

//        FXGraphBuilder theBuilder = FXGraphBuilder.create();
//        FXGraph theGraph = theBuilder.build();
//
//        List<FXNode> theNodes = new ArrayList<>();
//        int centerX = 400;
//        int centerY = 300;
//        int numNodes = 20;
//        int radius = 220;
//        for (int i = 0; i < numNodes; i++) {
//            Button button1 = new Button();
//            button1.setText("Node " + i);
//
//            double positionX = centerX + Math.cos(Math.toRadians(360 / numNodes * i)) * radius;
//            double positionY = centerY + Math.sin(Math.toRadians(360 / numNodes * i)) * radius;
//
//            FXNodeBuilder theNodeBuilder = new FXNodeBuilder(theGraph);
//            theNodes.add(theNodeBuilder.node(button1).x(positionX).y(positionY).build());
//        }
//
//        for (int i = 0; i < theNodes.size() - 1; i++) {
//
//            FXEdgeBuilder theEdgeBuilder = new FXEdgeBuilder(theGraph);
//            theEdgeBuilder.source(theNodes.get(i)).destination(theNodes.get(i+1)).build();
//        }

//        aStage.setScene(new Scene(theGraph));
//        AST ast = new AST(new Sequence(new If(new BoolLiteral(true), new Skip(), new Skip()), new Skip()));

        String example = "SKIP";
        AST ast = WhileParser.parseAst(example);

        Tree<AstNode<?>> astTree = createTree(ast);

        aStage.setScene(new Scene(drawTree(astTree, AstNode::toString, new Point2D.Double(300, 50), 100, 100)));
        aStage.show();
    }

//    @Override
//    public void start(Stage primaryStage) throws Exception{
//        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
//        primaryStage.setTitle("Hello World");
//        primaryStage.setScene(new Scene(root, 300, 275));
//        primaryStage.show();
//
//
//    }


    public static void main(String[] args) {
        launch(args);
    }
}

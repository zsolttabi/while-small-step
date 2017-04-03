package app;

import ast.AST;
import com.google.code.javafxgraph.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import main.antlr4.com.while_parser.WhileParser;
import utils.SimpleTreeBuilder;
import utils.Tree;
import utils.Tree.Node;

import java.awt.geom.Point2D;
import java.util.function.Function;


public class Main extends Application {

    private BorderPane mainPane;
    private String example = "x := 0;\nwhile x < 5 do\n    x :=  x + 1";

    private static Tree<SimpleASTNode> createSimpleTree(AST ast) {
        return new Tree<>(new SimpleTreeBuilder().visit(ast));
    }

    private static Point2D calculateChildPosition(int childIndex, int nodeSize, Point2D parentPos, double distanceX, double distanceY) {
        double y = parentPos.getY() + distanceY;
        double x = parentPos.getX() + (nodeSize == 1 ? 0 : (childIndex == 0 ? -distanceX : childIndex == 2 || (childIndex == 1 && nodeSize == 2)? distanceX : 0));
        return new Point2D.Double(x, y);
    }

    private static <T> FXNode drawNode(Node<T> node,
                                       Function<T, String> dataToColor,
                                        Function<T, String> dataToString,
                                        Point2D position,
                                        double distanceX,
                                        double distanceY,
                                        FXNodeBuilder nodeBuilder,
                                        FXEdgeBuilder edgeBuilder) {

        Button button = new Button();
        button.setText(dataToString.apply(node.getData()));
        button.setStyle("-fx-text-fill: " + dataToColor.apply(node.getData()) + ";");
        FXNode fxNode = nodeBuilder.node(button).x(position.getX()).y(position.getY()).build();

        for (int i = 0; i < node.size(); ++i) {
            Point2D childPos = calculateChildPosition(i, node.size(), position, distanceX * node.depth(), distanceY);
            FXNode childFxNode = drawNode(node.getChild(i), dataToColor, dataToString, childPos, distanceX, distanceY, nodeBuilder, edgeBuilder);
            edgeBuilder.source(fxNode).destination(childFxNode).build();
        }

        return fxNode;
    }

    private static <T> FXGraph drawTree(Tree<T> tree, Function<T, String> dataToColor, Function<T, String> dataToString, Point2D position, double distanceX, double distanceY) {
        FXGraph graph = FXGraphBuilder.create().build();
        FXNodeBuilder theNodeBuilder = new FXNodeBuilder(graph);
        FXEdgeBuilder theEdgeBuilder = new FXEdgeBuilder(graph);
        drawNode(tree.getRoot(), dataToColor, dataToString, position, distanceX, distanceY, theNodeBuilder, theEdgeBuilder);
        return graph;
    }

    private AST ast;

    @Override
    public void start(Stage aStage) throws Exception {
        aStage.setMinWidth(1000);
        aStage.setMinHeight(700);
        aStage.setTitle(getClass().getSimpleName());

        mainPane = new BorderPane();
        mainPane.setTop(top());
        mainPane.setLeft(left());
        mainPane.setRight(right());
        mainPane.setCenter(center(setAst(example)));

        Scene scene = new Scene(mainPane);
        scene.getStylesheets().add(this.getClass().getResource("style.css").toExternalForm());
        aStage.setScene(scene);
        aStage.show();
    }

    private VBox right() {
        VBox right = new VBox();

        return right;
    }

    private AST stepAst() {
        return ast = ast.step();
    }

    private AST setAst(String input) {
        return ast = WhileParser.parseAst(input);
    }

    private AST reduceAST() {
        return ast = ast.reduce();
    }

    private HBox top() {

        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        hbox.setStyle("-fx-background-color: #2c2e33; -fx-border-width: 0 0 3 0; -fx-border-color: #ccc7cb #ccc7cb #ccc7cb #ccc7cb;");

        return hbox;
    }

    private VBox left() {

        Button stepButton = new Button("Step");
        stepButton.setPrefSize(120, 20);
        stepButton.setOnAction(e -> mainPane.setCenter(center(stepAst())));

        Button reduceButton = new Button("Reduce");
        reduceButton.setPrefSize(120, 20);
        reduceButton.setOnAction(e -> mainPane.setCenter(center(reduceAST())));

        HBox buttonBox = new HBox();
        buttonBox.setPadding(new Insets(10, 15, 10, 15));
        buttonBox.setSpacing(10);
        buttonBox.getChildren().addAll(stepButton, reduceButton);

        TextArea textArea = new TextArea();
        textArea.setFont(Font.font("Courier New", FontWeight.BOLD, 14));
        textArea.setWrapText(false);
        textArea.setPrefColumnCount(30);
        textArea.setPrefRowCount(35);
        textArea.setText(example);
        textArea.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!oldValue.equals(newValue)) {
                mainPane.setCenter(center(setAst(newValue)));
            }
        });

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(8);
        vbox.setStyle("-fx-background-color: #2c2e33;  -fx-border-width: 0 3 0 0; -fx-border-color: #ccc7cb #ccc7cb #ccc7cb #ccc7cb;");
        vbox.getChildren().addAll(buttonBox, textArea);

        return vbox;
    }

    private ScrollPane center(AST ast) {


        GridPane grid = new GridPane();
        grid.setStyle("-fx-background-color: #4e5156;");
        grid.setHgap(10);
        grid.setPadding(new Insets(10));
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 10, 0, 10));

        FXGraph fxGraph = drawTree(createSimpleTree(ast),
                s -> s.isBad() ? "#ff0000" : "Black", SimpleASTNode::toString,
                new Point2D.Double(500, 100), 50, 50);
        fxGraph.setStyle("-fx-background-color: #4e5156;");

        return fxGraph;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

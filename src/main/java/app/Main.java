package app;

import antlr4.com.while_parser.WhileParser;
import ast.AST;
import com.google.code.javafxgraph.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import utils.SimpleTreeBuilder;
import utils.Tree;
import utils.Tree.Node;

import java.awt.geom.Point2D;
import java.util.function.Function;


public class Main extends Application {

    private BorderPane mainPane;
    private String example = "x := 0;\nwhile x < 5 do\n  x :=  x + 1";

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
        aStage.setMinWidth(1200);
        aStage.setMinHeight(700);
        aStage.setTitle(getClass().getSimpleName());

        setAst(example);
        mainPane = new BorderPane();
        mainPane.setTop(top());
        mainPane.setLeft(left());
//        mainPane.setRight(right());
        mainPane.setCenter(center());

        Scene scene = new Scene(mainPane);
        scene.getStylesheets().add(getClass().getClassLoader().getResource("style.css").toExternalForm());
        aStage.setScene(scene);
        aStage.show();
    }

//    private VBox right() {
//        VBox right = new VBox();
//
//        return right;
//    }

    private void stepAst() {
        ast = ast.step();
    }

    private void setAst(String input) {
        ast = WhileParser.parseAst(input);
    }

    private void reduceAST() {
        ast = ast.reduce();
    }

    private HBox top() {

        HBox hbox = new HBox();
        hbox.setPadding(new Insets(10, 12, 10, 12));
        hbox.setSpacing(10);
        hbox.setStyle("-fx-background-color: #2c2e33; -fx-border-width: 0 0 3 0; -fx-border-color: #ccc7cb #ccc7cb #ccc7cb #ccc7cb;");

        VBox vBox = new VBox();
        vBox.setPadding(new Insets(5, 10, 5, 10));
        vBox.setSpacing(15);

        Label vars = new Label("Variables:");
        vars.setTextFill(Paint.valueOf("#ffffff"));
        vars.setFont(Font.font("Courier New", FontWeight.BOLD, 14));

        Label values = new Label("Values:");
        values.setTextFill(Paint.valueOf("#ffffff"));
        values.setFont(Font.font("Courier New", FontWeight.BOLD, 14));

        vBox.getChildren().add(vars);
        vBox.getChildren().add(values);
        hbox.getChildren().add(vBox);

        ast.getState().entrySet().forEach(e -> {

            Label var = new Label(e.getKey().getIdentifier());
            var.setTextFill(Paint.valueOf("#d8d8d8"));
            var.setFont(Font.font("Courier New", FontWeight.NORMAL, 12));

            Label value = new Label(e.getValue().getValue().toString());
            value.setTextFill(Paint.valueOf("#d8d8d8"));
            value.setFont(Font.font("Courier New", FontWeight.NORMAL, 12));

            VBox varBox = new VBox();
            varBox.setPadding(new Insets(5, 10, 5, 10));
            varBox.setSpacing(15);
            varBox.getChildren().add(var);
            varBox.getChildren().add(value);

            hbox.getChildren().add(varBox);

        });

        return hbox;
    }

    private VBox left() {

        Button stepButton = new Button("Step");
        stepButton.setPrefSize(120, 20);
        stepButton.setOnAction(e -> {
            stepAst();
            mainPane.setCenter(center());
            mainPane.setTop(top());
        });

        Button reduceButton = new Button("Reduce");
        reduceButton.setPrefSize(120, 20);
        reduceButton.setOnAction(e -> {
            reduceAST();
            mainPane.setCenter(center());
            mainPane.setTop(top());
        });

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
                setAst(newValue);
                mainPane.setCenter(center());
            }
        });
        textArea.addEventFilter(KeyEvent.KEY_PRESSED, e -> {

            int tabWidth = 2;

            if (e.getCode() == KeyCode.TAB) {
                for (int i = 0; i < tabWidth; ++i) {
                    int caret = textArea.getCaretPosition();
                    if (e.isShiftDown()) {
                        if(caret > 0 && textArea.getText(caret - 1, caret).equals(" ")) {
                            textArea.deletePreviousChar();
                        }
                    } else {
                        textArea.insertText(caret, " ");
                    }
                }

                e.consume();
            }

        });

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(8);
        vbox.setStyle("-fx-background-color: #2c2e33;  -fx-border-width: 0 3 0 0; -fx-border-color: #ccc7cb #ccc7cb #ccc7cb #ccc7cb;");
        vbox.getChildren().addAll(buttonBox, textArea);

        return vbox;
    }

    private FXGraph center() {

        HBox hBox = new HBox();
        hBox.setStyle("-fx-background-color: #4e5156;");
        hBox.setPadding(new Insets(0));
        hBox.setPadding(new Insets(0, 0, 0, 0));

        FXGraph fxGraph = drawTree(createSimpleTree(ast),
                s -> s.isBad() ? "#ff0000" : "Black", SimpleASTNode::toString,
                new Point2D.Double(300, 100), 50, 50);
        fxGraph.setStyle("-fx-background-color: #4e5156;");

        hBox.getChildren().add(fxGraph);

        return fxGraph;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

package app;

import ast.AST;
import ast.IAST;
import ast.expression.abstract_operations.ArithBinOp;
import ast.expression.values.BoolLiteral;
import ast.expression.Expression;
import ast.statement.If;
import ast.statement.Sequence;
import ast.statement.Statement;
import com.google.code.javafxgraph.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
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
        return new Tree<>(new SimpleTreeBuilder().visit(ast));
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
            Point2D childPos = calculateChildPosition(i, node.size(), position, distanceX * node.depth(), distanceY);
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
        aStage.setMinWidth(1000);
        aStage.setMinHeight(700);
        aStage.setTitle(getClass().getSimpleName());

        String example = "x := 0; while x < 4 do x :=  x + 1";
        AST ast = WhileParser.parseAst(example);
        Tree<SimpleASTNode> astTree = createSimpleTree(ast);

        Button stepButton = new Button("Step");

        FXGraph fxGraph = drawTree(astTree, SimpleASTNode::toString, new Point2D.Double(500, 200), 50, 50);
        BorderPane border = new BorderPane();
        HBox hbox = new HBox();
        hbox.getChildren().add(stepButton);
        border.setTop(hbox);
        border.setLeft(addVBox());
        border.setCenter(fxGraph);
        stepButton.setOnAction(e -> border.setCenter(drawTree(createSimpleTree((AST)ast.step()), SimpleASTNode::toString, new Point2D.Double(500, 200), 50, 50)));


//        border.setCenter(addGridPane());
//        root.getChildren().add(stepButton);
//        root.getChildren().add(fxGraph);
        aStage.setScene(new Scene(border));
        aStage.show();
    }
    public HBox addHBox() {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        hbox.setStyle("-fx-background-color: #336699;");

        Button buttonCurrent = new Button("Current");
        buttonCurrent.setPrefSize(100, 20);

        Button buttonProjected = new Button("Projected");
        buttonProjected.setPrefSize(100, 20);
        hbox.getChildren().addAll(buttonCurrent, buttonProjected);

        return hbox;
    }

    public VBox addVBox() {
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(8);

        Text title = new Text("Data");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        vbox.getChildren().add(title);

        Hyperlink options[] = new Hyperlink[] {
                new Hyperlink("Sales"),
                new Hyperlink("Marketing"),
                new Hyperlink("Distribution"),
                new Hyperlink("Costs")};

        for (int i=0; i<4; i++) {
            VBox.setMargin(options[i], new Insets(0, 0, 0, 8));
            vbox.getChildren().add(options[i]);
        }

        return vbox;
    }

    public GridPane addGridPane() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 10, 0, 10));

        // Category in column 2, row 1
        Text category = new Text("Sales:");
        category.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        grid.add(category, 1, 0);

        // Title in column 3, row 1
        Text chartTitle = new Text("Current Year");
        chartTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        grid.add(chartTitle, 2, 0);

        // Subtitle in columns 2-3, row 2
        Text chartSubtitle = new Text("Goods and Services");
        grid.add(chartSubtitle, 1, 1, 2, 1);

        // Hous
        // Right label in column 4 (top), row 3
        Text servicesPercent = new Text("Services\n20%");
        GridPane.setValignment(servicesPercent, VPos.TOP);
        grid.add(servicesPercent, 3, 2);

        return grid;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

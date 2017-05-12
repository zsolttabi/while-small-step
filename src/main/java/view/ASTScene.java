package view;

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
import org.abego.treelayout.TreeForTreeLayout;
import org.abego.treelayout.TreeLayout;
import org.abego.treelayout.util.DefaultConfiguration;
import org.abego.treelayout.util.DefaultTreeForTreeLayout;
import program.Program;
import syntax.while_parser.WhileParser;
import utils.Tree;
import viewmodel.ASTNode;
import viewmodel.ASTNodeExtentProvider;
import viewmodel.ASTVisitor;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static viewmodel.ASTNode.NodeType;

public class ASTScene extends Scene {

    private static final String BACK_COLOR = "#2c2e33";
    private static final String BORDER_COLOR = "#ccc7cb";
    private static final String LIGHT_BACK_COLOR = "#686c6d";
    private Program program;
    private BorderPane mainPane;

    private static Map<NodeType, String> NODE_TYPE_TO_COLOR = new HashMap<>();
    static {
        NODE_TYPE_TO_COLOR.put(NodeType.NORMAL, "#000000");
        NODE_TYPE_TO_COLOR.put(NodeType.NEXT, "#3F92EA");
        NODE_TYPE_TO_COLOR.put(NodeType.STUCK, "#FF0000");
        NODE_TYPE_TO_COLOR.put(NodeType.TERMINATED, "#4CEA3F");
    }

    public ASTScene(String initialInput) {
        super(new BorderPane());
        setProgram(initialInput);

        mainPane = (BorderPane) getRoot();
        mainPane.setTop(top());
        mainPane.setLeft(left(initialInput));
        refreshCenter();

        URL style = getClass().getClassLoader().getResource("style.css");
        if (style != null) {
            getStylesheets().add(style.toExternalForm());
        }
    }

    public static ASTScene of(String initialInput) {
        return new ASTScene(initialInput);
    }

    private static <T> void addChildren(DefaultTreeForTreeLayout<T> tree, Tree.Node<T> parent) {
        parent.getChildren().forEach(c -> {
            tree.addChild(parent.getData(), c.getData());
            addChildren(tree, c);
        });
    }

    private static <T> TreeForTreeLayout<T> createTreeLayout(Tree.Node<T> root) {
        DefaultTreeForTreeLayout<T> tree = new DefaultTreeForTreeLayout<>(root.getData());
        addChildren(tree, root);
        return tree;
    }

    private void stepAst() {
        program.step();
    }

    private void setProgram(String input) {
        program = new Program(WhileParser.parse(input), 1000);
    }

    private void reduceAST() {
        program.reduce();
    }

    private HBox top() {

        HBox hbox = new HBox();
        hbox.setPadding(new Insets(10, 12, 10, 12));
        hbox.setSpacing(10);
        hbox.setStyle("-fx-background-color: " + BACK_COLOR + "; -fx-border-width: 0 0 3 0; -fx-border-color: " + BORDER_COLOR + " #ccc7cb #ccc7cb #ccc7cb;");

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

        program.getCurrentConfiguration().getState().entrySet().forEach(e -> {

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

    private VBox left(String initialInput) {

        Button stepButton = new Button("Step");
        stepButton.setPrefSize(120, 20);
        stepButton.setOnAction(e -> {
            stepAst();
            refreshCenter();
            mainPane.setTop(top());
        });

        Button reduceButton = new Button("Reduce");
        reduceButton.setPrefSize(120, 20);
        reduceButton.setOnAction(e -> {
            reduceAST();
            refreshCenter();
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

        textArea.setText(initialInput);
        textArea.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!oldValue.equals(newValue)) {
                setProgram(newValue);
                refreshCenter();
            }
        });
        textArea.addEventFilter(KeyEvent.KEY_PRESSED, e -> {

            int tabWidth = 2;

            if (e.getCode() == KeyCode.TAB) {
                for (int i = 0; i < tabWidth; ++i) {
                    int caret = textArea.getCaretPosition();
                    if (e.isShiftDown()) {
                        if (caret > 0 && textArea.getText(caret - 1, caret).equals(" ")) {
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
        vbox.setStyle(
                "-fx-background-color: " + BACK_COLOR + ";  -fx-border-width: 0 3 0 0; -fx-border-color: " + BORDER_COLOR + " #ccc7cb #ccc7cb #ccc7cb;");
        vbox.getChildren().addAll(buttonBox, textArea);

        return vbox;
    }

    private ASTPane<ASTNode> getTreePane() {

        TreeForTreeLayout<ASTNode> tree = ASTScene.createTreeLayout(ASTVisitor.visitAST(program).getRoot());
        TreeLayout<ASTNode> treeLayout = new TreeLayout<>(tree,
                new ASTNodeExtentProvider(30, 30),
                new DefaultConfiguration<>(50.0, 10.0));

        return new ASTPane<>(treeLayout,
                ASTNode::toString,
                s -> NODE_TYPE_TO_COLOR.get(s.getNodeType()),
                Font.font("Courier New", FontWeight.BOLD, 14));
    }

    private void refreshCenter() {
        mainPane.setCenter(getTreePane());
    }
}

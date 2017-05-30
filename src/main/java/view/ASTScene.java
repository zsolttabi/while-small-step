package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
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
import org.sonatype.inject.Nullable;
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
        NODE_TYPE_TO_COLOR.put(NodeType.SYNTAX_ERROR, "#c429ba");
    }

    public ASTScene(String initialInput) {
        super(new BorderPane());
        setProgram(initialInput);

        mainPane = (BorderPane) getRoot();
        refreshTop();
        refreshLeft(initialInput);
        refreshCenter();

        URL style = getClass().getClassLoader().getResource("style.css");
        if (style != null) {
            getStylesheets().add(style.toExternalForm());
        }
    }

    private void refreshLeft(String initialInput) {

        Button startButton = makeButton("Start", null);
        Button stopButton = makeButton("Stop", null);

        HBox startStopButtons = new HBox();
        startStopButtons.setPadding(new Insets(10, 15, 5, 15));
        startStopButtons.setSpacing(10);
        startStopButtons.getChildren().addAll(startButton, stopButton);

        Button firstButton = makeButton("First", e -> {
            program.first();
            refreshCenter();
            refreshTop();
        });
        Button stepButton = makeButton("Next", e -> {
            if (program.hasNext()) {
                program.next();
            }
            refreshCenter();
            refreshTop();
        });
        Button stepBackButton = makeButton("Prev", e -> {
            if (program.hasPrev()) {
                program.prev();
            }
            refreshCenter();
            refreshTop();
        });
        Button reduceButton = makeButton("Last", e -> {
            program.last();
            refreshCenter();
            refreshTop();
        });

        HBox stepButtons = new HBox();
        stepButtons.setPadding(new Insets(5, 15, 10, 15));
        stepButtons.setSpacing(10);
        stepButtons.getChildren().addAll(firstButton, stepBackButton, stepButton, reduceButton);

        TextArea codeEditorTextArea = new TextArea();
        codeEditorTextArea.setFont(Font.font("Courier New", FontWeight.BOLD, 14));
        codeEditorTextArea.setWrapText(false);
        codeEditorTextArea.setPrefColumnCount(30);
        codeEditorTextArea.setPrefRowCount(35);
        codeEditorTextArea.setText(initialInput);
        codeEditorTextArea.addEventFilter(KeyEvent.KEY_PRESSED, e -> customEdtiorBehavior(codeEditorTextArea, e));

        codeEditorTextArea.setEditable(true);
        stopButton.setDisable(true);
        stepButtons.setDisable(true);

        startButton.setOnAction(e -> {
            startButton.setDisable(true);
            codeEditorTextArea.setEditable(false);
            stopButton.setDisable(false);
            stepButtons.setDisable(false);
            setProgram(codeEditorTextArea.getText());
            refreshCenter();
        });

        stopButton.setOnAction(e -> {
            stopButton.setDisable(true);
            codeEditorTextArea.setEditable(true);
            stepButtons.setDisable(true);
            startButton.setDisable(false);
        });

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(8);
        vbox.setStyle("-fx-background-color: " + BACK_COLOR + "; -fx-border-width: 0 3 0 0; -fx-border-color: " + BORDER_COLOR + " #ccc7cb #ccc7cb #ccc7cb;");
        vbox.getChildren().addAll(startStopButtons, stepButtons, codeEditorTextArea);

        mainPane.setLeft(vbox);
    }

    private static void customEdtiorBehavior(TextArea codeEditorTextArea, KeyEvent e) {
        int tabWidth = 2;

        if (e.getCode() == KeyCode.TAB) {
            for (int i = 0; i < tabWidth; ++i) {
                int caret = codeEditorTextArea.getCaretPosition();
                if (e.isShiftDown()) {
                    if (caret > 0 && codeEditorTextArea.getText(caret - 1, caret).equals(" ")) {
                        codeEditorTextArea.deletePreviousChar();
                    }
                } else {
                    codeEditorTextArea.insertText(caret, " ");
                }
            }

            e.consume();
        }
    }

    private static Button makeButton(String first, @Nullable EventHandler<ActionEvent> actionEventEventHandler) {
        Button firstButton = new Button(first);
        firstButton.setPrefSize(50, 20);
        if (actionEventEventHandler != null ){
            firstButton.setOnAction(actionEventEventHandler);
        }
        return firstButton;
    }

    public static ASTScene of(String initialInput) {
        return new ASTScene(initialInput);
    }

    private static void setDisableOn(boolean value, Node... nodes) {
        for (Node node : nodes) {
            node.setDisable(value);
        }
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

    private void setProgram(String input) {
        program = new Program(WhileParser.parse(input), 1000);
    }

    private void refreshTop() {

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

        program.current().getState().entrySet().forEach(e -> {

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

        mainPane.setTop(hbox);
    }

    private void refreshCenter() {

        TreeForTreeLayout<ASTNode> tree = ASTScene.createTreeLayout(ASTVisitor.visitAST(program).getRoot());
        TreeLayout<ASTNode> treeLayout = new TreeLayout<>(tree,
                new ASTNodeExtentProvider(30, 30),
                new DefaultConfiguration<>(50.0, 10.0));

        mainPane.setCenter(new ASTPane<>(treeLayout,
                ASTNode::toString,
                s -> NODE_TYPE_TO_COLOR.get(s.getNodeType()),
                Font.font("Courier New", FontWeight.BOLD, 14))
        );
    }
}

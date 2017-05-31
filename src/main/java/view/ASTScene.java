package view;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
import viewmodel.SimpleAstNode;
import viewmodel.ASTNodeExtentProvider;
import viewmodel.SimpleAstBuilder;
import viewmodel.CodeWriter;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static viewmodel.SimpleAstNode.NodeType;

public class ASTScene extends Scene {

    private static final String BACK_COLOR = "#2c2e33";
    private static final String BORDER_COLOR = "#ccc7cb";
    private static final String LIGHT_BACK_COLOR = "#686c6d";
    private static final String LIGHT_GRAY_COLOR = "#bec0c4";
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

    private TableView<Variable> variableTable = new TableView<>();

    public ASTScene(String initialInput) {
        super(new BorderPane());
        setProgram(initialInput);

        mainPane = (BorderPane) getRoot();
        refreshLeft(initialInput);
        refreshCenter();

        URL style = getClass().getClassLoader().getResource("style.css");
        if (style != null) {
            getStylesheets().add(style.toExternalForm());
        }
    }

    private void refreshLeft(String initialInput) {

        Font codeEditorFont = Font.font("Courier New", FontWeight.BOLD, 14);

        TextArea codeEditorTextArea = new TextArea();
        codeEditorTextArea.setFont(codeEditorFont);
        codeEditorTextArea.setWrapText(false);
        codeEditorTextArea.setPrefColumnCount(30);
        codeEditorTextArea.setPrefRowCount(35);
        codeEditorTextArea.setText(initialInput);
        codeEditorTextArea.addEventFilter(KeyEvent.KEY_PRESSED, e -> customEditorBehavior(codeEditorTextArea, e));

        Button startButton = makeButton("Start", null);
        Button stopButton = makeButton("Stop", null);

        HBox startStopButtons = new HBox();
        startStopButtons.setPadding(new Insets(10, 15, 5, 15));
        startStopButtons.setSpacing(10);
        startStopButtons.getChildren().addAll(startButton, stopButton);

        Button firstButton = makeButton("First", e -> {
            program.first();
            codeEditorTextArea.setText(new CodeWriter().write(program));
            refreshCenter();
            updateVariableTable();
        });
        Button stepButton = makeButton("Next", e -> {
            if (program.hasNext()) {
                program.next();
                codeEditorTextArea.setText(new CodeWriter().write(program));
            }
            refreshCenter();
            updateVariableTable();
        });
        Button stepBackButton = makeButton("Prev", e -> {
            if (program.hasPrev()) {
                program.prev();
                codeEditorTextArea.setText(new CodeWriter().write(program));
            }
            refreshCenter();
            updateVariableTable();
        });
        Button reduceButton = makeButton("Last", e -> {
            program.last();
            codeEditorTextArea.setText(new CodeWriter().write(program));
            refreshCenter();
            updateVariableTable();
        });

        HBox stepButtons = new HBox();
        stepButtons.setPadding(new Insets(5, 15, 10, 15));
        stepButtons.setSpacing(10);
        stepButtons.getChildren().addAll(firstButton, stepBackButton, stepButton, reduceButton);

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

        final Label tableLabel = new Label("Variables");
        tableLabel.setFont(new Font("Arial", 20));

        TableColumn<Variable, String> identifierCol = new TableColumn<>("Identifier");
        identifierCol.setCellValueFactory(new PropertyValueFactory<>("identifier"));
        TableColumn<Variable, String> valueCol = new TableColumn<>("Value");
        valueCol.setCellValueFactory(new PropertyValueFactory<>("value"));

        variableTable.setEditable(false);
        variableTable.setSelectionModel(null);
        variableTable.getColumns().add(identifierCol);
        variableTable.getColumns().add(valueCol);
        variableTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        variableTable.setPlaceholder(new Label("Empty state"));
        updateVariableTable();

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(8);
        vbox.setStyle("-fx-background-color: " + BACK_COLOR + "; -fx-border-width: 0 3 0 0; -fx-border-color: " + BORDER_COLOR + " #ccc7cb #ccc7cb #ccc7cb;");
        vbox.getChildren().addAll(startStopButtons, stepButtons, codeEditorTextArea, variableTable);

        mainPane.setLeft(vbox);
    }

    private static void customEditorBehavior(TextArea codeEditorTextArea, KeyEvent e) {
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


    public static class Variable {

        private final SimpleStringProperty identifier;
        private final SimpleStringProperty value;

        public Variable(String identifier, String value) {
            this.identifier = new SimpleStringProperty(identifier);
            this.value = new SimpleStringProperty(value);
        }

        public String getIdentifier() {
            return identifier.get();
        }

        public void setIdentifier(String identifier) {
            this.identifier.set(identifier);
        }

        public String getValue() {
            return value.get();
        }

        public void setValue(String value) {
            this.value.set(value);
        }

    }

    private void updateVariableTable() {
        variableTable.setItems(FXCollections.observableList(program.current().getState().entrySet()
                .stream().map(e -> new Variable(e.getKey().getIdentifier(), e.getValue() == null ? "NA" : e.getValue().getValue().toString()))
                .collect(Collectors.toList())));
    }

    private void refreshCenter() {

        TreeForTreeLayout<SimpleAstNode> tree = ASTScene.createTreeLayout(SimpleAstBuilder.visitAST(program).getRoot());
        TreeLayout<SimpleAstNode> treeLayout = new TreeLayout<>(tree,
                new ASTNodeExtentProvider(30, 30),
                new DefaultConfiguration<>(50.0, 10.0));

        mainPane.setCenter(new ASTPane<>(treeLayout,
                SimpleAstNode::toString,
                s -> NODE_TYPE_TO_COLOR.get(s.getNodeType()),
                Font.font("Courier New", FontWeight.BOLD, 14))
        );
    }
}

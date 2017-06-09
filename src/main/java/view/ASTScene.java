package view;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.abego.treelayout.TreeForTreeLayout;
import org.abego.treelayout.TreeLayout;
import org.abego.treelayout.util.DefaultConfiguration;
import org.abego.treelayout.util.DefaultTreeForTreeLayout;
import org.sonatype.inject.Nullable;
import parser.WhileProgramParser;
import program.Program;
import utils.Tree;
import viewmodel.CodeWriter;
import viewmodel.SimpleAstBuilder;
import viewmodel.SimpleAstNode;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static viewmodel.SimpleAstNode.NodeType;

public class ASTScene extends Scene {

    private static final String BACK_COLOR = "#2c2e33";
    private static final String BORDER_COLOR = "#b8bbc1";
    private static final String LIGHT_BACK_COLOR = "#686c6d";
    private static final String LIGHT_GRAY_COLOR = "#bec0c4";
    private static final int DEFAULT_ALLOWED_PREFIX = 100;
    private static final Font REGULAR_BOLD = Font.font("System Regular", FontWeight.BOLD, 14);
    private static final Font REGULAR_FONT = Font.font("System Regular", 13);

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
    private int allowedPrefix = DEFAULT_ALLOWED_PREFIX;

    public ASTScene(String initialInput) {
        super(new BorderPane());
        mainPane = (BorderPane) getRoot();
        refreshLeft(initialInput);
        clearCenter();

        URL style = getClass().getClassLoader().getResource("style.css");
        if (style != null) {
            getStylesheets().add(style.toExternalForm());
        }
    }

    private void clearCenter() {
        program = null;

        Label label = new Label("No program loaded");
        label.setFont(REGULAR_FONT);
        label.setTextFill(Color.valueOf(BACK_COLOR));
        label.setAlignment(Pos.CENTER);

        HBox textBox = new HBox(label);
        textBox.setAlignment(Pos.CENTER);


        HBox hbox = new HBox(textBox);
        hbox.setStyle("-fx-background-color: " + LIGHT_BACK_COLOR);
        hbox.setAlignment(Pos.CENTER);

        VBox vbox = new VBox(hbox);
        vbox.setPadding(new Insets(10, 10, 10, 0));
        vbox.setStyle("-fx-background-color: " + BACK_COLOR);
        VBox.setVgrow(hbox, Priority.ALWAYS);

        mainPane.setCenter(vbox);
    }

    private void refreshLeft(String input) {

        Font codeEditorFont = Font.font("Courier New", FontWeight.BOLD, 14);

        TextArea codeEditorTextArea = new TextArea();
        codeEditorTextArea.setFont(codeEditorFont);
        codeEditorTextArea.setWrapText(false);
        codeEditorTextArea.setPrefColumnCount(30);
        codeEditorTextArea.setPrefRowCount(35);
        codeEditorTextArea.setText(input);
        codeEditorTextArea.addEventFilter(KeyEvent.KEY_PRESSED, e -> customEditorBehavior(codeEditorTextArea, e));

        Button startButton = makeButton("Start", null);
        Button stopButton = makeButton("Stop", null);

        HBox startStopButtons = new HBox();
        startStopButtons.setPadding(new Insets(5, 15, 5, 15));
        startStopButtons.setSpacing(10);
        startStopButtons.getChildren().addAll(startButton, stopButton);

        Button firstButton = makeButton("First", e -> {
            program.first();
            codeEditorTextArea.setText(new CodeWriter().write(program));
            setCenter();
            updateVariableTable();
        });
        Button stepButton = makeButton("Next", e -> {
            if (program.hasNext()) {
                program.next();
                codeEditorTextArea.setText(new CodeWriter().write(program));
            }
            setCenter();
            updateVariableTable();
        });
        Button stepBackButton = makeButton("Prev", e -> {
            if (program.hasPrev()) {
                program.prev();
                codeEditorTextArea.setText(new CodeWriter().write(program));
            }
            setCenter();
            updateVariableTable();
        });
        Button reduceButton = makeButton("Last", e -> {
            program.last();
            codeEditorTextArea.setText(new CodeWriter().write(program));
            setCenter();
            updateVariableTable();
        });

        HBox stepButtons = new HBox();
        stepButtons.setPadding(new Insets(5, 15, 10, 15));
        stepButtons.setSpacing(10);
        stepButtons.getChildren().addAll(firstButton, stepBackButton, stepButton, reduceButton);

        codeEditorTextArea.setEditable(true);
        stopButton.setDisable(true);
        stepButtons.setDisable(true);


        Spinner<Integer> spinner = new Spinner<>(1, 1000, DEFAULT_ALLOWED_PREFIX);
        spinner.setEditable(true);
        spinner.valueProperty().addListener((observable, oldValue, newValue) -> setAllowedPrefix(newValue));
        spinner.setMaxWidth(100);
        Label spinnerCaption = makeLabel("Allowed prefix:");

        HBox spinnerBox = new HBox();
        spinnerBox.getChildren().addAll(spinnerCaption, spinner);
        spinnerBox.setPadding(new Insets(10, 10, 5, 20));
        spinnerBox.setSpacing(15);

        startButton.setOnAction(e -> {
            startButton.setDisable(true);
            codeEditorTextArea.setEditable(false);
            stopButton.setDisable(false);
            stepButtons.setDisable(false);
            spinner.setDisable(true);
            createProgram(codeEditorTextArea.getText());
            setCenter();
        });

        stopButton.setOnAction(e -> {
            stopButton.setDisable(true);
            codeEditorTextArea.setEditable(true);
            stepButtons.setDisable(true);
            startButton.setDisable(false);
            spinner.setDisable(false);
            clearCenter();
        });

        TableColumn<Variable, String> identifierCol = new TableColumn<>("Identifier");
        identifierCol.setCellValueFactory(new PropertyValueFactory<>("identifier"));
        identifierCol.setSortable(false);
        TableColumn<Variable, String> valueCol = new TableColumn<>("Value");
        valueCol.setCellValueFactory(new PropertyValueFactory<>("value"));
        valueCol.setSortable(false);

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
        vbox.setStyle("-fx-background-color: " + BACK_COLOR + "; -fx-border-width: 0 0 0 0; ");
        vbox.getChildren().addAll(spinnerBox, startStopButtons, stepButtons, codeEditorTextArea, /*tableLabel,*/ variableTable);

        mainPane.setLeft(vbox);
    }

    private static Label makeLabel(String text) {
        Label tableLabel = new Label(text);
        tableLabel.setFont(REGULAR_BOLD);
        tableLabel.setTextFill(Color.WHITE);
        return tableLabel;
    }

    private static void customEditorBehavior(TextArea codeEditorTextArea, KeyEvent e) {
        int tabWidth = CodeWriter.BASE_INDENT.length();

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
        if (actionEventEventHandler != null) {
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

    private void createProgram(String input) {
        program = new Program(WhileProgramParser.parse(input), allowedPrefix);
    }

    public void setAllowedPrefix(int allowedPrefix) {
        this.allowedPrefix = allowedPrefix;
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
        if (program == null) {
            return;
        }
        variableTable.setItems(FXCollections.observableList(program.current().getState().entrySet()
                .stream().map(e -> new Variable(e.getKey().getIdentifier(), e.getValue() == null ? "NA" : e.getValue().getValue().toString()))
                .collect(Collectors.toList())));
    }

    private void setCenter() {

        TreeForTreeLayout<SimpleAstNode> tree = ASTScene.createTreeLayout(SimpleAstBuilder.visitAST(program).getRoot());
        TreeLayout<SimpleAstNode> treeLayout = new TreeLayout<>(tree,
                new ASTNodeExtentProvider(30, 30),
                new DefaultConfiguration<>(50.0, 10.0));

        ASTPane<SimpleAstNode> treePane = new ASTPane<>(treeLayout,
                SimpleAstNode::toString,
                s -> NODE_TYPE_TO_COLOR.get(s.getNodeType()),
                ASTNodeExtentProvider.TREE_NODE_FONT);

        treePane.setPadding(new Insets(10, 10, 10, 0));
        treePane.setStyle("  -fx-background-color: " + BACK_COLOR + "; -fx-border-width: 0 0 0 0 ");

        mainPane.setCenter(treePane);
    }
}

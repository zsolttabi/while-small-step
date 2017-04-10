package app;

import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import org.abego.treelayout.TreeLayout;

import java.util.function.Function;


public class TreePane<T> extends ScrollPane {

    private final TreeCanvas<T> canvas;

    public TreePane(TreeLayout<T> treeLayout, Function<T, String> toStringFun, Function<T, String> toColorFun, Font font) {

        canvas = new TreeCanvas<>(treeLayout, toStringFun, toColorFun, font);

        StackPane canvasContainer = new StackPane(canvas);
        canvasContainer.setPadding(new Insets(20, 20, 20, 20));

        setContent(canvasContainer);

        viewportBoundsProperty().addListener(
                (observableValue, oldBounds, newBounds) -> canvasContainer.setPrefSize(
                        Math.max(canvas.getBoundsInParent().getMaxX(), newBounds.getWidth()),
                        Math.max(canvas.getBoundsInParent().getMaxY(), newBounds.getHeight())
                ));

    }

}
package view;

import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.abego.treelayout.TreeForTreeLayout;
import org.abego.treelayout.TreeLayout;

import java.util.function.Function;


public class ASTCanvas<T> extends Canvas {

    private final static int ARC_SIZE = 10;
    private final static Color BOX_COLOR = Color.LIGHTGRAY;
    private final static Color BORDER_COLOR = Color.DARKGRAY;
    private final TreeLayout<T> treeLayout;
    private final Function<T, String> toStringFun;
    private final Function<T, String> toColorFun;
    private final Font font;

    public ASTCanvas(TreeLayout<T> treeLayout, Function<T, String> toStringFun, Function<T, String> toColorFun, Font font) {
        super(treeLayout.getBounds().getBounds().getWidth(),
                treeLayout.getBounds().getBounds().getHeight());
        this.treeLayout = treeLayout;
        this.toStringFun = toStringFun;
        this.toColorFun = toColorFun;
        this.font = font;
        widthProperty().addListener(evt -> draw());
        heightProperty().addListener(evt -> draw());
        draw();
    }

    private static double getCenterY(Rectangle2D b1) {
        return b1.getMinY() + b1.getHeight() / 2;
    }

    private static double getCenterX(Rectangle2D b1) {
        return b1.getMinX() + b1.getWidth() / 2;
    }

    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public double prefWidth(double height) {
        return getWidth();
    }

    @Override
    public double prefHeight(double width) {
        return getHeight();
    }

    private TreeForTreeLayout<T> getTree() {
        return treeLayout.getTree();
    }

    private Iterable<T> getChildren(T parent) {
        return getTree().getChildren(parent);
    }

    private Rectangle2D getBoundsOfNode(T node) {
        java.awt.geom.Rectangle2D.Double swingBounds = treeLayout.getNodeBounds().get(node);
        return new Rectangle2D(swingBounds.getMinX(),
                swingBounds.getMinY(),
                swingBounds.getWidth(),
                swingBounds.getHeight());
    }

    private void paintEdges(GraphicsContext gc, T parent) {
        if (!getTree().isLeaf(parent)) {
            Rectangle2D b1 = getBoundsOfNode(parent);


            double x1 = getCenterX(b1);
            double y1 = getCenterY(b1);
            for (T child : getChildren(parent)) {
                Rectangle2D b2 = getBoundsOfNode(child);
                gc.strokeLine(x1, y1, getCenterX(b2), getCenterY(b2));
                paintEdges(gc, child);
            }
        }
    }

    private void paintBox(GraphicsContext g, T node) {
        // draw the box in the background
        g.setStroke(BORDER_COLOR);
        g.setFill(BOX_COLOR);
        Rectangle2D box = getBoundsOfNode(node);
        g.fillRoundRect(box.getMinX(), box.getMinY(), box.getWidth() - 1,
                box.getHeight() - 1, ARC_SIZE, ARC_SIZE);
        g.strokeRoundRect(box.getMinX(), box.getMinY(), box.getWidth() - 1,
                (int) box.getHeight() - 1, ARC_SIZE, ARC_SIZE);

        g.setFont(font);
        g.setFill(Color.valueOf(toColorFun.apply(node)));

        Text text = new Text(toStringFun.apply(node));
        text.setFont(font);
        Bounds textBounds = text.getBoundsInLocal();

        double x = box.getMinX() + (box.getWidth() - textBounds.getWidth()) / 2 - ARC_SIZE / 4;
        double y = box.getMinY() + (box.getHeight() - textBounds.getHeight()) - ARC_SIZE / 2;

        g.fillText(text.getText(), x, y);

    }

    private void draw() {
        GraphicsContext gc = this.getGraphicsContext2D();

        paintEdges(gc, getTree().getRoot());
        for (T node : treeLayout.getNodeBounds().keySet()) {
            paintBox(gc, node);
        }
    }


}
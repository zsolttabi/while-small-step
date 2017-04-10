package app;

import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.abego.treelayout.TreeForTreeLayout;
import org.abego.treelayout.TreeLayout;

import java.util.function.Function;


public class TreePane<T> extends Pane {

    private final static int ARC_SIZE = 10;
    private final static Color BOX_COLOR = Color.LIGHTGRAY;
    private final static Color BORDER_COLOR = Color.DARKGRAY;
    private final Canvas canvas;
    private final TreeLayout<T> treeLayout;
    private final Function<T, String> toStringFun;
    private final Function<T, String> toColorFun;
    private final Font font;

    public TreePane(TreeLayout<T> treeLayout, Function<T, String> toStringFun, Function<T, String> toColorFun, Font font) {
        this.canvas = new Canvas(treeLayout.getBounds().getBounds().getWidth(),
                treeLayout.getBounds().getBounds().getHeight());
        this.treeLayout = treeLayout;
        this.toStringFun = toStringFun;
        this.toColorFun = toColorFun;
        this.font = font;
        getChildren().add(canvas);
        paint();
    }

    private static double getCenterY(Rectangle2D b1) {
        return b1.getMinY() + b1.getHeight() / 2;
    }

    private static double getCenterX(Rectangle2D b1) {
        return b1.getMinX() + b1.getWidth() / 2;
    }

    public Canvas getCanvas() {
        return canvas;
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

    @Override
    protected void layoutChildren() {
        final double x = snappedLeftInset();
        final double y = snappedTopInset();
        final double w = snapSize(getWidth()) - x - snappedRightInset();
        final double h = snapSize(getHeight()) - y - snappedBottomInset();
        canvas.setLayoutX(x);
        canvas.setLayoutY(y);
        canvas.setWidth(w);
        canvas.setHeight(h);
    }

    private void paintEdges(GraphicsContext gc, T parent) {
        if (!getTree().isLeaf(parent)) {
            Rectangle2D b1 = getBoundsOfNode(parent);


            double x1 = getCenterX(b1);
            double y1 = getCenterY(b1);
            for (T child : getChildren(parent)) {
                Rectangle2D b2 = getBoundsOfNode(child);
                gc.strokeLine((int) x1, (int) y1, (int) getCenterX(b2), // TODO: why cast is needed?
                        (int) getCenterY(b2));
                paintEdges(gc, child);
            }
        }
    }

    private void paintBox(GraphicsContext g, T node) {
        // draw the box in the background
        g.setStroke(BORDER_COLOR);
        g.setFill(BOX_COLOR);
        Rectangle2D box = getBoundsOfNode(node);
        g.fillRoundRect((int) box.getMinX(), (int) box.getMinY(), (int) box.getWidth() - 1,
                (int) box.getHeight() - 1, ARC_SIZE, ARC_SIZE);
        g.strokeRoundRect((int) box.getMinX(), (int) box.getMinY(), (int) box.getWidth() - 1,
                (int) box.getHeight() - 1, ARC_SIZE, ARC_SIZE);


		// draw the text on top of the box (possibly multiple lines)
        g.setFont(font);
		g.setFill(Color.valueOf(toColorFun.apply(node)));

        Text text = new Text(toStringFun.apply(node));
        text.setFont(font);
        Bounds textBounds = text.getBoundsInLocal();
        Text dummy = new Text("a");
        text.setFont(font);


//		double x = box.getMinX() + ARC_SIZE / 2 + textBounds.getWidth() / 2;
		double x = box.getMinX() + (box.getWidth() - textBounds.getWidth()) / 2 - dummy.getBoundsInLocal().getWidth() / 2;
		double y = box.getMinY() + (box.getHeight() - textBounds.getHeight()) / 2;

		g.fillText(text.getText(), x, y);

    }

    private void paint() {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        paintEdges(gc, getTree().getRoot());

        // paint the boxes
        for (T node : treeLayout.getNodeBounds().keySet()) {
            paintBox(gc, node);
        }
    }


}
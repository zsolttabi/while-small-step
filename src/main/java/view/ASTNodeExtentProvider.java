package view;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.abego.treelayout.NodeExtentProvider;
import viewmodel.SimpleAstNode;

public class ASTNodeExtentProvider implements NodeExtentProvider<SimpleAstNode> {

    public static final Font TREE_NODE_FONT = Font.font("Courier New", FontWeight.BOLD, 14);

    private final double baseWidth;
    private final double baseHeight;

    public ASTNodeExtentProvider(double baseWidth, double baseHeight) {
        this.baseWidth = baseWidth;
        this.baseHeight = baseHeight;
    }

    @Override
	public double getWidth(SimpleAstNode node) {
        Text text = new Text(node.toString());
        text.setFont(TREE_NODE_FONT);
        return baseWidth + text.getBoundsInLocal().getWidth();
	}

	@Override
	public double getHeight(SimpleAstNode node) {
        Text text = new Text(node.toString());
        text.setFont(TREE_NODE_FONT);
        return baseHeight + text.getBoundsInLocal().getHeight();
	}

}